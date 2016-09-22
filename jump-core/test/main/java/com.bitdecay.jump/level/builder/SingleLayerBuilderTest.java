package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Monday on 9/19/2016.
 */
public class SingleLayerBuilderTest {

    @Test
    public void testCreate() {
        LevelLayers layers = new LevelLayers(10);

        SingleLayerBuilder builder = new SingleLayerBuilder(layers, 0);

        assertEquals(layers, builder.parent);
        assertEquals(0, builder.editingLayer);
    }

    @Test
    public void testAddRemoveOtherObject() {
        LevelLayers layers = new LevelLayers(10);

        SingleLayerBuilder builder = new SingleLayerBuilder(layers, 0);

        Set<LevelObject> myObjectSet = new HashSet<>();
        MockLevelObject tile = new MockLevelObject(new BitRectangle(10, 10, 10, 10));
        tile.uuid = "test";
        myObjectSet.add(tile);

        builder.addObjects(myObjectSet);

        assertEquals(tile, layers.getLayer(0).otherObjects.get("test"));

        builder.removeObjects(myObjectSet);

        assertNull(layers.getLayer(0).otherObjects.get("test"));
    }

    @Test
    public void testAddRemoveTileObject() {
        LevelLayers layers = new LevelLayers(10);
        layers.gridOffset.set(0, 0);

        SingleLayerBuilder builder = new SingleLayerBuilder(layers, 0);

        Set<LevelObject> myObjectSet = new HashSet<>();
        TileObject tile = new TileObject(new BitRectangle(0, 0, layers.cellSize, layers.cellSize), false, 0);
        tile.uuid = "test";
        myObjectSet.add(tile);

        builder.addObjects(myObjectSet);

        assertEquals(tile, layers.getLayer(0).grid[0][0]);

        builder.removeObjects(myObjectSet);

        assertNull(layers.getLayer(0).grid[0][0]);
    }

    @Test
    public void testAddRemoveTriggerObject() {
        LevelLayers layers = new LevelLayers(10);
        layers.gridOffset.set(0, 0);

        SingleLayerBuilder builder = new SingleLayerBuilder(layers, 0);
        Set<LevelObject> myObjectSet = new HashSet<>();

        MockLevelObject obj1 = new MockLevelObject(new BitRectangle(0, 0, 10, 10));
        MockLevelObject obj2 = new MockLevelObject(new BitRectangle(20, 0, 10, 10));
        myObjectSet.add(obj1);
        myObjectSet.add(obj2);


        TriggerObject trigger = new TriggerObject(obj1, obj2);
        trigger.uuid = "test";
        myObjectSet.add(trigger);

        builder.addObjects(myObjectSet);

        assertEquals(trigger, layers.getLayer(0).triggers.get("test"));

        myObjectSet.clear();
        myObjectSet.add(obj1);

        Set<LevelObject> removedObjects = builder.removeObjects(myObjectSet);
        assertEquals(2, removedObjects.size());
        assertTrue(removedObjects.contains(obj1));
        assertTrue(removedObjects.contains(trigger));
    }

    @Test
    public void testAddRemoveOverwriteTileObject() {
        LevelLayers layers = new LevelLayers(10);
        layers.gridOffset.set(0, 0);

        SingleLayerBuilder builder = new SingleLayerBuilder(layers, 0);

        Set<LevelObject> myObjectSet = new HashSet<>();
        TileObject tile = new TileObject(new BitRectangle(0, 0, layers.cellSize, layers.cellSize), false, 0);
        myObjectSet.add(tile);

        builder.addObjects(myObjectSet);

        assertEquals(tile, layers.getLayer(0).grid[0][0]);

        TileObject tile2 = new TileObject(new BitRectangle(0, 0, layers.cellSize, layers.cellSize), false, 0);
        myObjectSet.clear();
        myObjectSet.add(tile2);
        Set<LevelObject> removedObjects = builder.addObjects(myObjectSet);

        assertEquals(1, removedObjects.size());
        assertEquals(tile, removedObjects.iterator().next());

        assertEquals(tile2, layers.getLayer(0).grid[0][0]);
    }
}
