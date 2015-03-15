package com.bitDecay.game;

import bitDecayJump.*;
import bitDecayJump.render.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MainScreen implements Screen {
	SpriteBatch batch;
	Texture img;
	private BitWorld world;
	private OrthographicCamera cam;
	private BitWorldRenderer worldRenderer;

	@Override
	public void show() {
		batch = new SpriteBatch();
		img = new Texture(Gdx.files.internal("title.png"));

		cam = new OrthographicCamera(1600, 900);
		cam.lookAt(new Vector3(0, 0, 0));
		cam.update();

		world = new BitWorld();

		BitBodyProps props = new BitBodyProps();
		props.maxSpeedX = 10;
		props.maxSpeedY = 10;
		props.accelX = 3;
		props.accelY = 0;
		props.bodyType = BodyType.DYNAMIC;

		BitBody body = world.createBody(0, 0, 10, 10, props);
		body.velocity.x = 10;

		props.bodyType = BodyType.STATIC;

		// static bodies can't move.
		BitBody body2 = world.createBody(0, 20, 10, 10, props);
		// body.velocity.x = 10;

		worldRenderer = new LibGDXWorldRenderer(world, cam);
	}

	@Override
	public void render(float delta) {
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			((Game) Gdx.app.getApplicationListener()).setScreen(new LevelEditor());
		} else if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			((Game) Gdx.app.getApplicationListener()).setScreen(new LevelTester());
		}
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
