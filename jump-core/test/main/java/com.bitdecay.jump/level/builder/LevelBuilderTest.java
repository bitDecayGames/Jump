package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.level.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
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
        assertEquals("Grid x should start at start_size", LevelBuilder.START_SIZE, test.layers.getLayer(0).length, 0);
        assertEquals("Grid y should start at start_size", LevelBuilder.START_SIZE, test.layers.getLayer(0)[0].length, 0);

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

        assertTrue("Block 1 created", test.layers.getLayer(0)[1][0] != null);
        assertTrue("Block 2 created", test.layers.getLayer(0)[1][1] != null);
        assertTrue("Block 3 created", test.layers.getLayer(0)[2][1] != null);

        assertFalse("Block 1 is not one-way", test.layers.getLayer(0)[1][0].oneway);
        assertFalse("Block 2 is not one-way", test.layers.getLayer(0)[1][1].oneway);
        assertTrue("Block 3 is one-way", test.layers.getLayer(0)[2][1].oneway);
    }

    @Test
    public void testCreateLevelObjectsOverride() {
        LevelBuilder test = new LevelBuilder(32);
        test.gridOffset = new BitPointInt(); // make our math easier

        // should result in 2 blocks created
        test.createLevelObject(new BitPointInt(32, 0), new BitPointInt(64, 64), false, 0);

        // should result in 1 block created
        test.createLevelObject(new BitPointInt(64, 32), new BitPointInt(96, 64), false, 0);

        assertTrue("Block 1 created", test.layers.getLayer(0)[1][0] != null);
        assertTrue("Block 2 created", test.layers.getLayer(0)[1][1] != null);
        assertTrue("Block 3 created", test.layers.getLayer(0)[2][1] != null);

        TileObject tile1 = test.layers.getLayer(0)[1][0];
        TileObject tile2 = test.layers.getLayer(0)[1][1];
        TileObject tile3 = test.layers.getLayer(0)[2][1];

        // should result in 2 blocks created
        test.createLevelObject(new BitPointInt(32, 0), new BitPointInt(64, 64), false, 0);

        // should result in 1 block created
        test.createLevelObject(new BitPointInt(64, 32), new BitPointInt(96, 64), false, 0);

        assertNotEquals("Block 1 overridden", tile1.uuid, test.layers.getLayer(0)[1][0].uuid);
        assertNotEquals("Block 2 overridden", tile2.uuid, test.layers.getLayer(0)[1][1].uuid);
        assertNotEquals("Block 3 overridden", tile3.uuid, test.layers.getLayer(0)[2][1].uuid);
    }

    @Test
    public void testAddTriggerDirectly() {
        LevelBuilder test = new LevelBuilder(32);

        TriggerObject triggerObj = new TriggerObject();

        test.addObjects(new HashSet<>(Arrays.asList(triggerObj)));
        assertTrue("Builder contains one trigger", test.triggers.size() == 1);
        assertEquals("Builder contains correct trigger", triggerObj, test.triggers.get(triggerObj.uuid));
    }

    @Test
    public void testAddDebugSpawnDirectly() {
        LevelBuilder test = new LevelBuilder(32);

        DebugSpawnObject debugSpawnObj = new DebugSpawnObject(new BitPointInt());

        test.addObjects(new HashSet<>(Arrays.asList(debugSpawnObj)));
        assertEquals("Builder contains correct trigger", debugSpawnObj, test.debugSpawn);
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
    public void testRemoveGridTiles() {
        LevelBuilder test = new LevelBuilder(32);
        test.gridOffset = new BitPointInt(); // make our math easier

        // should result in 2 blocks created
        test.createLevelObject(new BitPointInt(32, 0), new BitPointInt(64, 64), false, 0);

        // should result in 1 block created
        test.createLevelObject(new BitPointInt(64, 32), new BitPointInt(96, 64), false, 0);

        assertTrue("Block 1 created", test.layers.getLayer(0)[1][0] != null);
        assertTrue("Block 2 created", test.layers.getLayer(0)[1][1] != null);
        assertTrue("Block 3 created", test.layers.getLayer(0)[2][1] != null);

        TileObject tile1 = test.layers.getLayer(0)[1][0];
        TileObject tile2 = test.layers.getLayer(0)[1][1];
        TileObject tile3 = test.layers.getLayer(0)[2][1];

        test.removeObjects(new HashSet<>(Arrays.asList(tile1, tile2, tile3)));

        assertTrue("Block 1 removed", test.layers.getLayer(0)[1][0] == null);
        assertTrue("Block 2 removed", test.layers.getLayer(0)[1][1] == null);
        assertTrue("Block 3 removed", test.layers.getLayer(0)[2][1] == null);
    }

    @Test
    public void testRemoveObjects() {
        LevelBuilder test = new LevelBuilder(32);

        LevelObject obj = new LevelObject(new BitRectangle(10, 10, 10, 10)) {
            @Override
            public BitBody buildBody() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }
        };

        test.otherObjects.put(obj.uuid, obj);

        test.removeObjects(new HashSet<>(Arrays.asList(obj)));

        assertTrue("Object removed from builder", test.otherObjects.isEmpty());
    }

    @Test
    public void testRemoveTriggers() {
        LevelBuilder test = new LevelBuilder(32);

        LevelObject levelObject = new LevelObject(new BitRectangle(32, 32, 64, 64)) {
            @Override
            public BitBody buildBody() {
                return null;
            }

            @Override
            public String name() {
                return null;
            }
        };

        TriggerObject obj = new TriggerObject(levelObject, levelObject);

        test.triggers.put(obj.uuid, obj);

        test.removeObjects(new HashSet<>(Arrays.asList(obj)));

        assertTrue("Object removed from builder", test.triggers.isEmpty());
    }

    @Test
    public void testEnsureGridFits() {
        //TODO test this
    }

    @Test
    public void testUpdateNeighbors() {
        LevelBuilder builder = new LevelBuilder(10);

        builder.layers.addLayer(0, new TileObject[3][3]);

        // center
        builder.layers.getLayer(0)[1][1] = new TileObject(new BitRectangle(), false, 0);

        // sides
        builder.layers.getLayer(0)[1][0] = new TileObject(new BitRectangle(), false, 0);
        builder.layers.getLayer(0)[0][1] = new TileObject(new BitRectangle(), false, 0);
        builder.layers.getLayer(0)[2][1] = new TileObject(new BitRectangle(), false, 0);
        builder.layers.getLayer(0)[1][2] = new TileObject(new BitRectangle(), false, 0);

        assertTrue(builder.layers.getLayer(0)[1][0].collideNValue == 0);
        assertTrue(builder.layers.getLayer(0)[0][1].collideNValue == 0);
        assertTrue(builder.layers.getLayer(0)[2][1].collideNValue == 0);
        assertTrue(builder.layers.getLayer(0)[1][2].collideNValue == 0);

        builder.updateNeighbors(builder.layers.getLayer(0), 1, 1);

        assertTrue(builder.layers.getLayer(0)[1][0].collideNValue == Direction.UP);
        assertTrue(builder.layers.getLayer(0)[0][1].collideNValue == Direction.RIGHT);
        assertTrue(builder.layers.getLayer(0)[2][1].collideNValue == Direction.LEFT);
        assertTrue(builder.layers.getLayer(0)[1][2].collideNValue == Direction.DOWN);
    }

    @Test
    public void testUpdateOwnNeighborValues() {
        LevelBuilder builder = new LevelBuilder(10);

        builder.layers.addLayer(0, new TileObject[3][3]);

        // center
        builder.layers.getLayer(0)[1][1] = new TileObject(new BitRectangle(), false, 0);

        // sides
        builder.layers.getLayer(0)[1][0] = new TileObject(new BitRectangle(), false, 0); // bottom
        builder.layers.getLayer(0)[0][1] = new TileObject(new BitRectangle(), false, 0); // left
        builder.layers.getLayer(0)[2][1] = new TileObject(new BitRectangle(), false, 0); // right
        builder.layers.getLayer(0)[1][2] = new TileObject(new BitRectangle(), false, 0); // bottom

        assertTrue("Neighbor value is zero on new tile", builder.layers.getLayer(0)[1][1].collideNValue == 0);
        assertTrue("Neighbor render is zero on new tile", builder.layers.getLayer(0)[1][1].renderNValue == 0);

        builder.updateOwnNeighborValues(builder.layers.getLayer(0), 1, 1);

        assertTrue("All neighbors are set", builder.layers.getLayer(0)[1][1].collideNValue == Direction.ALL);
        assertTrue("All neighbors are set", builder.layers.getLayer(0)[1][1].renderNValue == Direction.ALL);

        builder.layers.getLayer(0)[1][0] = null; // bottom
        builder.layers.getLayer(0)[0][1] = null; // left

        builder.updateOwnNeighborValues(builder.layers.getLayer(0), 1, 1);

        assertTrue("Up and right set", builder.layers.getLayer(0)[1][1].collideNValue == (Direction.UP | Direction.RIGHT));
        assertTrue("Up and right set", builder.layers.getLayer(0)[1][1].renderNValue == (Direction.UP | Direction.RIGHT));
    }

    @Test
    public void testUpdateOwnNeighborValuesOneWay() {
        LevelBuilder builder = new LevelBuilder(10);

        builder.layers.addLayer(0, new TileObject[3][3]);

        // center
        builder.layers.getLayer(0)[1][1] = new TileObject(new BitRectangle(), false, 0);

        // sides
        builder.layers.getLayer(0)[1][0] = new TileObject(new BitRectangle(), true, 0); // bottom
        builder.layers.getLayer(0)[0][1] = new TileObject(new BitRectangle(), true, 0); // left
        builder.layers.getLayer(0)[2][1] = new TileObject(new BitRectangle(), true, 0); // right
        builder.layers.getLayer(0)[1][2] = new TileObject(new BitRectangle(), true, 0); // bottom

        assertTrue("Neighbor value is zero on new tile", builder.layers.getLayer(0)[1][1].collideNValue == 0);
        assertTrue("Neighbor render is zero on new tile", builder.layers.getLayer(0)[1][1].renderNValue == 0);

        builder.updateOwnNeighborValues(builder.layers.getLayer(0), 1, 1);

        assertTrue("All neighbors are set", builder.layers.getLayer(0)[1][1].collideNValue == 0);
        assertTrue("All neighbors are set", builder.layers.getLayer(0)[1][1].renderNValue == Direction.ALL);

        builder.layers.getLayer(0)[1][0] = null; // bottom
        builder.layers.getLayer(0)[0][1] = null; // left

        builder.updateOwnNeighborValues(builder.layers.getLayer(0), 1, 1);

        assertTrue("Up and right set", builder.layers.getLayer(0)[1][1].collideNValue == 0);
        assertTrue("Up and right set", builder.layers.getLayer(0)[1][1].renderNValue == (Direction.UP | Direction.RIGHT));
    }

    @Test
    public void testSelectObjectsTiles() {
        // TODO: Do this
    }

    @Test
    public void testAddListener() {
        LevelBuilder builder = new LevelBuilder(10);

        LevelBuilderListener listener = new LevelBuilderListener() {
            @Override
            public void levelChanged(Level level) {

            }
        };

        builder.addListener(listener);

        assertTrue("Builder should only have one listener", builder.listeners.size() == 1);
        assertEquals("Builder should be the right object", listener, builder.listeners.get(0));
    }

    @Test
    public void testRemoveListener() {
        LevelBuilder builder = new LevelBuilder(10);

        LevelBuilderListener listener = new LevelBuilderListener() {
            @Override
            public void levelChanged(Level level) {

            }
        };

        builder.listeners.add(listener);

        builder.removeListener(listener);

        assertTrue("Listener should be removed", builder.listeners.size() == 0);
    }

    @Test
    public void testSetDebugSpawn() {
        LevelBuilder builder = new LevelBuilder(10);

        DebugSpawnObject debugSpawnObject = new DebugSpawnObject(new BitPointInt());

        builder.setDebugSpawn(debugSpawnObject);

        assertEquals("Builder has correct spawn", debugSpawnObject, builder.debugSpawn);
    }

    @Test
    public void testSetTheme() {
        LevelBuilder builder = new LevelBuilder(10);

        builder.setTheme(5);

        assertTrue("Theme set correctly", builder.theme == 5);
    }
}