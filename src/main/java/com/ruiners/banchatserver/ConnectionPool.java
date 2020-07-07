package com.ruiners.banchatserver;

import com.ruiners.banchatserver.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;

public final class ConnectionPool {

    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName(Config.JDBC_DRIVER);
            c = DriverManager.getConnection(Config.DATABASE_URL, Config.DATABASE_USER, Config.DATABASE_PASSWORD);
            c.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void close(AutoCloseable... objects) {
        try {
            for (AutoCloseable object : objects) {
                if (object != null) {
                    object.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
