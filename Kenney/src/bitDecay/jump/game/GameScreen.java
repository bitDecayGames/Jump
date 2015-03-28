package bitDecay.jump.game;

import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.bitdecay.engine.animation.AnimationState;
import com.bitdecay.engine.utilities.ImageUtilities;

public class GameScreen implements Screen {

	SpriteBatch batch = new SpriteBatch();
	float elapsed = 0;
	private AnimationState animState;
	private List<String> states = Arrays.asList("WALK", "PICKUP", "FLOAT");

	@Override
	public void show() {
		animState = new AnimationState();
		Animation running = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "walk"));
		running.setPlayMode(PlayMode.LOOP);
		animState.addState("WALK", running);

		Animation pickup = new Animation(.5f, ImageUtilities.getImagesByPrefix("yellow", "pickup"));
		pickup.setPlayMode(PlayMode.LOOP);
		animState.addState("PICKUP", pickup);

		Animation floating = new Animation(.5f, ImageUtilities.getImagesByPrefix("yellow", "float"));
		floating.setPlayMode(PlayMode.LOOP);
		animState.addState("FLOAT", floating);

		newState();
	}

	@Override
	public void render(float delta) {
		elapsed += delta;
		if (elapsed > 2) {
			newState();
		}
		Gdx.gl.glClearColor(0.0f, 0.8f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(animState.getNextKeyFrame(delta), 100, 100);
		batch.end();
	}

	private void newState() {
		elapsed = 0;
		animState.setState(states.get((int) (Math.random() * states.size())));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
