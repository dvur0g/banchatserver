package com.ruiners.banchatserver.handler;

import com.google.gson.Gson;
import com.ruiners.banchatserver.config.Config;
import com.ruiners.banchatserver.model.Client;
import com.ruiners.banchatserver.model.Message;
import com.ruiners.banchatserver.model.Room;
import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SocketIoHandler {
    private final EngineIoServer serverEngine = new EngineIoServer();
    private final List<Client> clients = new ArrayList<>();
    private final Gson gson = new Gson();

    SocketIoHandler() {
        SocketIoServer serverSocket = new SocketIoServer(serverEngine);
        SocketIoNamespace namespace = serverSocket.namespace("/");

        namespace.on("connection", socket -> {
            Client client = new Client(Config.DEFAULT_ROOM, (SocketIoSocket) socket[0]);
            clients.add(client);

            client.getSocket().on("chat message", args -> {
                Message message = gson.fromJson((String) args[0], Message.class);
                DatabaseHandler.insertMessage(message);

                for (Client participant : clients) {
                    if (participant.getRoom() == message.getRoom())
                        participant.getSocket().send("chat message", args);
                }
                System.out.println(client.getSocket().getId() + " to room " + message.getRoom() + " >> " + message.getMessage());
            });

            client.getSocket().on("enter room", room -> {
                client.setRoom(gson.fromJson((String) room[0], Long.class));
                System.out.println("client " + client.getSocket().getId() + " entered room " + client.getRoom());

                if (client.getRoom() != Config.DEFAULT_ROOM)
                    client.getSocket().send("last messages", gson.toJson(DatabaseHandler.getMessages(client.getRoom())));
            });

            client.getSocket().on("new room", args -> {
                Room room = gson.fromJson((String) args[0], Room.class);
                DatabaseHandler.insertRoom(room);

                client.getSocket().send("get rooms", gson.toJson(DatabaseHandler.getRooms()));
            });

            client.getSocket().send("get rooms", gson.toJson(DatabaseHandler.getRooms()));

            System.out.println("New connection " + client.getSocket().getId());
        });
    }

    @RequestMapping(value = "/socket.io/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public void httpHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        serverEngine.handleRequest(request, response);
    }

}
