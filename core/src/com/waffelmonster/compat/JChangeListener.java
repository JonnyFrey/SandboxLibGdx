package com.waffelmonster.compat;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.function.BiConsumer;

public class JChangeListener extends ChangeListener {

    private final BiConsumer<ChangeEvent, Actor> action;

    public JChangeListener(final BiConsumer<ChangeEvent, Actor> action) {
        this.action = action;
    }

    @Override
    public void changed(ChangeEvent event, Actor actor) {
        this.action.accept(event, actor);
    }
}
