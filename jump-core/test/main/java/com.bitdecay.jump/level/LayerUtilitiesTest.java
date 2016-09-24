package com.bitdecay.jump.level;

import com.bitdecay.jump.geom.BitRectangle;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 9/19/2016.
 */
public class LayerUtilitiesTest {

    @Test
    public void testUpdateNeighbors() {
        SingleLayer layer = new SingleLayer(10);

        // center
        layer.grid[1][1] = new TileObject(new BitRectangle(), false, 0);

        // sides
        layer.grid[1][0] = new TileObject(new BitRectangle(), false, 0);
        layer.grid[0][1] = new TileObject(new BitRectangle(), false, 0);
        layer.grid[2][1] = new TileObject(new BitRectangle(), false, 0);
        layer.grid[1][2] = new TileObject(new BitRectangle(), false, 0);

        assertTrue(layer.grid[1][0].collideNValue == 0);
        assertTrue(layer.grid[0][1].collideNValue == 0);
        assertTrue(layer.grid[2][1].collideNValue == 0);
        assertTrue(layer.grid[1][2].collideNValue == 0);

        LayerUtilities.updateNeighbors(layer, 1, 1);

        assertTrue(layer.grid[1][0].collideNValue == Direction.UP);
        assertTrue(layer.grid[0][1].collideNValue == Direction.RIGHT);
        assertTrue(layer.grid[2][1].collideNValue == Direction.LEFT);
        assertTrue(layer.grid[1][2].collideNValue == Direction.DOWN);
    }

    @Test
    public void testUpdateOwnNeighborValues() {
        SingleLayer layer = new SingleLayer(10);

        // center
        layer.grid[1][1] = new TileObject(new BitRectangle(), false, 0);

        // sides
        layer.grid[1][0] = new TileObject(new BitRectangle(), false, 0); // bottom
        layer.grid[0][1] = new TileObject(new BitRectangle(), false, 0); // left
        layer.grid[2][1] = new TileObject(new BitRectangle(), false, 0); // right
        layer.grid[1][2] = new TileObject(new BitRectangle(), false, 0); // bottom

        assertTrue("Neighbor value is zero on new tile", layer.grid[1][1].collideNValue == 0);
        assertTrue("Neighbor render is zero on new tile", layer.grid[1][1].renderNValue == 0);

        LayerUtilities.updateOwnNeighborValues(layer, 1, 1);

        assertTrue("All neighbors are set", layer.grid[1][1].collideNValue == Direction.ALL);
        assertTrue("All neighbors are set", layer.grid[1][1].renderNValue == Direction.ALL);

        layer.grid[1][0] = null; // bottom
        layer.grid[0][1] = null; // left

        LayerUtilities.updateOwnNeighborValues(layer, 1, 1);

        assertTrue("Up and right set", layer.grid[1][1].collideNValue == (Direction.UP | Direction.RIGHT));
        assertTrue("Up and right set", layer.grid[1][1].renderNValue == (Direction.UP | Direction.RIGHT));
    }

    @Test
    public void testUpdateOwnNeighborValuesOneWay() {
        SingleLayer layer = new SingleLayer(10);

        // center
        layer.grid[1][1] = new TileObject(new BitRectangle(), false, 0);

        // sides
        layer.grid[1][0] = new TileObject(new BitRectangle(), true, 0); // bottom
        layer.grid[0][1] = new TileObject(new BitRectangle(), true, 0); // left
        layer.grid[2][1] = new TileObject(new BitRectangle(), true, 0); // right
        layer.grid[1][2] = new TileObject(new BitRectangle(), true, 0); // bottom

        assertTrue("Neighbor value is zero on new tile", layer.grid[1][1].collideNValue == 0);
        assertTrue("Neighbor render is zero on new tile", layer.grid[1][1].renderNValue == 0);

        LayerUtilities.updateOwnNeighborValues(layer, 1, 1);

        assertTrue("All neighbors are set", layer.grid[1][1].collideNValue == 0);
        assertTrue("All neighbors are set", layer.grid[1][1].renderNValue == Direction.ALL);

        layer.grid[1][0] = null; // bottom
        layer.grid[0][1] = null; // left

        LayerUtilities.updateOwnNeighborValues(layer, 1, 1);

        assertTrue("Up and right set", layer.grid[1][1].collideNValue == 0);
        assertTrue("Up and right set", layer.grid[1][1].renderNValue == (Direction.UP | Direction.RIGHT));
    }
}
