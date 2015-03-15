package com.bitDecay.game.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import com.bitDecay.game.JumpTest;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new JumpTest(), config);
	}
}
