package bitDecay.jump.game;

import bitDecay.run.TestApp;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitdecay.desktop.FileUtilities;
import com.bitdecay.engine.utilities.DevConstants;
import com.bitdecay.engine.utilities.DevConstants.Constants;

public class Launcher {

	public static void main(String[] args) {
		DevConstants.setDevConstants(new Constants("", "repo/images"));
		FileUtilities.generateImageAtlases();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new Kenney(), config);
	}

}
