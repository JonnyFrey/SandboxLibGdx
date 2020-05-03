package com.waffelmonster.compat;

import com.badlogic.gdx.utils.Timer;

public class JTask extends Timer.Task {

    private final Runnable runnable;

    public JTask(final Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        this.runnable.run();
    }
}