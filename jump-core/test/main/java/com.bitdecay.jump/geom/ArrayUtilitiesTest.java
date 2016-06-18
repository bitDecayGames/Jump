package com.bitdecay.jump.geom;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Monday on 6/12/2016.
 */
public class ArrayUtilitiesTest {
    @Test
    public void testOnGrid() {
        Object[][] grid = new Integer[5][5];
        assertTrue(ArrayUtilities.onGrid(grid, 0, 0));
        assertTrue(ArrayUtilities.onGrid(grid, 4, 4));

        assertFalse(ArrayUtilities.onGrid(grid, 3, 7));
        assertFalse(ArrayUtilities.onGrid(grid, -1, 3));
    }
}
