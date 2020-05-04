package com.waffelmonster.server.state;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private String name;
    private Map<Connection, Player> players;
    private List<ChatMessage> messages;

    public Room(String name) {
        this.name = name;
        this.players = new HashMap<>();
        this.messages = new ArrayList<>();
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

    public Map<Connection, Player> getPlayers() {
        return players;
    }

    public boolean join(Player player) {
        boolean hasJoined = this.players.containsKey(player.getConnection());
        if (!hasJoined) {
            this.players.put(player.getConnection(), player);
        }
        return !hasJoined;
    }

    public boolean quit(Connection connection) {
        return this.players.remove(connection) != null;
    }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }
}
