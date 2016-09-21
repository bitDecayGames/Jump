package com.bitdecay.jump.level;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Monday on 9/19/2016.
 */
public class LevelLayersTest {

    @Test
    public void testCreateNew() {
        int cellSize = 10;
        LevelLayers layers = new LevelLayers(cellSize);
        assertNotNull(layers.layers.get(0));

        assertEquals(cellSize, layers.layers.get(0).cellSize);
    }

    @Test
    public void testAddLayer() {
        int cellSize = 10;
        LevelLayers layers = new LevelLayers(cellSize);
        assertNotNull(layers.layers.get(0));

        SingleLayer newLayer = new SingleLayer(cellSize);
        layers.addLayer(1, newLayer);

        assertTrue(layers.hasLayer(1));
        assertEquals(newLayer, layers.getLayer(1));
    }
}
