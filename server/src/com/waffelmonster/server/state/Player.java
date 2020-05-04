package com.waffelmonster.server.state;

import com.esotericsoftware.kryonet.Connection;

public class Player {
    private Connection connection;
    private String name;

    public Player(Connection connection, String name) {
        this.connection = connection;
        this.name = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }
}
