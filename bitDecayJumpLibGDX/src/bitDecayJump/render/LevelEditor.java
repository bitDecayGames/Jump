package bitDecayJump.render;

import java.io.*;

import javax.swing.*;

import bitDecayJump.geom.*;
import bitDecayJump.level.*;
import bitDecayJump.ui.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class LevelEditor extends InputAdapter implements Screen, OptionsUICallback {

	private static final int CAM_SPEED = 5;
	private static final int TILE_SIZE = 16;

	public SpriteBatch spriteBatch;

	public BitPointInt mouseDown;
	public BitPointInt mouseRelease;

	public LevelBuilder curLevelBuilder;
	public String savedLevel;

	OrthographicCamera camera;
	ShapeRenderer shaper;
	private MouseMode mouseMode;

	JDialog buttonsDialog = new JDialog();

	public LevelEditor() {

		spriteBatch = new SpriteBatch();
		shaper = new ShapeRenderer();

		camera = new OrthographicCamera(1600, 900);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		curLevelBuilder = new LevelBuilder(new Level(TILE_SIZE));

		mouseMode = new SelectMouseMode(curLevelBuilder);

		buttonsDialog.add(new OptionsUI(this));
		buttonsDialog.pack();
		buttonsDialog.setAlwaysOnTop(true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mouseRelease = GeomUtils.snap(getMouseCoords(), TILE_SIZE);

		handleInput();

		camera.update();
		spriteBatch.setProjectionMatrix(camera.combined);
		shaper.setProjectionMatrix(camera.combined);

		drawGrid();
		drawLevel(spriteBatch);
		drawOrigin();

		mouseMode.render(shaper);
	}

	private void drawLevel(SpriteBatch sb) {
		sb.begin();
		shaper.begin(ShapeType.Filled);
		shaper.setColor(Color.DARK_GRAY);
		for (LevelObject obj : curLevelBuilder.level.objects) {
			shaper.rect(obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
		}

		shaper.end();
		shaper.begin(ShapeType.Line);
		shaper.setColor(Color.GREEN);
		for (LevelObject obj : curLevelBuilder.selection) {
			shaper.rect(obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
		}

		shaper.end();
		sb.end();
	}

	private void drawGrid() {
		shaper.begin(ShapeType.Line);
		shaper.setColor(.1f, .1f, .1f, 1f);
		Vector3 topLeft = camera.unproject(new Vector3(-TILE_SIZE, -TILE_SIZE, 0));
		BitPointInt snapTopLeft = GeomUtils.snap(new BitPointInt((int) topLeft.x, (int) topLeft.y), TILE_SIZE);
		Vector3 bottomRight = camera.unproject(new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 2 * TILE_SIZE, 0));
		BitPointInt snapBottomRight = GeomUtils.snap(new BitPointInt((int) bottomRight.x, (int) bottomRight.y), TILE_SIZE);

		for (float x = snapTopLeft.x; x <= snapBottomRight.x; x += TILE_SIZE) {
			shaper.line(x, snapTopLeft.y, x, snapBottomRight.y);
		}
		for (float y = snapBottomRight.y; y <= snapTopLeft.y; y += TILE_SIZE) {
			shaper.line(snapTopLeft.x, y, snapBottomRight.x, y);
		}
		shaper.end();
	}

	private void drawOrigin() {
		shaper.setColor(Color.MAROON);
		shaper.begin(ShapeType.Filled);
		shaper.circle(0, 0, camera.zoom * (TILE_SIZE / 3));
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
			if (camera.zoom > 1) {
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
		// mouseDown = GeomUtils.snap(getMouseCoords(), TILE_SIZE);
		mouseMode.mouseDown(getMouseCoords());
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// mouseRelease = GeomUtils.snap(getMouseCoords(), TILE_SIZE);
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
		System.out.println(mode);
		if ("SELECT".equalsIgnoreCase(mode)) {
			mouseMode = new SelectMouseMode(curLevelBuilder);
		} else if ("CREATE".equalsIgnoreCase(mode)) {
			mouseMode = new CreateMouseMode(curLevelBuilder);
		} else if ("DELETE".equalsIgnoreCase(mode)) {
			mouseMode = new DeleteMouseMode(curLevelBuilder);
		} else if ("SAVE".equalsIgnoreCase(mode)) {
			saveLevel();
		} else if ("LOAD".equalsIgnoreCase(mode)) {
			loadLevel();
		}
	}

	private void saveLevel() {
		savedLevel = curLevelBuilder.toJson();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
				writer.write(savedLevel);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			curLevelBuilder = new LevelBuilder(savedLevel);
		}
	}

	private void loadLevel() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		// fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
				StringBuffer json = new StringBuffer();
				String line = reader.readLine();
				while (line != null) {
					json.append(line);
					line = reader.readLine();
				}
				if (json.length() > 0) {
					curLevelBuilder = new LevelBuilder(json.toString());
				} else {
					System.out.println("File was empty. Could not load.");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
