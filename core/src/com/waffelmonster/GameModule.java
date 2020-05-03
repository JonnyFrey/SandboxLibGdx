package com.waffelmonster;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.waffelmonster.assets.Skins;
import com.waffelmonster.message.ConnectRequest;
import com.waffelmonster.message.ConnectResponse;
import com.waffelmonster.message.KryoUtils;

public class GameModule extends AbstractModule {

    public static final String NEON_SKIN = "neon_skin";

    private final SandboxGame game;

    public GameModule(final SandboxGame game) {
        this.game = game;
    }

    @Override
    protected void configure() {
        this.bind(AssetManager.class).toInstance(new AssetManager());
        this.bind(SandboxGame.class).toInstance(this.game);
    }

    @Provides
    @Singleton
    public Client provideClient() {
        final Client client = new Client();
        KryoUtils.registerMessages(client.getKryo());
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

    public void load(final AssetManager manager, final Skins skins) {
        manager.load(skins.getPath(), Skin.class);
        manager.load(skins.getAtlas(), TextureAtlas.class);
    }

}
