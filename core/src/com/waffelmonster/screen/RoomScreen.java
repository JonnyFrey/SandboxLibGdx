package com.waffelmonster.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.waffelmonster.SandboxGame;
import com.waffelmonster.actors.TicTacToeBoxActor;
import com.waffelmonster.assets.AssetUtils;
import com.waffelmonster.assets.Textures;

import static com.waffelmonster.GameModule.NEON_SKIN;

public class RoomScreen extends Listener implements Screen {

    private final SandboxGame game;
    private final Client client;
    private final Skin skin;

    private final Texture boardTexture;
    private final Texture xTexture;
    private final Texture oTexture;

    private Stage stage;

    @Inject
    public RoomScreen(
            final SandboxGame game,
            final Client client,
            @Named(NEON_SKIN) final Skin skin,
            final AssetUtils assetUtils
            ) {
        this.game = game;
        this.client = client;
        this.skin = skin;

        this.boardTexture = assetUtils.get(Textures.BOARD);
        this.xTexture = assetUtils.get(Textures.X);
        this.oTexture = assetUtils.get(Textures.O);

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

        Table board = new Table();

        board.setBackground(new TextureRegionDrawable(new TextureRegion(this.boardTexture)));

        final Image image = new Image();

        System.out.println(image.getWidth() + image.getHeight());

        final TextureRegionDrawable xImage = new TextureRegionDrawable(this.xTexture);
        final TextureRegionDrawable oImage = new TextureRegionDrawable(this.oTexture);
        final TextureRegionDrawable blank = new TextureRegionDrawable(new Texture(
                this.xTexture.getWidth(),
                this.xTexture.getHeight(),
                Pixmap.Format.Alpha
        ));

        final TicTacToeBoxActor.BoxBuilder builder = new TicTacToeBoxActor.BoxBuilder(
                blank, xImage, oImage
        );

        board.add(builder.create(0, 0));
        board.add(builder.create(0, 1));
        board.add(builder.create(0, 2));
        board.row();
        board.add(builder.create(1, 0));
        board.add(builder.create(1, 1));
        board.add(builder.create(1, 2));
        board.row();
        board.add(builder.create(2, 0));
        board.add(builder.create(2, 1));
        board.add(builder.create(2, 2));

        Window controls = new Window("Controls", this.skin);

        SplitPane splitPane = new SplitPane(board, controls, false, this.skin);
        splitPane.setSplitAmount(0.75f);
        splitPane.setSize(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        this.stage.addActor(splitPane);

        Gdx.input.setInputProcessor(this.stage);
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
