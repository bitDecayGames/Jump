package com.bitdecay.jump.level;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Monday on 9/19/2016.
 */
public class SingleLayerTest {

    @Test
    public void TestCreate() {
        SingleLayer layer = new SingleLayer(10);

        assertEquals(10, layer.cellSize);
        assertNotNull(layer.grid);
//        assertNotNull(layer.gridOffset);
        assertNotNull(layer.otherObjects);
    }
}
