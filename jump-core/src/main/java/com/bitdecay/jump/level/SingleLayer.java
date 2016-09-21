package com.bitdecay.jump.level;

import com.bitdecay.jump.geom.BitPointInt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 9/19/2016.
 */
public class SingleLayer {
    public TileObject[][] grid;
    public Map<String, LevelObject> otherObjects;
    public Map<String, TriggerObject> triggers;
    public BitPointInt gridOffset;
    public int cellSize;

    public SingleLayer(int cellSize) {
        this.cellSize = cellSize;
        grid = new TileObject[10][10];
        gridOffset = new BitPointInt();
        otherObjects = new HashMap<>();
        triggers = new HashMap<>();
    }
}
