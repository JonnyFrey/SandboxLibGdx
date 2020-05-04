package com.waffelmonster.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.esotericsoftware.kryonet.Client;
import com.waffelmonster.message.tictactoe.MoveRequest;

public class TicTacToeBoxActor extends Image {

    public enum State {
        BLANK, X, O
    }

    private final Drawable blankDraw;
    private final Drawable xDraw;
    private final Drawable oDraw;
    private final int xPos;
    private final int yPos;
    private final Client client;

    public TicTacToeBoxActor(final Client client, final Drawable blankDraw, final Drawable xDraw, final Drawable oDraw, int xPos, int yPos) {
        this.client = client;
        this.blankDraw = blankDraw;
        this.xDraw = xDraw;
        this.oDraw = oDraw;

        this.setDrawable(blankDraw);

        this.xPos = xPos;
        this.yPos = yPos;

        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(super.touchDown(event, x, y, pointer, button)) {
                    System.out.println("Clicked " + xPos + " " + yPos);
                    final MoveRequest request = new MoveRequest();
                    request.x = xPos;
                    request.y = yPos;
                    client.sendTCP(request);
                    return true;
                }
                return false;
            }
        });
    }

    public void setState(final State state) {
        switch (state) {
            case BLANK:
                this.setDrawable(this.blankDraw);
                break;
            case X:
                this.setDrawable(this.xDraw);
                break;
            case O:
                this.setDrawable(this.oDraw);
                break;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public static class BoxBuilder {

        private final Client client;
        private final Drawable blank;
        private final Drawable x;
        private final Drawable o;

        public BoxBuilder(Client client, Drawable blank, Drawable x, Drawable o) {
            this.client = client;
            this.blank = blank;
            this.x = x;
            this.o = o;
        }

        public TicTacToeBoxActor create(int xPos, int yPos) {
            return new TicTacToeBoxActor(this.client, this.blank, this.x, this.o, xPos, yPos);
        }

    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}
