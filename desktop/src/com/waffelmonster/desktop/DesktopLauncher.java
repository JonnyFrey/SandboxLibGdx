package com.waffelmonster.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.waffelmonster.SandboxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Sandbox";
		config.width = 1080;
		config.height = 810;
		LwjglApplication app = new LwjglApplication(new SandboxGame(), config);
	}
}
