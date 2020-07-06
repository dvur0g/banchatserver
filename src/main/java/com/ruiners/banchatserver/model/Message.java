package com.ruiners.banchatserver.model;

import java.util.Date;
import java.util.UUID;

public class Message {
    private final String id;
    private final String message;
    private final long timestamp;

    public Message(String message) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.timestamp = new Date().getTime();
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
