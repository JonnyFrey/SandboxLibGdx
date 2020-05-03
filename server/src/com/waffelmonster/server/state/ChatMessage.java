package com.waffelmonster.server.state;

public class ChatMessage {

    private Player player;
    private String message;

    public ChatMessage(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }
}
