package com.waffelmonster.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TicTacToeBoxActor extends Image {

    private final Drawable blankDraw;
    private final Drawable xDraw;
    private final Drawable oDraw;
    private final int xPos;
    private final int yPos;
    private int type;

    public TicTacToeBoxActor(final Drawable blankDraw, final Drawable xDraw, final Drawable oDraw, int xPos, int yPos) {
        this.blankDraw = blankDraw;
        this.xDraw = xDraw;
        this.oDraw = oDraw;

        this.setDrawable(blankDraw);

        this.xPos = xPos;
        this.yPos = yPos;

        this.type = 0;

        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(super.touchDown(event, x, y, pointer, button)) {
                    System.out.println("Clicked " + xPos + " " + yPos);
                    type = (type + 1) % 3;
                    switch (type) {
                        case 0:
                            setDrawable(blankDraw);
                            break;
                        case 1:
                            setDrawable(xDraw);
                            break;
                        case 2:
                            setDrawable(oDraw);
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public static class BoxBuilder {

        private final Drawable blank;
        private final Drawable x;
        private final Drawable o;

        public BoxBuilder(Drawable blank, Drawable x, Drawable o) {
            this.blank = blank;
            this.x = x;
            this.o = o;
        }

        public TicTacToeBoxActor create(int xPos, int yPos) {
            return new TicTacToeBoxActor(this.blank, this.x, this.o, xPos, yPos);
        }

    }

}
