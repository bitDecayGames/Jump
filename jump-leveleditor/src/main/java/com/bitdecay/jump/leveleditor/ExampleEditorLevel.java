package com.bitdecay.jump.leveleditor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.builder.LevelBuilderListener;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.level.builder.TileObject;
import com.bitdecay.jump.leveleditor.input.ControlMap;
import com.bitdecay.jump.leveleditor.input.PlayerInputHandler;
import com.bitdecay.jump.leveleditor.render.LibGDXWorldRenderer;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.state.JumperStateWatcher;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Monday on 10/18/2015.
 */
public class ExampleEditorLevel implements EditorHook {
    private BitWorld world = new BitWorld();
    LibGDXWorldRenderer renderer = new LibGDXWorldRenderer(world, null);

    ShapeRenderer shaper = new ShapeRenderer();

    Level currentLevel;
    PlayerInputHandler playerController = new PlayerInputHandler();

    public ExampleEditorLevel() {
        world.setGravity(0, -900);
    }

    @Override
    public void update(float delta) {
        playerController.update();
        world.step(delta);
    }

    @Override
    public void render(OrthographicCamera cam) {
        // currently has no custom rendering
        renderer.cam = cam;
        renderer.render();

        if (currentLevel != null && currentLevel.spawn != null) {
            shaper.setProjectionMatrix(cam.combined);
            shaper.begin(ShapeRenderer.ShapeType.Filled);
            shaper.setColor(BitColors.SPAWN_OUTER);
            shaper.circle(currentLevel.spawn.x, currentLevel.spawn.y, 7);
            shaper.setColor(BitColors.SPAWN);
            shaper.circle(currentLevel.spawn.x, currentLevel.spawn.y, 4);
            shaper.end();
        }
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
}
