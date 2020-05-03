package com.waffelmonster.server.state;

import java.util.HashMap;

public class Room {
    private String name;
    private HashMap<String, Player> players;

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

    public Player getPlayer(String player) {
        return this.players.get(player);
    }

    public boolean join(Player player) {
        boolean hasJoined = this.players.containsKey(player.getName());
        if (!hasJoined) {
            this.players.put(player.getName(), player);
        }
        return !hasJoined;
    }

    public boolean quit(Player player) {
        return this.players.remove(player.getName()) != null;
    }
}
