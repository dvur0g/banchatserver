package com.ruiners.banchatserver.handler;

import com.ruiners.banchatserver.ConnectionPool;
import com.ruiners.banchatserver.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseHandler {

    public static void insertMessage(Message message) {
        PreparedStatement ps = null;
        try(Connection c = ConnectionPool.getConnection()) {
            ps = c.prepareStatement("INSERT INTO messages VALUES ((select nextval ('message_sequence')), ?, ?, ?)");
            ps.setString(1, message.getMessage());
            ps.setLong(2, message.getRoom());
            ps.setLong(3, message.getTimestamp());

            ps.executeUpdate();
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.close(ps);
        }
    }

    public static List<Message> getMessages(long room) {
        List<Message> messages = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try(Connection c = ConnectionPool.getConnection()) {
            ps = c.prepareStatement("SELECT * FROM messages WHERE room = ? ORDER BY timestamp");
            ps.setLong(1, room);

            rs = ps.executeQuery();
            while (rs.next())
                messages.add(new Message(rs.getLong("id"), rs.getString("text"),
                        rs.getLong("room"), rs.getLong("timestamp")));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.close(rs, ps);
        }

        return messages;
    }
}
