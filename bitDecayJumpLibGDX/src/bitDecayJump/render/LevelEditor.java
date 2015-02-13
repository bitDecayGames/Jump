package bitDecayJump.render;

import java.io.*;
import java.util.*;

import javax.swing.*;

import bitDecayJump.*;
import bitDecayJump.geom.*;
import bitDecayJump.level.*;
import bitDecayJump.ui.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class LevelEditor extends InputAdapter implements Screen, OptionsUICallback {

	private static final int CAM_SPEED = 5;

	public SpriteBatch spriteBatch;
	public SpriteBatch uiBatch;
	public BitmapFont font;

	public BitPointInt mouseDown;
	public BitPointInt mouseRelease;

	public LevelBuilder curLevelBuilder;
	public String savedLevel;

	OrthographicCamera camera;
	ShapeRenderer shaper;
	private Map<String, MouseMode> mouseModes;
	private MouseMode mouseMode;

	JDialog buttonsDialog = new JDialog();

	private TextureRegion[] tiles;
	private TextureRegion fullSet;

	private BitWorld world;
	private LibGDXWorldRenderer worldRenderer;

	private LevelBuilderListener levelListener = new LevelBuilderListener() {
		Map<LevelObject, BitBody> objToBody = new HashMap<LevelObject, BitBody>();

		@Override
		public void levelChanged(Level level) {
			world.setLevel(level);
		}
	};

	public LevelEditor() {

		spriteBatch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		shaper = new ShapeRenderer();

		font = new BitmapFont(Gdx.files.internal("fonts/test2.fnt"), Gdx.files.internal("fonts/test2.png"), false);
		font.setColor(Color.YELLOW);

		camera = new OrthographicCamera(1600, 900);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		setLevelBuilder(new Level(32));

		world = new BitWorld();
		world.setGravity(0, -600);
		world.setLevel(new Level(curLevelBuilder.level.tileSize));

		worldRenderer = new LibGDXWorldRenderer(world, camera);

		mouseModes = new HashMap<String, MouseMode>();
		mouseModes.put("SELECT", new SelectMouseMode(curLevelBuilder));
		mouseModes.put("CREATE", new CreateMouseMode(curLevelBuilder));
		mouseModes.put("DELETE", new DeleteMouseMode(curLevelBuilder));
		mouseModes.put("SET PLAYER", new SetPlayerMouseMode(curLevelBuilder, world));
		mouseMode = mouseModes.get("SELECT");

		buttonsDialog.add(new OptionsUI(this));
		buttonsDialog.pack();
		buttonsDialog.setAlwaysOnTop(true);

		fullSet = new TextureRegion(new Texture(Gdx.files.internal("tileset.png")));
		tiles = fullSet.split(16, 16)[0];
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mouseRelease = GeomUtils.snap(getMouseCoords(), curLevelBuilder.level.tileSize);

		handleInput();

		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		shaper.setProjectionMatrix(camera.combined);

		drawGrid();
		world.step(delta);
		worldRenderer.render();
		drawLevelEdit(spriteBatch);
		drawOrigin();

		mouseMode.render(shaper);

		renderMouseCoords();
	}

	private void renderMouseCoords() {
		uiBatch.begin();
		font.draw(uiBatch, getMouseCoords().toString(), 20, 20);
		uiBatch.end();
	}

	private void drawLevelEdit(SpriteBatch sb) {
		sb.begin();
		for (LevelObject obj : curLevelBuilder.objects) {
			sb.draw(tiles[obj.nValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
		}
		sb.end();

		//		shaper.begin(ShapeType.Filled);
		//		shaper.setColor(Color.DARK_GRAY);
		//		for (LevelObject obj : curLevelBuilder.objects) {
		//			shaper.rect(obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
		//		}
		//		shaper.end();
		shaper.begin(ShapeType.Line);
		shaper.setColor(Color.GREEN);
		for (LevelObject obj : curLevelBuilder.selection) {
			shaper.rect(obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
		}

		shaper.end();
	}

	private void drawGrid() {
		shaper.begin(ShapeType.Line);
		shaper.setColor(.1f, .1f, .1f, 1f);
		Vector3 topLeft = camera.unproject(new Vector3(-curLevelBuilder.level.tileSize, -curLevelBuilder.level.tileSize, 0));
		BitPointInt snapTopLeft = GeomUtils.snap(new BitPointInt((int) topLeft.x, (int) topLeft.y), curLevelBuilder.level.tileSize);
		Vector3 bottomRight = camera.unproject(new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 2 * curLevelBuilder.level.tileSize, 0));
		BitPointInt snapBottomRight = GeomUtils.snap(new BitPointInt((int) bottomRight.x, (int) bottomRight.y), curLevelBuilder.level.tileSize);

		for (float x = snapTopLeft.x; x <= snapBottomRight.x; x += curLevelBuilder.level.tileSize) {
			shaper.line(x, snapTopLeft.y, x, snapBottomRight.y);
		}
		for (float y = snapBottomRight.y; y <= snapTopLeft.y; y += curLevelBuilder.level.tileSize) {
			shaper.line(snapTopLeft.x, y, snapBottomRight.x, y);
		}
		shaper.end();
	}

	private void drawOrigin() {
		shaper.setColor(Color.MAROON);
		shaper.begin(ShapeType.Filled);
		shaper.circle(0, 0, camera.zoom * (curLevelBuilder.level.tileSize / 3));
		shaper.end();
	}

	public BitPointInt unproject(int x, int y) {
		Vector3 unproj = camera.unproject(new Vector3(x, y, 0));
		return new BitPointInt((int) unproj.x, (int) unproj.y);
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			camera.translate(-CAM_SPEED * camera.zoom, 0);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			camera.translate(CAM_SPEED * camera.zoom, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			camera.translate(0, CAM_SPEED * camera.zoom);
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			camera.translate(0, -CAM_SPEED * camera.zoom);
		}

		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			if (camera.zoom > .3) {
				camera.zoom -= .032f;
			}
		} else if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			if (camera.zoom < 5) {
				camera.zoom += .032f;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.DEL) || Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
			curLevelBuilder.deleteSelected();
		}

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			if (!buttonsDialog.isShowing()) {
				buttonsDialog.setLocationRelativeTo(null);
				buttonsDialog.setVisible(true);
			}
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// log.debug("track click: " + mouseDown);
		// mouseDown = GeomUtils.snap(getMouseCoords(), curLevelBuilder.level.tileSize);
		mouseMode.mouseDown(getMouseCoords());
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// mouseRelease = GeomUtils.snap(getMouseCoords(), curLevelBuilder.level.tileSize);
		mouseMode.mouseUp(getMouseCoords());
		// createObject();
		// mouseDown = null;
		// mouseRelease = null;
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseMode.mouseDragged(getMouseCoords());
		return super.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouseMode.mouseMoved(getMouseCoords());
		return super.mouseMoved(screenX, screenY);
	}

	private BitPointInt getMouseCoords() {
		return unproject(Gdx.input.getX(), Gdx.input.getY());
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void setMode(String mode) {
		if (mouseModes.containsKey(mode.toUpperCase())) {
			mouseMode = mouseModes.get(mode.toUpperCase());
		} else if ("SAVE".equalsIgnoreCase(mode)) {
			saveLevel();
		} else if ("LOAD".equalsIgnoreCase(mode)) {
			Level loadLevel = LevelUtilities.loadLevel();
			if (loadLevel != null) {
				setLevelBuilder(loadLevel);
			}
		}
	}

	private void saveLevel() {
		savedLevel = curLevelBuilder.getJson();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save As");
		fileChooser.setCurrentDirectory(new File("."));
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
				writer.write(savedLevel);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLevelBuilder(LevelUtilities.loadLevel(savedLevel));
		}
	}

	private void setLevelBuilder(Level level) {
		if (curLevelBuilder == null) {
			curLevelBuilder = new LevelBuilder(level);
			curLevelBuilder.addListener(levelListener);
		} else {
			curLevelBuilder.setLevel(level);
		}
	}
}
