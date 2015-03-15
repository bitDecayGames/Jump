package bitDecay.run;

import bitDecayJump.render.LevelEditor;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class TestApp extends Game {

	@Override
	public void create() {
		setScreen(new LevelEditor());
	}

}
