package com.bitdecay.jump.level;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 9/19/2016.
 */
public class SingleLayer {
    public TileObject[][] grid;
    public Map<String, LevelObject> otherObjects;
    public Map<String, TriggerObject> triggers;
    public int cellSize;

    public SingleLayer() {
        // Here for JSON
    }

    public SingleLayer(int cellSize) {
        this(cellSize, 10, 10);
    }

    public SingleLayer(int cellSize, int width, int height) {
        this.cellSize = cellSize;
        grid = new TileObject[width][height];
        otherObjects = new HashMap<>();
        triggers = new HashMap<>();
    }
}
