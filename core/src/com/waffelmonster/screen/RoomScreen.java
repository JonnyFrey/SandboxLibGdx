package com.waffelmonster.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.waffelmonster.SandboxGame;

import static com.waffelmonster.GameModule.NEON_SKIN;

public class RoomScreen extends Listener implements Screen {

    private final SandboxGame game;
    private final Client client;
    private final Skin skin;
    private Stage stage;

    @Inject
    public RoomScreen(
            final SandboxGame game,
            final Client client,
            @Named(NEON_SKIN) final Skin skin
    ) {
        this.game = game;
        this.client = client;
        this.skin = skin;
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        // Dark Lavender for the background
        Gdx.gl.glClearColor(0f, 1f, 0.588f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        this.stage = new Stage();
        Window main = new Window("Main", this.skin);
        main.setColor(Color.PINK);
        Window controls = new Window("Controls", this.skin);
        main.setColor(Color.PURPLE);

        SplitPane splitPane = new SplitPane(main, controls, false, this.skin);
        splitPane.setSplitAmount(0.75f);
        splitPane.setSize(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        this.stage.addActor(splitPane);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
