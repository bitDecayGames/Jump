package com.bitDecay.game;

import java.util.*;

import bitDecayJump.*;
import bitDecayJump.level.*;
import bitDecayJump.render.LibGDXWorldRenderer;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class LevelTester implements Screen {
	private static final int CAM_SPEED = 5;

	/**
	 * a slightly hacky flag to drop the large delta's that come in as a result
	 * of pausing/switching screens
	 */
	private boolean resumed = true;

	private OrthographicCamera cam;
	private SpriteBatch batch;
	private LibGDXWorldRenderer worldRenderer;
	private Level level;

	private List<BitBody> bodies;

	private TextureRegion[] tiles;
	private TextureRegion fullSet;
	private BitWorld world;

	private BitBodyProps props;

	public LevelTester() {
		this.level = LevelUtilities.loadLevel("./test/test.level");
		world = new BitWorld();
		bodies = new ArrayList<BitBody>();

		props = new BitBodyProps();
		props.bodyType = BodyType.DYNAMIC;
		//		props.gravity = false;
		//		test1();
		main(level, props);
		world.setLevel(level);

		cam = new OrthographicCamera(1600, 900);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		batch = new SpriteBatch();

		worldRenderer = new LibGDXWorldRenderer(world, cam);

		fullSet = new TextureRegion(new Texture(Gdx.files.internal("tileset.png")));
		tiles = fullSet.split(16, 16)[0];
	}

	private void test1() {
		BitBody ascendingBody = world.createBody(30, -37, 20, 51, props);
		ascendingBody.velocity.y = 5;
	}

	private void main(Level level, BitBodyProps props) {
		//		bodies.add(world.createBody(124, 382, 7, 7, props));
		//		bodies.add(world.createBody(140, 380, 25, 25, props));
		//		bodies.add(world.createBody(169, 406, 10, 10, props));
		bodies.add(world.createBody(121, 473, 31, 29, props));
		//		BitBody body = world.createBody(208, 461, 25, 29, props);
		//		bodies.add(body);
		//		body.velocity.x = 10;
		//		bodies.add(world.createBody(235, 455, 30, 29, props));
		//
		//		bodies.add(world.createBody(183, 390, 18, 47, props));
		//		bodies.add(world.createBody(265, 408, 58, 55, props));
		//
		//		// what to do in this case?
		//		bodies.add(world.createBody(245, 412, 58, 55, props));
		//
		//		bodies.add(world.createBody(122, 605, 10, 12, props));
		//				BitBody body = world.createBody(188, 601, 5, 5, props);
		//		BitBody body;
		//		body = world.createBody(185, 435, 9, 9, props);
		//		body = world.createBody(198, 435, 9, 9, props);
		//		body = world.createBody(467, 580, 9, 8, props);
		//		body.velocity.x = 1;
		//		bodies.add(body);

		//		body = world.createBody(30, 28, 39, 38, props);
		//		body.velocity.y = -1;

		//		body = world.createBody(114, 428, 9, 7, props);
		//		body.velocity.x = 1;
		//		bodies.add(body);
		//
		//		body = world.createBody(147, 435, 9, 9, props);
		//		body.velocity.y = 1;
		//		bodies.add(body);
		//
		//		body = world.createBody(273, 119, 8, 16, props);
		//		body.velocity.y = -10;
		//		bodies.add(body);
		//
		//		body = world.createBody(260, 392, 7, 31, props);
		//		body.velocity.x = -1;
		//		bodies.add(body);
		world.setGravity(0, -600);
	}

	@Override
	public void show() {
		resumed = true;
	}

	@Override
	public void render(float delta) {
		if (resumed) {
			resumed = false;
			return;
		}
		if (Gdx.input.isKeyPressed(Keys.NUMPAD_4)) {
			cam.translate(-CAM_SPEED * cam.zoom, 0);
		} else if (Gdx.input.isKeyPressed(Keys.NUMPAD_6)) {
			cam.translate(CAM_SPEED * cam.zoom, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.NUMPAD_8)) {
			cam.translate(0, CAM_SPEED * cam.zoom);
		} else if (Gdx.input.isKeyPressed(Keys.NUMPAD_2)) {
			cam.translate(0, -CAM_SPEED * cam.zoom);
		}

		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			if (cam.zoom > .05) {
				cam.zoom -= .032f;
			}
		} else if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			if (cam.zoom < 5) {
				cam.zoom += .032f;
			}
		}
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		drawLevel();
		worldRenderer.render();
		world.step(delta);
		if (Gdx.input.isKeyJustPressed(Keys.S)) {
		}

		if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			for (BitBody bitBody : bodies) {
				if (bitBody.grounded) {
					bitBody.velocity.y = 200;
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			for (BitBody bitBody : bodies) {
				bitBody.velocity.x = -60;
			}
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			for (BitBody bitBody : bodies) {
				bitBody.velocity.x = 60;
			}
		} else {
			for (BitBody bitBody : bodies) {
				bitBody.velocity.x = 0;
			}
		}
	}

	private void drawLevel() {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		for (TileObject obj : level.getObjects()) {
			batch.draw(tiles[obj.nValue], obj.rect.xy.x, obj.rect.xy.y);
		}
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
		resumed = true;
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
