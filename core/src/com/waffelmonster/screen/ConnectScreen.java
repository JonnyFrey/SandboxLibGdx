package com.waffelmonster.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.waffelmonster.SandboxGame;
import com.waffelmonster.assets.AssetUtils;
import com.waffelmonster.assets.Screens;
import com.waffelmonster.assets.Textures;
import com.waffelmonster.compat.JChangeListener;
import com.waffelmonster.compat.JTask;
import com.waffelmonster.message.ConnectRequest;
import com.waffelmonster.message.ConnectResponse;

import java.io.IOException;

import static com.kotcrab.vis.ui.widget.VisWindow.FADE_TIME;
import static com.waffelmonster.GameModule.NEON_SKIN;

public class ConnectScreen extends Listener implements Screen {

    private final SandboxGame game;
    private final Client client;
    private final Skin skin;
    private final Texture splashTexture;
    private final Label messageLabel;

    private Stage stage;

    @Inject
    public ConnectScreen(
            final SandboxGame game,
            final Client client,
            final AssetUtils assetUtils,
            @Named(NEON_SKIN) final Skin skin
    ) {
        this.game = game;
        this.client = client;
        this.skin = skin;
        this.splashTexture = assetUtils.get(Textures.SPLASH);

        this.messageLabel = new Label(null, skin);
        this.messageLabel.setVisible(false);
        this.messageLabel.addAction(Actions.alpha(0));
        this.messageLabel.act(0);
        this.messageLabel.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(FADE_TIME), Actions.fadeOut(FADE_TIME))));
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        // Dark Lavender for the background
        Gdx.gl.glClearColor(0.451f, 0.31f, 0.588f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }

    private void showMessage(final String message, final Color color) {
        if (this.messageLabel != null) {
            this.messageLabel.setVisible(false);
            this.messageLabel.setText(message);
            this.messageLabel.setColor(color);
            this.messageLabel.setPosition(
                    (Gdx.graphics.getWidth() / 2f) - this.messageLabel.getPrefWidth() / 2f,
                    (Gdx.graphics.getHeight() / 48f * 3) - this.messageLabel.getPrefHeight() / 2f
            );
            this.messageLabel.setVisible(true);
            Timer.schedule(new JTask(() -> this.messageLabel.setVisible(false)), 3.9f);
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        System.out.println("Response received " + object);
        if (object instanceof ConnectResponse) {
            final ConnectResponse response = (ConnectResponse) object;
            if (response.success) {
                System.out.println("Transition");
                this.game.transition(Screens.TIC_TAC_TOE);
                this.showMessage("Connection Successful", Color.GREEN);
            } else {
                this.showMessage("Username is already taken", Color.RED);
            }
        }
    }

    @Override
    public void show() {

        final Image splash = new Image(this.splashTexture);
        final Label splashLabel = new Label("Sandbox", skin);

        final TextField connectionField = new TextField("box.popcraft.org", skin);
        final Label connectionFieldLabel = new Label("Enter Server Address:", skin);

        final TextField usernameField = new TextField("waffelmonster", skin);
        final Label usernameLabel = new Label("Enter Username:", skin);

        final TextButton button = new TextButton("Connect", skin);

        this.stage = new Stage();

        this.stage.addActor(splash);
        this.stage.addActor(splashLabel);
        this.stage.addActor(connectionField);
        this.stage.addActor(connectionFieldLabel);
        this.stage.addActor(usernameField);
        this.stage.addActor(usernameLabel);
        this.stage.addActor(button);
        this.stage.addActor(this.messageLabel);

        connectionField.setWidth(250f);
        usernameField.setWidth(250f);

        splash.setPosition(
                (Gdx.graphics.getWidth() / 2f) - splash.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 4f * 3f) - splash.getHeight() / 2f
        );
        splashLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - splashLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 12f * 7) - splashLabel.getHeight() / 2f
        );
        connectionFieldLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - connectionFieldLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 11) - connectionFieldLabel.getHeight() / 2f
        );
        connectionField.setPosition(
                (Gdx.graphics.getWidth() / 2f) - connectionField.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 9) - connectionField.getHeight() / 2f
        );
        usernameLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - usernameLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 7) - usernameLabel.getHeight() / 2f
        );
        usernameField.setPosition(
                (Gdx.graphics.getWidth() / 2f) - usernameField.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 5) - usernameField.getHeight() / 2f
        );
        button.setPosition(
                (Gdx.graphics.getWidth() / 2f) - button.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 3) - button.getHeight() / 2f
        );

        button.addListener(new JChangeListener((changeEvent, actor) -> {
            this.messageLabel.setVisible(false);
            System.out.println("Attempting to connect");

            Timer.post(new JTask(() -> {
                try {
                    this.client.connect(1000, connectionField.getText(), 42069);

                    final ConnectRequest request = new ConnectRequest();
                    request.playerName = usernameField.getText();

                    this.client.sendTCP(request);
                    System.out.println("Request sent");
                } catch (IOException e) {
                    System.out.println("Failed");
                    showMessage("Failed to connect to server", Color.RED);
                }
            }));
            System.out.println("Job Done");
        }));

        Gdx.input.setInputProcessor(this.stage);
        this.client.addListener(this);
    }


    @Override
    public void pause() {
        this.stage.dispose();
        this.stage = null;
    }

    @Override
    public void resume() {
        this.show();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        this.client.removeListener(this);
    }

}
