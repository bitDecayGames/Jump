package com.bitdecay.jump.leveleditor;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.bitdecay.jump.state.JumperStateWatcher;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Monday on 10/18/2015.
 */
public class EditorLevel implements EditorHook {
    private BitWorld world = new BitWorld();
    LibGDXWorldRenderer renderer = new LibGDXWorldRenderer(world, null);

    public EditorLevel() {
        world.setGravity(0, -900);
        world.setTileSize(16);
    }

    @Override
    public void update(float delta) {
        world.step(delta);
    }

    @Override
    public void render(OrthographicCamera cam) {
        // currently has no custom rendering
        renderer.cam = cam;
        renderer.render();
    }

    @Override
    public void levelChanged(Level level) {
        loadLevel(level);
    }

    @Override
    public void updateGrid(BitPointInt gridOffset, TileObject[][] grid, Collection<LevelObject> otherObjects, int tileSize) {
        loadLevel(gridOffset, grid, otherObjects, tileSize);
    }

    private Collection<BitBody> buildBodies(Collection<LevelObject> otherObjects) {
        ArrayList<BitBody> bodies = new ArrayList<>();
        for (LevelObject levelObject : otherObjects) {
            bodies.add(levelObject.buildBody());
        }
        return bodies;
    }

    private void loadLevel(Level level) {
        loadLevel(level.gridOffset, level.gridObjects, level.otherObjects, level.tileSize);
    }

    private void loadLevel(BitPointInt gridOffset, TileObject[][] grid, Collection<LevelObject> otherObjects, int tileSize) {
        world.setGridOffset(gridOffset);
        world.setGrid(grid);
        world.setTileSize(tileSize);
        world.setObjects(buildBodies(otherObjects));
        world.resetTimePassed();

        BitBody playerBody = new JumperBody();
        playerBody.bodyType = BodyType.DYNAMIC;
        PlayerInputHandler playerController = new PlayerInputHandler();
        playerBody.aabb = new BitRectangle(0,0,16,32);
        playerBody.stateWatcher = new JumperStateWatcher();
        playerController.setBody(playerBody, ControlMap.defaultMapping);
        world.addBody(playerBody);
    }
}
