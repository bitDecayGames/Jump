package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.*;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.level.builder.LevelBuilderListener;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.level.builder.TileObject;
import com.bitdecay.jump.leveleditor.input.PlayerInputHandler;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.leveleditor.ui.menus.EditorMenus;
import com.bitdecay.jump.leveleditor.render.mouse.*;
import com.bitdecay.jump.leveleditor.setup.EditorToolbox;
import com.bitdecay.jump.leveleditor.ui.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LevelEditor extends InputAdapter implements Screen, OptionsUICallback, PropModUICallback {

    public static final String EDITOR_ASSETS_FOLDER = "editorAssets";
    private EditorMenus menus;

    private static final int CAM_SPEED = 5;

    public BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/test2.fnt"), Gdx.files.internal("fonts/test2.png"), false);

    private String jumpVersion = "Jump v" + BitWorld.VERSION;
    private String renderVersion = "Render v" + BitWorld.VERSION;
    private GlyphLayout jumpVersionGlyphLayout = new GlyphLayout(font, jumpVersion);
    private GlyphLayout renderVersionGlyphLayout = new GlyphLayout(font, renderVersion);
    private BitPoint jumpSize = new BitPoint(jumpVersionGlyphLayout.width, jumpVersionGlyphLayout.height);
    private BitPoint renderSize = new BitPoint(renderVersionGlyphLayout.width, renderVersionGlyphLayout.height);

    private static Map<String, BitPoint> extraStrings = new HashMap<>();

    public SpriteBatch spriteBatch;
    public SpriteBatch uiBatch;

    public LevelBuilder curLevelBuilder;

    private OrthographicCamera camera;
    private ShapeRenderer shaper;
    private Map<OptionsMode, MouseMode> mouseModes;
    private MouseMode mouseMode;

    private Map<Integer, JDialog> uiKeys;

    private JumperBody playerBody;

    private Map<String, TextureRegion[]> materialMap;
    private TextureRegion[] fallbackTiles;
    private TextureRegion fullSet;

    private BitWorld world;
    private LibGDXWorldRenderer worldRenderer;

    private LevelBuilderListener levelListener = new LevelBuilderListener() {

        @Override
        public void levelChanged(Level level) {
            loadLevel(level);
        }

        @Override
        public void updateGrid(BitPointInt gridOffset, TileObject[][] grid, Collection<LevelObject> otherObjects) {
            loadLevel(gridOffset, grid, otherObjects);
        }
    };

    private PlayerInputHandler playerController;

    private EditorToolbox toolBox = new EditorToolbox();

    // a flags to control whether we are moving the world forward or not
    private boolean stepWorld = true;
    private boolean singleStep = false;

    public LevelEditor() {
        setUpMenus();
        spriteBatch = new SpriteBatch();
        uiBatch = new SpriteBatch();
        shaper = new ShapeRenderer();

        font.setColor(BitColors.UI_TEXT);

        curLevelBuilder = new LevelBuilder(16);
        curLevelBuilder.addListener(levelListener);

        camera = new OrthographicCamera(1600, 900);
        setCamToOrigin();

        world = new BitWorld();
        world.setGravity(0, -900);

        world.setGridOffset(curLevelBuilder.gridOffset);
        world.setTileSize(curLevelBuilder.tileSize);
        world.setGrid(curLevelBuilder.grid);

        worldRenderer = new LibGDXWorldRenderer(world, camera);

        playerBody = new JumperBody();
        playerBody.bodyType = BodyType.DYNAMIC;

        mouseModes = new HashMap<>();
        mouseModes.put(OptionsMode.SELECT, new SelectMouseMode(curLevelBuilder));
        mouseModes.put(OptionsMode.CREATE, new CreateMouseMode(curLevelBuilder));
        mouseModes.put(OptionsMode.ONEWAY, new CreateOneWayMouseMode(curLevelBuilder));
        mouseModes.put(OptionsMode.STATIC, new CreateStaticMouseMode(curLevelBuilder, world));
        mouseModes.put(OptionsMode.MOVING_PLATFORM, new MovingPlatformMouseMode(curLevelBuilder));
        mouseModes.put(OptionsMode.DELETE, new DeleteMouseMode(curLevelBuilder));
        mouseModes.put(OptionsMode.SET_SPAWN, new SpawnMouseMode(curLevelBuilder));

        playerController = new PlayerInputHandler();
        mouseModes.put(OptionsMode.SET_TEST_PLAYER, new SetPlayerMouseMode(curLevelBuilder, world, playerController, playerBody));
        mouseMode = mouseModes.get(OptionsMode.SELECT);

        uiKeys = new HashMap<>();

        JDialog buttonsDialog = new OptionsUI(this, toolBox);
        buttonsDialog.setTitle("Tools");

        uiKeys.put(Keys.T, buttonsDialog);

        JDialog playerTweakDialog = new PropModUI(this, playerBody);
        playerTweakDialog.setTitle("Player Props");

        uiKeys.put(Keys.P, playerTweakDialog);

        materialMap = new HashMap<>();

        fullSet = new TextureRegion(new Texture(Gdx.files.internal(EDITOR_ASSETS_FOLDER + "/fallbacktileset.png")));

        fallbackTiles = fullSet.split(16, 16)[0];
    }

    private void setUpMenus() {
        menus = new EditorMenus(this);

        InputMultiplexer inputMux = new InputMultiplexer();
        inputMux.addProcessor(menus.getStage());
        inputMux.addProcessor(this);
        Gdx.input.setInputProcessor(inputMux);
    }

    private Collection<BitBody> buildBodies(Collection<LevelObject> otherObjects) {
        ArrayList<BitBody> bodies = new ArrayList<BitBody>();
        for (LevelObject levelObject : otherObjects) {
            bodies.add(levelObject.buildBody());
        }
        return bodies;
    }

    private void setCamToOrigin() {
        int tileSize = curLevelBuilder.tileSize;
        float width = curLevelBuilder.grid.length * tileSize;
        float height = curLevelBuilder.grid[0].length * tileSize;

        BitPoint center = new BitPoint(0, 0).plus(curLevelBuilder.gridOffset.x * tileSize, curLevelBuilder.gridOffset.y * tileSize);
        center.add(width / 2, height / 2);
        camera.position.set(center.x, center.y, 0);

        float widthZoom = width / Gdx.graphics.getWidth();
        float heightZoom = height / Gdx.graphics.getHeight();
        camera.zoom = Math.max(widthZoom, heightZoom) + .25f;
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        playerController.update();

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        shaper.setProjectionMatrix(camera.combined);

        drawGrid();
        debugRender();
        if (singleStep) {
            singleStep = false;
            world.step(1/120f);
        } else if (stepWorld) {
            world.step(delta);
        } else {
            world.nonStep(delta);
        }
        worldRenderer.render();
        drawLevelEdit();
        drawOrigin();

        spriteBatch.begin();
        shaper.begin(ShapeType.Line);
        mouseMode.render(shaper, spriteBatch);
        spriteBatch.end();
        shaper.end();

        uiBatch.begin();
        renderStrings();
        renderExtraUIHints();
        renderVersion();
        uiBatch.end();

        renderSpecial();

        menus.updateAndDraw();
    }

    private void renderSpecial() {
        shaper.setProjectionMatrix(uiBatch.getProjectionMatrix());
        shaper.setColor(BitColors.MISC);
        if (stepWorld) {
            shaper.begin(ShapeType.Line);
            shaper.polygon(new float[]{20, 20, 70, 45, 20, 70, 20, 20});
        } else {
            shaper.begin(ShapeType.Filled);
            shaper.rect(20, 20, 20, 50);
            shaper.rect(50, 20, 20, 50);
        }
        shaper.end();
    }

    private void debugRender() {
        shaper.begin(ShapeType.Line);
        shaper.setColor(BitColors.GRID_SIZE);
        shaper.rect(curLevelBuilder.gridOffset.x * curLevelBuilder.tileSize, curLevelBuilder.gridOffset.y * curLevelBuilder.tileSize,
                curLevelBuilder.grid.length * curLevelBuilder.tileSize, curLevelBuilder.grid[0].length * curLevelBuilder.tileSize);
        shaper.end();
    }

    private void renderStrings() {
        for (Map.Entry<String, BitPoint> entry : extraStrings.entrySet()) {
            Vector3 screenCoords = camera.project(new Vector3(entry.getValue().x, entry.getValue().y, 0));
            font.draw(uiBatch, entry.getKey(), screenCoords.x, screenCoords.y);
        }
        extraStrings.clear();
    }

    private void renderExtraUIHints() {
        font.draw(uiBatch, getMouseCoords().toString(), 20, 20);
        font.draw(uiBatch, String.format("World time: %.2f", world.getTimePassed()), 100, 20);
    }

    private void renderVersion() {
        font.draw(uiBatch, jumpVersion, Gdx.graphics.getWidth() - jumpSize.x, jumpSize.y);
        font.draw(uiBatch, renderVersion, Gdx.graphics.getWidth() - renderSize.x, jumpSize.y + renderSize.y);
    }

    /**
     * Queues a string to be rendered next frame
     * @param text
     * @param location
     */
    public static void addStringForRender(String text, BitPoint location) {
        extraStrings.put(text, location);
    }

    private void drawLevelEdit() {
        spriteBatch.begin();
        spriteBatch.setColor(1, 1, 1, .3f);
        for (int x = 0; x < curLevelBuilder.grid.length; x++) {
            for (int y = 0; y < curLevelBuilder.grid[0].length; y++) {
                TileObject obj = curLevelBuilder.grid[x][y];
                if (obj != null) {
                    spriteBatch.draw(getMaterial(obj.material)[obj.nValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
                }
            }
        }
        spriteBatch.end();

        shaper.begin(ShapeType.Line);
        shaper.setColor(BitColors.SELECTION);
        for (LevelObject obj : curLevelBuilder.selection) {
            shaper.rect(obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
        }
        shaper.end();
    }

    private TextureRegion[] getMaterial(int material) {
        //		String matLoc = curLevelBuilder.level.materials.get(material);
        //		if (matLoc != null && !matLoc.isEmpty()) {
        //			TextureRegion[] materialImages = materialMap.get(curLevelBuilder.level.baseMaterialDir + matLoc);
        //			if (materialImages != null) {
        //				return materialImages;
        //			}
        //		}
        return fallbackTiles;
    }

    private void drawGrid() {
        shaper.begin(ShapeType.Line);
        shaper.setColor(BitColors.GRID_LINES);
        Vector3 topLeft = camera.unproject(new Vector3(-curLevelBuilder.tileSize, -curLevelBuilder.tileSize, 0));
        BitPointInt snapTopLeft = GeomUtils.snap(new BitPointInt((int) topLeft.x, (int) topLeft.y), curLevelBuilder.tileSize);
        Vector3 bottomRight = camera.unproject(new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 2 * curLevelBuilder.tileSize, 0));
        BitPointInt snapBottomRight = GeomUtils.snap(new BitPointInt((int) bottomRight.x, (int) bottomRight.y), curLevelBuilder.tileSize);

        for (float x = snapTopLeft.x; x <= snapBottomRight.x; x += curLevelBuilder.tileSize) {
            shaper.line(x, snapTopLeft.y, x, snapBottomRight.y);
        }
        for (float y = snapBottomRight.y; y <= snapTopLeft.y; y += curLevelBuilder.tileSize) {
            shaper.line(snapTopLeft.x, y, snapBottomRight.x, y);
        }
        shaper.end();
    }

    private void drawOrigin() {
        shaper.setColor(BitColors.ORIGIN);
        shaper.begin(ShapeType.Filled);
        shaper.circle(0, 0, camera.zoom * (curLevelBuilder.tileSize / 3));
        shaper.end();
    }

    public BitPointInt unproject(int x, int y) {
        Vector3 unproj = camera.unproject(new Vector3(x, y, 0));
        return new BitPointInt((int) unproj.x, (int) unproj.y);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.GRAVE)) {
            stepWorld = !stepWorld;
        }
        if (!stepWorld && (Gdx.input.isKeyJustPressed(Keys.PLUS) || Gdx.input.isKeyJustPressed(Keys.EQUALS))) {
            singleStep = true;
        }

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
            if (camera.zoom > 5) {
                adjustCamZoom(-.2f);
            } else if (camera.zoom > .2) {
                adjustCamZoom(-.05f);
            }
        } else if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
            if (camera.zoom < 5) {
                adjustCamZoom(.05f);
            } else if (camera.zoom < 20) {
                adjustCamZoom(.2f);
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            loadLevel(curLevelBuilder.optimizeLevel());
        }

        if (Gdx.input.isKeyJustPressed(Keys.DEL) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
            curLevelBuilder.deleteSelected();
        }

        for (Integer uiKey : uiKeys.keySet()) {
            if (Gdx.input.isKeyPressed(uiKey)) {
                JDialog dialog = uiKeys.get(uiKey);
                if (!dialog.isShowing()) {
                    dialog.setVisible(true);
                }
            }
        }
    }

    private void adjustCamZoom(float change) {
        BitPointInt mouseCoordinates = getMouseCoords();
        camera.zoom += change;
        camera.update();
        mouseCoordinates = mouseCoordinates.minus(getMouseCoords());
        camera.translate(mouseCoordinates.x, mouseCoordinates.y);
    }

    private BitBody maybeGetPlayer() {
        return ((SetPlayerMouseMode) mouseModes.get(OptionsMode.SET_TEST_PLAYER)).lastPlayer;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseMode.mouseDown(getMouseCoords(), MouseButton.getButton(button));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mouseMode.mouseUp(getMouseCoords(), MouseButton.getButton(button));
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
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void setMode(OptionsMode mode) {
        if (mouseModes.containsKey(mode)) {
            mouseMode = mouseModes.get(mode);
        } else if (OptionsMode.UNDO.equals(mode)) {
            curLevelBuilder.undo();
        } else if (OptionsMode.REDO.equals(mode)) {
            curLevelBuilder.redo();
        } else if (OptionsMode.SET_MAT_DIR.equals(mode)) {
            // set base directory. Allow textures to be loaded from it.
        } else if (OptionsMode.SAVE_PLAYER.equals(mode)) {
            saveProps();
        } else if (OptionsMode.LOAD_PLAYER.equals(mode)) {
            loadProps();
        } else if (OptionsMode.SAVE_LEVEL.equals(mode)) {
            setLevelBuilder(LevelUtilities.saveLevel(curLevelBuilder));
        } else if (OptionsMode.LOAD_LEVEL.equals(mode)) {
            Level loadLevel = LevelUtilities.loadLevel();
            if (loadLevel != null) {
                setLevelBuilder(loadLevel);
                setCamToOrigin();
            }
        }
    }

    public void saveLevel() {
        LevelUtilities.saveLevel(curLevelBuilder);
    }

    public void loadLevel() {
        Level loadLevel = LevelUtilities.loadLevel();
        if (loadLevel != null) {
            setLevelBuilder(loadLevel);
            setCamToOrigin();
            stepWorld = false;
        }
    }

    private void loadLevel(Level level) {
        loadLevel(level.gridOffset, level.gridObjects, level.otherObjects);
    }

    private void loadLevel(BitPointInt gridOffset, TileObject[][] grid, Collection<LevelObject> otherObjects) {
        world.setGridOffset(gridOffset);
        world.setGrid(grid);
        world.setObjects(buildBodies(otherObjects));
        resetPlayer();
        world.resetTimePassed();
        stepWorld = false;
    }

    protected void resetPlayer() {
        BitBody player = maybeGetPlayer();
        if (player != null) {
            world.addBody(player);
        }
    }

    private void saveProps() {
        BitBody player = maybeGetPlayer();
        if (player != null) {
            FileUtils.saveToFile(player);
        }
    }

    private void loadProps() {
        BitBody player = maybeGetPlayer();
        if (player != null) {
            player = FileUtils.loadFileAs(JumperBody.class);
        }
    }

    private void setLevelBuilder(Level level) {
        if (level == null) {
            curLevelBuilder.newLevel(16);
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
                player.set(prop, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
