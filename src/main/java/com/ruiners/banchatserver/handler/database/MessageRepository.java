package com.ruiners.banchatserver.handler.database;

import com.ruiners.banchatserver.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> getMessageByRoom(Long room);
}
