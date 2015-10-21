package com.bitdecay.jump.leveleditor.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
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
import com.bitdecay.jump.state.JumperStateWatcher;

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

    public ExampleEditorLevel() {
        world.setGravity(0, -900);
        sampleTiles = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/fallbacktileset.png"))).split(16, 16)[0];
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
        drawLevelEdit();
        for (GameObject object : gameObjects) {
            object.render(batch);
        }
        batch.end();
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
                    batch.draw(sampleTiles[obj.nValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
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
                GameObject newObject;
                newObject = (GameObject) builderMap.get(levelObject.getClass()).newInstance();
                bodies.add(newObject.build(levelObject));
                gameObjects.add(newObject);
            }
            return bodies;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Normally you would build the level here -- create objects and add them to your game lists and the world, etc.
    private void loadLevel(Level level) {
        currentLevel = level;
        world.setTileSize(16);
        world.setGridOffset(level.gridOffset);
        world.setGrid(level.gridObjects);
        world.setTileSize(level.tileSize);
        world.setObjects(buildBodies(level.otherObjects));
        world.resetTimePassed();

        if (level.spawn != null) {
            BitBody playerBody = new JumperBody();
            playerBody.bodyType = BodyType.DYNAMIC;
            playerController = new PlayerInputHandler();
            playerBody.aabb = new BitRectangle(level.spawn.x,level.spawn.y,16,32);
            playerBody.stateWatcher = new JumperStateWatcher();
            playerController.setBody(playerBody, ControlMap.defaultMapping);
            world.addBody(playerBody);
        }
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
