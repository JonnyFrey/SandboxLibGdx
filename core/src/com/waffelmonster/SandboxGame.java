package com.waffelmonster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.waffelmonster.assets.Screens;
import com.waffelmonster.assets.Skins;
import com.waffelmonster.assets.Textures;

import java.util.EnumMap;
import java.util.Map;

public class SandboxGame extends Game {

    // TODO: Replace this using proper DI
    private static final AssetManager ASSET_MANAGER = new AssetManager();
    private final Map<Screens, Screen> screenCache = new EnumMap<>(Screens.class);

    @Override
    public void create() {
        this.setScreen(screenCache.computeIfAbsent(Screens.CONNECT, Screens::create));
    }

    @Override
    public void render() {
        ASSET_MANAGER.update();
        super.render();
    }

    public static void load(final Skins skins) {
        ASSET_MANAGER.load(skins.getPath(), Skin.class);
        ASSET_MANAGER.load(skins.getAtlas(), TextureAtlas.class);
    }

    public static void load(final Textures textures) {
        ASSET_MANAGER.load(textures.getPath(), Texture.class);
    }

    public static Skin get(final Skins skins) {
        if (!ASSET_MANAGER.isLoaded(skins.getPath(), Skin.class)
                || !ASSET_MANAGER.isLoaded(skins.getAtlas(), TextureAtlas.class)) {
            load(skins);
            final Skin skin = ASSET_MANAGER.finishLoadingAsset(skins.getPath());
            skin.addRegions(ASSET_MANAGER.finishLoadingAsset(skins.getAtlas()));
            return skin;
        }
        return ASSET_MANAGER.get(skins.getPath(), Skin.class);
    }

    public static Texture get(final Textures textures) {
        if (!ASSET_MANAGER.isLoaded(textures.getPath(), Texture.class)) {
            load(textures);
            return ASSET_MANAGER.finishLoadingAsset(textures.getPath());
        }
        return ASSET_MANAGER.get(textures.getPath(), Texture.class);
    }

}

