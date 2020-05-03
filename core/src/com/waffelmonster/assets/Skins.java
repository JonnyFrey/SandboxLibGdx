package com.waffelmonster.assets;

public enum Skins {
    NEON(
            "atlas/neon/skin/neon-ui.json",
            "atlas/neon/skin/neon-ui.atlas"
    );

    private final String path;
    private final String atlas;

    Skins(final String path, final String atlas) {
        this.path = path;
        this.atlas = atlas;
    }

    public String getPath() {
        return this.path;
    }

    public String getAtlas() {
        return atlas;
    }
}
