package com.waffelmonster.assets;

import com.badlogic.gdx.Screen;
import com.waffelmonster.screen.ConnectScreen;

import java.util.function.Supplier;

public enum Screens {

    CONNECT(ConnectScreen::new);

    private final Supplier<Screen> screenSupplier;

    Screens(final Supplier<Screen> screenSupplier) {
        this.screenSupplier = screenSupplier;
    }

    public Screen create() {
        return this.screenSupplier.get();
    }

}
