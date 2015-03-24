package bitDecay.run;

import com.badlogic.gdx.backends.lwjgl.*;

public class Launcher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new TestApp(), config);
	}

}
