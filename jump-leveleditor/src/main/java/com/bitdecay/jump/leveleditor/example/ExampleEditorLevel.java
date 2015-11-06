package com.bitdecay.jump.leveleditor.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.gdx.level.EditorIdentifierObject;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.level.builder.TileObject;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.example.game.GameObject;
import com.bitdecay.jump.leveleditor.example.game.SecretObject;
import com.bitdecay.jump.leveleditor.example.level.SecretThing;
import com.bitdecay.jump.leveleditor.input.ControlMap;
import com.bitdecay.jump.leveleditor.input.PlayerInputHandler;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.render.JumperRenderStateWatcher;

import java.util.*;

/**
 * Created by Monday on 10/18/2015.
 */
public class ExampleEditorLevel implements EditorHook {
    BitWorld world = new BitWorld();

    SpriteBatch batch = new SpriteBatch();
    TextureRegion[] sampleTiles;

    Level currentLevel;
    PlayerInputHandler playerController = new PlayerInputHandler();

    Map<Class, Class> builderMap = new HashMap<>();

    List<GameObject> gameObjects = new ArrayList<>();

    Map<Integer, TextureRegion[]> tilesetMap = new HashMap<>();

    public ExampleEditorLevel() {
        world.setGravity(0, -900);
        tilesetMap.put(0, new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/fallbacktileset.png"))).split(16, 16)[0]);
        tilesetMap.put(1, new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/templatetileset.png"))).split(16, 16)[0]);
    }

    @Override
    public void update(float delta) {
        playerController.update();
        world.step(delta);
    }

    @Override
    public void render(OrthographicCamera cam) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        drawBackground(cam);
        drawLevelEdit();
        for (GameObject object : gameObjects) {
            object.render(batch);
        }
        batch.end();
    }

    private void drawBackground(final OrthographicCamera cam) {
        // Crappy example background rendering
        if (currentLevel.background == -1) {
            return;
        }
        TextureRegion dirtThumb = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/dirtThumb.png")));
        TextureRegion iceThumb = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/iceThumb.png")));
        Vector3 zero = cam.unproject(new Vector3(0, Gdx.graphics.getHeight(), 0));
        TextureRegion background = currentLevel.background == 0 ? dirtThumb : iceThumb;
        batch.setColor(Color.DARK_GRAY);
        batch.draw(background, zero.x, zero.y, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
        batch.setColor(Color.WHITE);
    }

    @Override
    public BitWorld getWorld() {
        return world;
    }

    private void drawLevelEdit() {
        /**
         * TODO: we still need to find a better way to load a grid into the world but with custom tile objects.
         * It shouldn't be hard, but it does need to be done.
        **/
        for (int x = 0; x < currentLevel.gridObjects.length; x++) {
            for (int y = 0; y < currentLevel.gridObjects[0].length; y++) {
                TileObject obj = currentLevel.gridObjects[x][y];
                if (obj != null) {
                    batch.draw(tilesetMap.get(obj.material)[obj.renderNValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
                }
            }
        }
    }

    @Override
    public void levelChanged(Level level) {
        loadLevel(level);
    }

    private Collection<BitBody> buildBodies(Collection<LevelObject> otherObjects) {
        try {
            ArrayList<BitBody> bodies = new ArrayList<>();
            for (LevelObject levelObject : otherObjects) {
                if (builderMap.containsKey(levelObject.getClass())) {
                    GameObject newObject;
                    newObject = (GameObject) builderMap.get(levelObject.getClass()).newInstance();
                    bodies.add(newObject.build(levelObject));
                    gameObjects.add(newObject);
                } else {
                    bodies.add(levelObject.buildBody());
                }
            }
            return bodies;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Normally you would build the level here -- create objects and add them to your game lists and the world, etc.
    private void loadLevel(Level level) {
        gameObjects.clear();

        currentLevel = level;
        world.setTileSize(16);
        world.setGridOffset(level.gridOffset);
        world.setGrid(level.gridObjects);
        world.setTileSize(level.tileSize);
        world.setObjects(buildBodies(level.otherObjects));
        world.resetTimePassed();

        if (level.spawn != null) {
            BitBody playerBody = new BitBody();
            playerBody.props = level.spawn.props;

            playerBody.bodyType = BodyType.DYNAMIC;
            playerController = new PlayerInputHandler();
            playerBody.aabb = new BitRectangle(level.spawn.rect.xy.x,level.spawn.rect.xy.y,16,32);
            playerBody.renderStateWatcher = new JumperRenderStateWatcher();
            playerController.setBody(playerBody, ControlMap.defaultMapping);
            world.addBody(playerBody);
        }
    }

    @Override
    public List<EditorIdentifierObject> getTilesets() {
        TextureRegion dirtThumb = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/dirtThumb.png")));
        TextureRegion iceThumb = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/iceThumb.png")));
        return Arrays.asList(new EditorIdentifierObject(0, "Dirt", dirtThumb), new EditorIdentifierObject(1, "Ice", iceThumb));
    }

    @Override
    public List<EditorIdentifierObject> getThemes() {
        TextureRegion noneThumb = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/noneThumb.png")));
        TextureRegion dirtThumb = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/dirtThumb.png")));
        TextureRegion iceThumb = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/iceThumb.png")));
        return Arrays.asList(new EditorIdentifierObject(-1, "None", noneThumb), new EditorIdentifierObject(0, "Surface Background", dirtThumb), new EditorIdentifierObject(1, "Cavern Background", iceThumb));
    }

    @Override
    public List<RenderableLevelObject> getCustomObjects() {
        builderMap.put(SecretThing.class, SecretObject.class);
        List<RenderableLevelObject> exampleItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            exampleItems.add(new SecretThing());
        }
        return exampleItems;
    }
}
