package com.waffelmonster;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.waffelmonster.assets.Skins;
import com.waffelmonster.assets.Textures;
import com.waffelmonster.message.ConnectRequest;
import com.waffelmonster.message.ConnectResponse;

public class GameModule extends AbstractModule {

    public static final String NEON_SKIN = "neon_skin";
    public static final String SPLASH_TEXTURE = "splash_texture";

    @Override
    protected void configure() {
        // No Op
        this.bind(AssetManager.class).toInstance(new AssetManager());
    }

    @Provides
    @Singleton
    public Client provideClient() {
        final Client client = new Client();

        final Kryo kryo = client.getKryo();

        kryo.register(ConnectRequest.class);
        kryo.register(ConnectResponse.class);

        client.start();
        return client;
    }

    @Named(NEON_SKIN)
    @Provides
    public Skin provideNeonSkin(final AssetManager manager) {
        if (!manager.isLoaded(Skins.NEON.getPath(), Skin.class)
                || !manager.isLoaded(Skins.NEON.getAtlas(), TextureAtlas.class)) {
            load(manager, Skins.NEON);
            final Skin skin = manager.finishLoadingAsset(Skins.NEON.getPath());
            skin.addRegions(manager.finishLoadingAsset(Skins.NEON.getAtlas()));
            return skin;
        }
        return manager.get(Skins.NEON.getPath(), Skin.class);
    }

    @Named(SPLASH_TEXTURE)
    @Provides
    public Texture provideSplashTexture(final AssetManager manager) {
        return get(manager, Textures.SPLASH);
    }

    public void load(final AssetManager manager, final Skins skins) {
        manager.load(skins.getPath(), Skin.class);
        manager.load(skins.getAtlas(), TextureAtlas.class);
    }

    public void load(final AssetManager manager, final Textures textures) {
        manager.load(textures.getPath(), Texture.class);
    }

    public Texture get(final AssetManager manager, final Textures textures) {
        if (!manager.isLoaded(textures.getPath(), Texture.class)) {
            load(manager, textures);
            return manager.finishLoadingAsset(textures.getPath());
        }
        return manager.get(textures.getPath(), Texture.class);
    }

}
