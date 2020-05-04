package com.waffelmonster.assets;

public enum Textures {

    SPLASH("sandbox.png"),

    BOARD("tic-tac-toe-board.jpg"),

    X("x_tictactoe.png"),

    O("o_tictactoe.png");

    private final String path;

    Textures(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
