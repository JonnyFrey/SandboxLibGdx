package com.waffelmonster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.waffelmonster.screen.ConnectScreen;

public class SandboxGame extends Game {

    private Injector injector;
    private AssetManager manager;

    @Override
    public void create() {
        this.injector = Guice.createInjector(new GameModule());
        this.manager = injector.getInstance(AssetManager.class);
        this.setScreen(injector.getInstance(ConnectScreen.class));
    }

    @Override
    public void render() {
        manager.update();
        super.render();
    }

}

