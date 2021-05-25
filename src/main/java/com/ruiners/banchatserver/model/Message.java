package com.ruiners.banchatserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String message;
    private Long room;
    private Long timestamp;

    public Message() {

    }

    public Message(Long id, String message, Long room, Long timestamp) {
        this.id = id;
        this.message = message;
        this.room = room;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Long getRoom() {
        return room;
    }

    public Long getTimestamp() {
        return timestamp;
    }

}
