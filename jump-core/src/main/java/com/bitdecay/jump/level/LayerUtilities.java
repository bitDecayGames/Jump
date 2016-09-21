package com.bitdecay.jump.level;

import com.bitdecay.jump.annotation.VisibleForTesting;
import com.bitdecay.jump.geom.ArrayUtilities;
import com.bitdecay.jump.geom.BitPointInt;

/**
 * Created by Monday on 9/19/2016.
 */
public class LayerUtilities {

    @VisibleForTesting
    public static void updateNeighbors(SingleLayer layer, int x, int y) {

        updateOwnNeighborValues(layer, x, y);

        updateOwnNeighborValues(layer, x + 1, y);
        updateOwnNeighborValues(layer, x - 1, y);
        updateOwnNeighborValues(layer, x, y + 1);
        updateOwnNeighborValues(layer, x, y - 1);
    }

    @VisibleForTesting
    static void updateOwnNeighborValues(SingleLayer layer, int x, int y) {
        TileObject[][] grid = layer.grid;

        if (!ArrayUtilities.onGrid(grid, x, y) || grid[x][y] == null) {
            return;
        }

        // check right
        if (ArrayUtilities.onGrid(grid, x + 1, y) && grid[x + 1][y] != null) {
            if (grid[x+1][y].oneway) {
                grid[x][y].collideNValue &= Direction.NOT_RIGHT;
            } else {
                grid[x][y].collideNValue |= Direction.RIGHT;
            }
            grid[x][y].renderNValue |= Direction.RIGHT;
        } else {
            grid[x][y].collideNValue &= Direction.NOT_RIGHT;
            grid[x][y].renderNValue &= Direction.NOT_RIGHT;
        }
        // check left
        if (ArrayUtilities.onGrid(grid, x - 1, y) && grid[x - 1][y] != null) {
            if (grid[x-1][y].oneway) {
                grid[x][y].collideNValue &= Direction.NOT_LEFT;
            } else {
                grid[x][y].collideNValue |= Direction.LEFT;
            }
            grid[x][y].renderNValue |= Direction.LEFT;
        } else {
            grid[x][y].collideNValue &= Direction.NOT_LEFT;
            grid[x][y].renderNValue &= Direction.NOT_LEFT;
        }
        // check up
        if (ArrayUtilities.onGrid(grid, x, y + 1) && grid[x][y + 1] != null) {
            if (grid[x][y+1].oneway) {
                grid[x][y].collideNValue &= Direction.NOT_UP;
            } else {
                grid[x][y].collideNValue |= Direction.UP;
            }
            grid[x][y].renderNValue |= Direction.UP;
        } else {
            grid[x][y].collideNValue &= Direction.NOT_UP;
            grid[x][y].renderNValue &= Direction.NOT_UP;
        }
        // check down
        if (ArrayUtilities.onGrid(grid, x, y - 1) && grid[x][y - 1] != null) {
            if (grid[x][y-1].oneway) {
                grid[x][y].collideNValue &= Direction.NOT_DOWN;
            } else {
                grid[x][y].collideNValue |= Direction.DOWN;
            }
            grid[x][y].renderNValue |= Direction.DOWN;
        } else {
            grid[x][y].collideNValue &= Direction.NOT_DOWN;
            grid[x][y].renderNValue &= Direction.NOT_DOWN;
        }
    }

    /**
     * Ensures the given object fits on the builder's grid. If not, resize
     * and rebuild the grid such that it does fit.
     * @param parent
     * @param obj The new tile object being added to the builder
     */
    public static void ensureGridFitsObject(LevelLayers parent, SingleLayer layer, TileObject obj) {
        TileObject[][] grid = layer.grid;

        BitPointInt objCell = LayerUtilities.getOccupiedCell(parent, layer, obj);
        while (!ArrayUtilities.onGrid(grid, objCell.x, objCell.y)) {
            TileObject[][] newGrid = new TileObject[grid.length * 2][grid[0].length * 2];
            BitPointInt newCell = new BitPointInt(0, 0);
            for (int i = 0; i < grid.length; i++) {
                newCell.x = i + grid.length / 2;
                for (int j = 0; j < grid[0].length; j++) {
                    newCell.y = j + grid[0].length / 2;
                    newGrid[newCell.x][newCell.y] = grid[i][j];
                }
            }
            grid = newGrid;

            layer.grid = grid;
            parent.gridOffset.set(parent.gridOffset.x - newGrid.length / 4, parent.gridOffset.y - newGrid[0].length / 4);

            // rebuild our objCell now that we changed the grid
            objCell = LayerUtilities.getOccupiedCell(parent, layer, obj);
        }
    }

    private static BitPointInt getOccupiedCell(LevelLayers parent, SingleLayer layer, LevelObject obj) {
        BitPointInt objCell;
        objCell = new BitPointInt((int) obj.rect.xy.x, (int) obj.rect.xy.y);
        objCell = objCell.floorDivideBy(layer.cellSize, layer.cellSize).minus(parent.gridOffset);
        return objCell;
    }
}
