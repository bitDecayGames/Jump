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
        this.cellSize = cellSize;
        grid = new TileObject[10][10];
        otherObjects = new HashMap<>();
        triggers = new HashMap<>();
    }
}
