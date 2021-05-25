package com.ruiners.banchatserver.model;

import io.socket.socketio.server.SocketIoSocket;

public class Client {
    private long room;
    private SocketIoSocket socket;

    public Client(long room, SocketIoSocket socket) {
        this.room = room;
        this.socket = socket;
    }

    public long getRoom() {
        return room;
    }

    public SocketIoSocket getSocket() {
        return socket;
    }

    public void setRoom(long room) {
        this.room = room;
    }

    public void setSocket(SocketIoSocket socket) {
        this.socket = socket;
    }
}
