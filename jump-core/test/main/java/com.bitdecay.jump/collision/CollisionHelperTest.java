package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Monday on 9/5/2016.
 */
public class CollisionHelperTest {

    @Test
    public void testGetGriddedPotentialCollisions() {
        BitBody[][] grid = new BitBody[3][3];
        int gridSize = 10;
        BitPointInt offset = new BitPointInt();

        BitBody body1 = new BitBody();
        body1.aabb = new BitRectangle(gridSize * 1, gridSize * 1, gridSize, gridSize);
        grid[1][1] = body1;

        BitBody actor = new BitBody();
        actor.aabb = new BitRectangle(1, 1, 10, 10);

        Set<BitBody> potentials = CollisionHelper.getGriddedPotentialCollisions(actor, grid, gridSize, offset);
        assert (potentials.size() == 1);
        assert (potentials.contains(body1));
    }

    @Test
    public void testGetGriddedPotentialCollisionsNone() {
        BitBody[][] grid = new BitBody[3][3];
        int gridSize = 10;
        BitPointInt offset = new BitPointInt();

        BitBody body1 = new BitBody();
        body1.aabb = new BitRectangle(gridSize * 2, gridSize * 2, gridSize, gridSize);
        grid[2][2] = body1;

        BitBody actor = new BitBody();
        actor.aabb = new BitRectangle(1, 1, 10, 10);

        Set<BitBody> potentials = CollisionHelper.getGriddedPotentialCollisions(actor, grid, gridSize, offset);
        assertEquals (0, potentials.size());
    }

    @Test
    public void testGetGriddedPotentialCollisionsMulti() {
        BitBody[][] grid = new BitBody[3][3];
        int celSize = 10;
        BitPointInt offset = new BitPointInt();

        BitBody body1 = new BitBody();
        body1.aabb = new BitRectangle(celSize * 1, celSize * 1, celSize, celSize);
        grid[1][1] = body1;

        BitBody body2 = new BitBody();
        body2.aabb = new BitRectangle(celSize * 1, celSize * 2, celSize, celSize);
        grid[1][2] = body2;

        BitBody body3 = new BitBody();
        body3.aabb = new BitRectangle(celSize * 2, celSize * 1, celSize, celSize);
        grid[2][1] = body3;

        BitBody body4 = new BitBody();
        body4.aabb = new BitRectangle(celSize * 2, celSize * 2, celSize, celSize);
        grid[2][2] = body4;



        BitBody actor = new BitBody();
        actor.aabb = new BitRectangle(celSize * 1.5f, celSize * 1.5f, celSize, celSize);

        Set<BitBody> potentials = CollisionHelper.getGriddedPotentialCollisions(actor, grid, celSize, offset);
        assertEquals (4, potentials.size());
        assert (potentials.contains(body1));
        assert (potentials.contains(body2));
        assert (potentials.contains(body3));
        assert (potentials.contains(body4));
    }
}
