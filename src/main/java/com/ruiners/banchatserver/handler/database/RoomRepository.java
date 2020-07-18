package com.ruiners.banchatserver.handler.database;

import com.ruiners.banchatserver.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
