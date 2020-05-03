package com.waffelmonster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.waffelmonster.assets.Screens;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SandboxGame extends Game {

    private Injector injector;
    private AssetManager manager;

    private Map<Screens, Screen> screenCache;

    private ReadWriteLock transitionLock = new ReentrantReadWriteLock();
    public Screens transition;

    @Override
    public void create() {
        this.screenCache = new EnumMap<>(Screens.class);
        this.injector = Guice.createInjector(new GameModule(this));
        this.manager = injector.getInstance(AssetManager.class);
        this.updateScreen(Screens.CONNECT);
    }

    public void transition(final Screens screens) {
        this.transitionLock.writeLock().lock();
        this.transition = screens;
        this.transitionLock.writeLock().unlock();
    }

    private void updateScreen(final Screens screens) {
        this.setScreen(
                this.screenCache.computeIfAbsent(
                        screens,
                        enm -> injector.getInstance(enm.getScreenClass())
                )
        );

    }

    @Override
    public void render() {
        manager.update();

        if (transitionLock.readLock().tryLock() && this.transition != null) {
            this.updateScreen(this.transition);
        }
        transitionLock.readLock().unlock();

        super.render();
    }

}