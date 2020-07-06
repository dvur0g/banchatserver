package com.ruiners.banchatserver.handler;

import com.google.gson.Gson;
import com.ruiners.banchatserver.model.Message;
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
    private final List<SocketIoSocket> clients = new ArrayList<>();
    private final Gson gson = new Gson();

    SocketIoHandler() {
        SocketIoServer serverSocket = new SocketIoServer(serverEngine);
        SocketIoNamespace namespace = serverSocket.namespace("/");

        namespace.on("connect", socket -> {
            SocketIoSocket client = (SocketIoSocket) socket[0];
            clients.add(client);

            client.on("message", args -> {
                Message message = gson.fromJson((String) args[0], Message.class);
                System.out.println(client.getId() + " shared a message on the namespace: " + "-" + message.getMessage());

                client.emit("message", args);
            });

//            client.on("connect", authentication -> {
//                Credentials credentials = gson.fromJson((String) authentication[0], Credentials.class);
//
//                System.out.println("Got credentials: " + credentials.getUsername() + "-" + credentials.getPassword());
//                //client.disconnect(true);
//            });

            System.out.println("New connection " + client.getId());
        });
    }

    @RequestMapping(value = "/socket.io/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public void httpHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        serverEngine.handleRequest(request, response);
    }

}
