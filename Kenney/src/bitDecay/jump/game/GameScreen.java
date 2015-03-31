package bitDecay.jump.game;

import bitDecayJump.BitWorld;
import bitDecayJump.entity.Player;
import bitDecayJump.level.LevelUtilities;
import bitDecayJump.render.LibGDXWorldRenderer;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {

	BitWorld world;
	LibGDXWorldRenderer worldRenderer;

	OrthographicCamera camera;

	Player player;
	SpriteBatch batch = new SpriteBatch();

	@Override
	public void show() {
		world = new BitWorld();
		world.setLevel(LevelUtilities.loadLevel("levels/kenney.level"));
		world.setGravity(0, -900);

		camera = new OrthographicCamera(1600, 900);

		worldRenderer = new LibGDXWorldRenderer(world, camera);
		player = new Player(world);
	}

	@Override
	public void render(float delta) {
		if (!world.step(delta)) {
			return;
		}
		player.update(delta);

		Gdx.gl.glClearColor(0.0f, 0.8f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.x = player.playerBody.aabb.xy.x;
		camera.position.y = player.playerBody.aabb.xy.y;
		camera.update();

		worldRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		player.render(batch);
		batch.end();
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
