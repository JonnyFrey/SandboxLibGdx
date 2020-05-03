package com.waffelmonster.server.state;

import com.esotericsoftware.kryonet.Connection;

import java.util.HashMap;

public class Room {
    private String name;
    private HashMap<Connection, Player> players;

    public Room(String name) {
        this.name = name;
        this.players = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer(Connection connection) {
        return this.players.get(connection);
    }

    public boolean join(Connection connection, Player player) {
        boolean hasJoined = this.players.containsKey(connection);
        if (!hasJoined) {
            this.players.put(connection, player);
        }
        return !hasJoined;
    }

    public boolean quit(Connection connection) {
        return this.players.remove(connection) != null;
    }
}
