package com.bitdecay.jump.leveleditor.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.level.builder.TileObject;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.input.ControlMap;
import com.bitdecay.jump.leveleditor.input.PlayerInputHandler;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.state.JumperStateWatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Monday on 10/18/2015.
 */
public class ExampleEditorLevel implements EditorHook {
    BitWorld world = new BitWorld();

    SpriteBatch batch = new SpriteBatch();
    TextureRegion[] sampleTiles;

    Level currentLevel;
    PlayerInputHandler playerController = new PlayerInputHandler();

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
        drawLevelEdit();
    }

    @Override
    public BitWorld getWorld() {
        return world;
    }

    private void drawLevelEdit() {
        batch.begin();
        batch.setColor(1, 1, 1, .3f);
        for (int x = 0; x < currentLevel.gridObjects.length; x++) {
            for (int y = 0; y < currentLevel.gridObjects[0].length; y++) {
                TileObject obj = currentLevel.gridObjects[x][y];
                if (obj != null) {
                    batch.draw(sampleTiles[obj.nValue], obj.rect.xy.x, obj.rect.xy.y, obj.rect.width, obj.rect.height);
                }
            }
        }
        batch.end();
    }

    @Override
    public void levelChanged(Level level) {
        loadLevel(level);
    }

    private Collection<BitBody> buildBodies(Collection<LevelObject> otherObjects) {
        ArrayList<BitBody> bodies = new ArrayList<>();
        for (LevelObject levelObject : otherObjects) {
            bodies.add(levelObject.buildBody());
        }
        return bodies;
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
    public List<LevelObject> getCustomObjects() {
        return Arrays.asList(new SecretThing(), new SecretThing(), new SecretThing(), new SecretThing(), new SecretThing());
    }
}
