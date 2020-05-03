package com.waffelmonster.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;
import com.waffelmonster.SandboxGame;
import com.waffelmonster.compat.JChangeListener;
import com.waffelmonster.compat.JTask;

import static com.kotcrab.vis.ui.widget.VisWindow.FADE_TIME;
import static com.waffelmonster.assets.Skins.NEON;
import static com.waffelmonster.assets.Textures.SPLASH;

public class ConnectScreen extends ScreenAdapter {

    private final Stage stage;

    private final Image splash;
    private final Label splashLabel;

    private final TextField connectionField;
    private final Label connectionFieldLabel;

    private final TextButton button;

    private final Label messageLabel;

    public ConnectScreen() {
        this.stage = new Stage();

        final Skin skin = SandboxGame.get(NEON);

        this.splash = new Image(SandboxGame.get(SPLASH));
        this.splashLabel = new Label("Sandbox", skin);
//        this.splashLabel.setFontScale(2);

        this.connectionField = new TextField("box.waffelmonster.com", skin);
        this.connectionFieldLabel = new Label("Enter Server Address:", skin);

        this.button = new TextButton("Connect", skin);

        this.messageLabel = new Label("Failed to connect", skin);

        this.stage.addActor(this.splash);
        this.stage.addActor(this.splashLabel);
        this.stage.addActor(this.connectionField);
        this.stage.addActor(this.connectionFieldLabel);
        this.stage.addActor(this.button);
        this.stage.addActor(this.messageLabel);

        this.connectionField.setWidth(250f);

        this.splash.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.splash.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 3f * 2f) - this.splash.getHeight() / 2f
        );
        this.splashLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.splashLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 2f) - this.splashLabel.getHeight() / 2f
        );
        this.connectionFieldLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.connectionFieldLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 3f) - this.connectionFieldLabel.getHeight() / 2f
        );
        this.connectionField.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.connectionField.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 4f) - this.connectionField.getHeight() / 2f
        );
        this.button.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.button.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 6f) - this.button.getHeight() / 2f
        );
        this.messageLabel.setPosition(
                (Gdx.graphics.getWidth() / 2f) - this.messageLabel.getWidth() / 2f,
                (Gdx.graphics.getHeight() / 12f) - this.messageLabel.getHeight() / 2f
        );

        this.messageLabel.setColor(Color.RED);
        this.messageLabel.setVisible(false);
        this.messageLabel.addAction(Actions.alpha(0));
        this.messageLabel.act(0);
        this.messageLabel.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(FADE_TIME), Actions.fadeOut(FADE_TIME))));

        this.stage.addListener(new JChangeListener((changeEvent, actor) -> {
            if (actor.equals(button)) {
                System.out.println("Attempting to connect");
                messageLabel.setVisible(true);
                Timer.schedule(new JTask(() -> messageLabel.setVisible(false)), 3.9f);
            }
        }));

        Gdx.input.setInputProcessor(this.stage);
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

}
