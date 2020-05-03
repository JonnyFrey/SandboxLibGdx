package com.waffelmonster.assets;

public enum Textures {

    SPLASH("sandbox.png");

    private final String path;

    Textures(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
