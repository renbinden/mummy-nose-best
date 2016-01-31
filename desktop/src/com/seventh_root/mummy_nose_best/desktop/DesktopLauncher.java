package com.seventh_root.mummy_nose_best.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.seventh_root.mummy_nose_best.MummyNoseBest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1152;
		config.height = 640;
		new LwjglApplication(new MummyNoseBest(), config);
	}
}
