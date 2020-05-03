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
import com.waffelmonster.compat.JChangeListener;
import com.waffelmonster.compat.JTask;
import com.waffelmonster.message.ConnectRequest;
import com.waffelmonster.message.ConnectResponse;

import java.io.IOException;

import static com.kotcrab.vis.ui.widget.VisWindow.FADE_TIME;
import static com.waffelmonster.GameModule.NEON_SKIN;
import static com.waffelmonster.GameModule.SPLASH_TEXTURE;

public class ConnectScreen extends Listener implements Screen {

    private final Stage stage;

    private final Image splash;
    private final Label splashLabel;

    private final TextField connectionField;
    private final Label connectionFieldLabel;

    private final TextField usernameField;
    private final Label usernameLabel;

    private final TextButton button;
    private final Label messageLabel;

    private final Client client;

    @Inject
    public ConnectScreen(
            final Client client,
            @Named(NEON_SKIN) final Skin skin,
            @Named(SPLASH_TEXTURE) final Texture splash
            ) {
        this.client = client;
        this.client.addListener(this);

        this.stage = new Stage();

        this.splash = new Image(splash);
        this.splashLabel = new Label("Sandbox", skin);

        this.connectionField = new TextField("box.popcraft.org", skin);
        this.connectionFieldLabel = new Label("Enter Server Address:", skin);

        this.usernameField = new TextField("waffelmonster", skin);
        this.usernameLabel = new Label("Enter Username:", skin);

        this.button = new TextButton("Connect", skin);

        this.messageLabel = new Label(null, skin);
        this.messageLabel.setVisible(false);
        this.messageLabel.addAction(Actions.alpha(0));
        this.messageLabel.act(0);
        this.messageLabel.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(FADE_TIME), Actions.fadeOut(FADE_TIME))));

        this.stage.addActor(this.splash);
        this.stage.addActor(this.splashLabel);
        this.stage.addActor(this.connectionField);
        this.stage.addActor(this.connectionFieldLabel);
        this.stage.addActor(this.usernameField);
        this.stage.addActor(this.usernameLabel);
        this.stage.addActor(this.button);
        this.stage.addActor(this.messageLabel);

        this.connectionField.setWidth(250f);
        this.usernameField.setWidth(250f);

        this.splash.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.splash.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 4f * 3f) - this.splash.getHeight() / 2f
        );
        this.splashLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.splashLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 12f * 7) - this.splashLabel.getHeight() / 2f
        );
        this.connectionFieldLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.connectionFieldLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 11) - this.connectionFieldLabel.getHeight() / 2f
        );
        this.connectionField.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.connectionField.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 9) - this.connectionField.getHeight() / 2f
        );
        this.usernameLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.usernameLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 7) - this.usernameLabel.getHeight() / 2f
        );
        this.usernameField.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.usernameField.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 5) - this.usernameField.getHeight() / 2f
        );
        this.button.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.button.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 24f * 3) - this.button.getHeight() / 2f
        );


        this.stage.addListener(new JChangeListener((changeEvent, actor) -> {
            if (actor.equals(button)) {
                this.messageLabel.setVisible(false);
                System.out.println("Attempting to connect");
                try {
                    this.client.connect(3000, this.connectionField.getText(), 42069);

                    final ConnectRequest request = new ConnectRequest();
                    request.playerName = this.usernameField.getText();

                    this.client.sendTCP(request);
                    System.out.println("Request sent");
                } catch (IOException e) {
                    System.out.println("Failed");
                    showMessage("Failed to connect to server", Color.RED);
                }
            }
        }));

        Gdx.input.setInputProcessor(this.stage);
    }

    private void showMessage(final String message, final Color color) {
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

    @Override
    public void received(Connection connection, Object object) {
        System.out.println("Response received " + object);
        if (object instanceof ConnectResponse) {
            final ConnectResponse response = (ConnectResponse) object;
            if (response.success) {
                System.out.println("Transition");
                this.showMessage("Connection Successful", Color.GREEN);
            } else {
                this.showMessage("Username is already taken", Color.RED);
            }
        }
    }

    @Override
    public void pause() {
        //No Op
    }

    @Override
    public void resume() {
        //No Op
    }

    @Override
    public void hide() {
        //No Op
    }

    @Override
    public void show() {
        //No Op
    }

}
