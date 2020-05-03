package com.waffelmonster.assets;

import com.badlogic.gdx.Screen;
import com.waffelmonster.screen.ConnectScreen;
import com.waffelmonster.screen.RoomScreen;

public enum Screens {

    CONNECT(ConnectScreen.class),
    TIC_TAC_TOE(RoomScreen.class);

    final Class<? extends Screen> screenClass;

    Screens(Class<? extends Screen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }
}
