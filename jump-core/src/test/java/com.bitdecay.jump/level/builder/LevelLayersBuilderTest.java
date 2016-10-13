package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.level.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Monday on 9/21/2016.
 */
public class LevelLayersBuilderTest {

    @Test
    public void testCreate() {
        LevelLayersBuilder builder = new LevelLayersBuilder(32);
        assertNotNull(builder.activeLevel);
        assertNotNull(builder.actions);
        assertNotNull(builder.layerBuilders);
        assertNotNull(builder.listeners);
        assertNotNull(builder.selection);

        assertEquals(-1, builder.lastAction);
    }

    @Test
    public void testCreateWithLevel() {
        Level level = new Level(32);

        LevelLayersBuilder builder = new LevelLayersBuilder(level);
        assertEquals(level, builder.activeLevel);
        assertEquals(32, builder.getCellSize());

        assertNotNull(builder.actions);
        assertNotNull(builder.layerBuilders);
        assertNotNull(builder.listeners);
        assertNotNull(builder.selection);

        assertEquals(-1, builder.lastAction);
    }

    @Test
    public void testSetLevel() {
        LevelLayersBuilder builder = new LevelLayersBuilder(32);

        Level level = new Level(48);
        level.layers.addLayer(1, new SingleLayer(16));

        builder.setLevel(level);

        assertEquals(level, builder.activeLevel);
        assertEquals(48, builder.getLevel().tileSize);

        assertEquals(2, builder.layerBuilders.size());
        assertEquals(level.layers, builder.layerBuilders.get(0).parent);
    }

    @Test
    public void testSetActiveLayer() {
        LevelLayersBuilder builder = new LevelLayersBuilder(32);

        assertEquals(1, builder.layerBuilders.size());

        builder.setActiveLayer(1);

        assertEquals(2, builder.layerBuilders.size());
        assertNotNull(builder.layerBuilders.get(1));
    }

    @Test
    public void testCreateLevelObject() {
        LevelLayersBuilder builder = new LevelLayersBuilder(32);

        builder.createLevelObject(new BitPointInt(0, 0), new BitPointInt(32, 32), false, 5);

        //compensating for grid offset
        TileObject createdTile = builder.activeLevel.layers.getLayer(0).grid[5][5];
        assertNotNull(createdTile);
        assertEquals(5, createdTile.material);
        assertFalse(createdTile.oneway);
    }

    @Test
    public void testCreateKineticObject() {
        LevelLayersBuilder builder = new LevelLayersBuilder(32);

        List<PathPoint> path = Arrays.asList(new PathPoint(new BitPoint(0, 0), 32, 0), new PathPoint(new BitPoint(100, 100), 32, 0));
        builder.createKineticObject(new BitRectangle(0, 0, 32, 16), path, true);

        Map<String, LevelObject> objs = builder.activeLevel.layers.getLayer(0).otherObjects;
        assertEquals(1, objs.size());

        PathedLevelObject kineticObj = (PathedLevelObject) objs.values().iterator().next();
        assertEquals(2, kineticObj.pathPoints.size());
    }

    @Test
    public void testCreateSingleObject() {
        LevelLayersBuilder builder = new LevelLayersBuilder(32);

        MockLevelObject obj = new MockLevelObject(new BitRectangle(0, 0, 100, 100));
        obj.uuid = "test";
        builder.createObject(obj);

        Map<String, LevelObject> objs = builder.activeLevel.layers.getLayer(0).otherObjects;
        assertEquals(obj, objs.get("test"));
    }

    @Test
    public void testMinMaxXY() {
        LevelLayersBuilder builder = new LevelLayersBuilder(32);

        builder.createLevelObject(new BitPointInt(0, 0), new BitPointInt(32, 32), false, 5);

        builder.createLevelObject(new BitPointInt(128, 128), new BitPointInt(160, 160), false, 5);

        BitPointInt minXY = builder.getMinXY();
        assertEquals(0, minXY.x);
        assertEquals(0, minXY.y);

        BitPointInt maxXY = builder.getMaxXY();
        assertEquals(160, maxXY.x);
        assertEquals(160, maxXY.y);
    }
}
