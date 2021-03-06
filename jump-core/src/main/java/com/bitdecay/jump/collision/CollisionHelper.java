package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.ArrayUtilities;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Monday on 9/5/2016.
 */
public class CollisionHelper {
    public static Set<BitBody> getGriddedPotentialCollisions(BitBody actor, BitBody[][] grid, int cellSize, BitPointInt offset) {

        Set<BitBody> allSuspects = null;

        // 1. determine our starting cell taking the grid offset into account
        BitPoint startCell = actor.aabb.xy.floorDivideBy(cellSize, cellSize).minus(offset);

        // 2. determine width/height in tiles
        BitPoint endCell = actor.aabb.xy.plus(actor.aabb.getWidth(), actor.aabb.getHeight()).floorDivideBy(cellSize, cellSize).minus(offset);

        // 3. loop over those all occupied tiles
        for (int x = (int) startCell.x; x <= (int) endCell.x; x++) {
            for (int y = (int) startCell.y; y <= (int) endCell.y; y++) {
                // ensure valid cell
                if (ArrayUtilities.onGrid(grid, x, y) && grid[x][y] != null) {
                    BitBody suspect = grid[x][y];
                    if (allSuspects == null) {
                        allSuspects = new HashSet<>();
                    }
                    allSuspects.add(suspect);
                }
            }
        }
        return allSuspects == null ? Collections.emptySet() : allSuspects;
    }
}
