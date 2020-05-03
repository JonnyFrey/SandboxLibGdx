package com.waffelmonster.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.google.inject.Inject;

public class AssetUtils {

    private final AssetManager manager;

    @Inject
    public AssetUtils(final AssetManager manager) {
        this.manager = manager;
    }

    public void load(final Textures textures) {
        this.manager.load(textures.getPath(), Texture.class);
    }

    public Texture get(final Textures textures) {
        if (!this.manager.isLoaded(textures.getPath(), Texture.class)) {
            load(textures);
            return this.manager.finishLoadingAsset(textures.getPath());
        }
        return this.manager.get(textures.getPath(), Texture.class);
    }

}
