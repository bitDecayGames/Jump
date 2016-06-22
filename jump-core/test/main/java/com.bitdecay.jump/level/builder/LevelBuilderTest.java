package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.level.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Monday on 6/21/2016.
 */
public class LevelBuilderTest {

    @Test
    public void testConstruction() {
        LevelBuilder test = new LevelBuilder(32);
        assertEquals("Tile size should be 32", 32, test.tileSize, 0);
        assertEquals("Grid x should start at start_size", LevelBuilder.START_SIZE, test.grid.length, 0);
        assertEquals("Grid y should start at start_size", LevelBuilder.START_SIZE, test.grid[0].length, 0);

        assertEquals("Grid offset x should be centered in start grid", new BitPointInt(-(LevelBuilder.START_SIZE/2), -(LevelBuilder.START_SIZE/2)), test.gridOffset);
    }

    @Test
    public void testSetLevel() {
        LevelBuilder test = new LevelBuilder(100);

        LevelObject genericObject = new LevelObject() {
            @Override
            public BitBody buildBody() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }
        };

        TriggerObject genericTrigger = new TriggerObject();

        DebugSpawnObject debugSpawn = new DebugSpawnObject(new BitPointInt(0, 0));

        Level level = new Level(32);
        level.otherObjects.add(genericObject);
        level.triggers.add(genericTrigger);
        level.debugSpawn = debugSpawn;

        test.setLevel(level);

        assertEquals("Tile size should be overriden", 32, test.tileSize, 0);
        assertTrue("Builder should only have one object", test.otherObjects.size() == 1);
        assertEquals("Builder object should be correct object", genericObject, test.otherObjects.get(genericObject.uuid));

        assertTrue("Builder should only have one trigger", test.triggers.size() == 1);
        assertEquals("Builder object should be correct trigger", genericTrigger, test.triggers.get(genericTrigger.uuid));

        assertEquals("Builder debug spawn should be correct object", debugSpawn, test.debugSpawn);
    }

    @Test
    public void testLevelListeners() {
        LevelBuilder test = new LevelBuilder(32);

        final boolean[] listenerCalled = {false, false};
        LevelBuilderListener listenerOne = new LevelBuilderListener() {
            @Override
            public void levelChanged(Level level) {
                listenerCalled[0] = true;
            }
        };
        LevelBuilderListener listenerTwo = new LevelBuilderListener() {
            @Override
            public void levelChanged(Level level) {
                listenerCalled[1] = true;
            }
        };

        test.addListener(listenerOne);
        test.addListener(listenerTwo);

        Level level = new Level(32);
        test.setLevel(level);

        assertTrue("Listeners one should be called", listenerCalled[0]);
        assertTrue("Listeners two should be called", listenerCalled[1]);
    }

    @Test
    public void testCreateKineticObject() {
        LevelBuilder test = new LevelBuilder(32);

        BitRectangle bodyShape = new BitRectangle(10, 10, 10, 10);
        List<PathPoint> path = Arrays.asList(new PathPoint(new BitPoint(0, 0), 10, 1), new PathPoint(new BitPoint(10, 0), 10, 1));
        boolean pendulum = false;

        test.createKineticObject(bodyShape, path, pendulum);

        assertTrue("Only one kinetic object in builder", test.otherObjects.size() == 1);

        PathedLevelObject created = (PathedLevelObject) test.otherObjects.values().iterator().next();

        assertEquals("Created object has correct aabb", bodyShape, created.rect);
        assertTrue("Object has 2 path points", created.pathPoints.size() == 2);
        assertTrue("Object has correct pendulum", created.pendulum == pendulum);
    }

    @Test
    public void testCreateLevelObject() {
        LevelBuilder test = new LevelBuilder(32);
        test.gridOffset = new BitPointInt(); // make our math easier

        // should result in 2 blocks created
        test.createLevelObject(new BitPointInt(32, 0), new BitPointInt(64, 64), false, 0);

        // should result in 1 block created
        test.createLevelObject(new BitPointInt(64, 32), new BitPointInt(96, 64), true, 0);

        assertTrue("Block 1 created", test.grid[1][0] != null);
        assertTrue("Block 2 created", test.grid[1][1] != null);
        assertTrue("Block 3 created", test.grid[2][1] != null);

        assertFalse("Block 1 is not one-way", test.grid[1][0].oneway);
        assertFalse("Block 2 is not one-way", test.grid[1][1].oneway);
        assertTrue("Block 3 is one-way", test.grid[2][1].oneway);
    }

    @Test
    public void testCreateLevelObjectsOverride() {
        // TODO: Test that grid cells are overridden if new objects placed on them
    }

    @Test
    public void testAddObjectsDirectly() {
        // TODO: Test adding Trigger objects and DebugSpawn directly
    }

    @Test
    public void testCreateObject() {
        LevelBuilder test = new LevelBuilder(32);

        LevelObject obj = new LevelObject(new BitRectangle(32, 32, 64, 64)) {
            @Override
            public BitBody buildBody() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }
        };

        test.createObject(obj);

        assertTrue("Builder contains one object", test.otherObjects.size() == 1);
        assertEquals("Builder contains correct object", obj, test.otherObjects.get(obj.uuid));
    }

    @Test
    public void testEnsureGridFits() {
        //TODO test this
    }
}