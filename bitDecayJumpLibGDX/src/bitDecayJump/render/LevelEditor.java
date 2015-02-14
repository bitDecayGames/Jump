package bitDecayJump.render;

import java.util.*;

import javax.swing.JDialog;

import bitDecayJump.*;
import bitDecayJump.geom.*;
import bitDecayJump.input.InputDistributer;
import bitDecayJump.level.*;
import bitDecayJump.render.mouse.*;
import bitDecayJump.ui.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class LevelEditor extends InputAdapter implements Screen, OptionsUICallback, PropModUICallback {

	private static final int CAM_SPEED = 5;

	public SpriteBatch spriteBatch;
	public SpriteBatch uiBatch;
	public BitmapFont font;

	public BitPointInt mouseDown;
	public BitPointInt mouseRelease;

	public LevelBuilder curLevelBuilder;
	public String savedLevel;

	private OrthographicCamera camera;
	private ShapeRenderer shaper;
	private Map<String, MouseMode> mouseModes;
	private MouseMode mouseMode;

	private JDialog buttonsDialog = new JDialog();
	private JDialog playerTweakDialog = new JDialog();
	private PropModUI propUI;

	private BitBodyProps playerProps;

	private TextureRegion[] tiles;
	private TextureRegion fullSet;

	private BitWorld world;
	private LibGDXWorldRenderer worldRenderer;

	private InputDistributer input;

	private LevelBuilderListener levelListener = new LevelBuilderListener() {
		@Override
		public void levelChanged(Level level) {
			world.setLevel(level);
			BitBody player = maybeGetPlayer();
			if (player != null) {
				world.addBody(player);
			}
		}
	};

	public LevelEditor() {
		input = new InputDistributer();
		input.addHandler(this);

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

		playerProps = new BitBodyProps();
		playerProps.bodyType = BodyType.DYNAMIC;

		mouseModes = new HashMap<String, MouseMode>();
		mouseModes.put("SELECT", new SelectMouseMode(curLevelBuilder));
		mouseModes.put("CREATE", new CreateMouseMode(curLevelBuilder));
		mouseModes.put("DELETE", new DeleteMouseMode(curLevelBuilder));
		mouseModes.put("SPAWN", new SpawnMouseMode(curLevelBuilder));

		PlayerController playerController = new PlayerController();
		input.addHandler(playerController);
		mouseModes.put("SET PLAYER", new SetPlayerMouseMode(curLevelBuilder, world, playerController, playerProps));
		mouseMode = mouseModes.get("SELECT");

		buttonsDialog.add(new OptionsUI(this));
		buttonsDialog.pack();
		buttonsDialog.setAlwaysOnTop(true);

		propUI = new PropModUI(this, playerProps);
		playerTweakDialog.add(propUI);
		playerTweakDialog.pack();
		playerTweakDialog.setAlwaysOnTop(true);

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

		shaper.begin(ShapeType.Line);
		shaper.setColor(Color.GREEN);
		for (LevelObject obj : curLevelBuilder.selection) {
			shaper.rect(obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
		}
		shaper.end();

		// TODO: Figure out how to better render these special aspects
		shaper.begin(ShapeType.Filled);
		BitPointInt spawn = curLevelBuilder.level.spawn;
		if (spawn != null) {
			shaper.setColor(Color.YELLOW);
			shaper.circle(spawn.x, spawn.y, 7);
			shaper.setColor(Color.RED);
			shaper.circle(spawn.x, spawn.y, 4);
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
			if (!playerTweakDialog.isShowing()) {
				playerTweakDialog.setLocationRelativeTo(null);
				playerTweakDialog.setVisible(true);
			}
		}

		//		BitBody player = maybeGetPlayer();
		//		if (player != null) {
		//			handlePlayerInput(player);
		//		}
	}

	//	// change this when we get better controls in place.
	//	private void handlePlayerInput(BitBody player) {
	//		if (Gdx.input.isKeyJustPressed(Keys.W)) {
	//			if (player.props.grounded) {
	//				player.velocity.y = 600;
	//			}
	//		}
	//		if (Gdx.input.isKeyPressed(Keys.A)) {
	//			player.velocity.x = -60;
	//		} else if (Gdx.input.isKeyPressed(Keys.D)) {
	//			player.velocity.x = 60;
	//		} else {
	//			player.velocity.x = 0;
	//		}
	//	}

	private BitBody maybeGetPlayer() {
		return ((SetPlayerMouseMode) mouseModes.get("SET PLAYER")).lastPlayer;
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
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void setMode(String mode) {
		if (mouseModes.containsKey(mode.toUpperCase())) {
			mouseMode = mouseModes.get(mode.toUpperCase());
		} else if ("SAVE PLAYER PROPS".equalsIgnoreCase(mode)) {
			saveProps();
		} else if ("LOAD PLAYER PROPS".equalsIgnoreCase(mode)) {
			loadProps();
		} else if ("SAVE".equalsIgnoreCase(mode)) {
			setLevelBuilder(LevelUtilities.saveLevel(curLevelBuilder));
		} else if ("LOAD".equalsIgnoreCase(mode)) {
			Level loadLevel = LevelUtilities.loadLevel();
			if (loadLevel != null) {
				setLevelBuilder(loadLevel);
			}
		}
	}

	private void saveProps() {
		BitBody player = maybeGetPlayer();
		if (player != null) {
			FileUtils.saveToFile(player.props);
		}
	}

	private void loadProps() {
		BitBody player = maybeGetPlayer();
		if (player != null) {
			player.props = FileUtils.loadFileAs(BitBodyProps.class);
			propUI.setProperties(player.props);
		}
	}

	private void setLevelBuilder(Level level) {
		if (level == null) {
			level = new Level(16);
		}
		if (curLevelBuilder == null) {
			curLevelBuilder = new LevelBuilder(level);
			curLevelBuilder.addListener(levelListener);
		} else {
			curLevelBuilder.setLevel(level);
		}
	}

	@Override
	public void propertyChanged(String prop, Object value) {
		System.out.println(prop + " = " + value);
		BitBody player = maybeGetPlayer();
		if (player != null) {
			try {
				player.props.set(prop, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
