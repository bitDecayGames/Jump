package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.LevelLayers;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.jump.level.MockLevelObject;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

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
    public void testAddObject() {
        LevelLayers layers = new LevelLayers(10);

        SingleLayerBuilder builder = new SingleLayerBuilder(layers, 0);

        Set<LevelObject> myObjectSet = new HashSet<>();
        MockLevelObject tile = new MockLevelObject(new BitRectangle(10, 10, 10, 10));
        tile.uuid = "test";
        myObjectSet.add(tile);

        builder.addObjects(myObjectSet);

        assertEquals(tile, layers.getLayer(0).otherObjects.get("test"));
    }
}
