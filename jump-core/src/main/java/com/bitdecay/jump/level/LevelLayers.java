package com.bitdecay.jump.level;

import com.bitdecay.jump.geom.BitPointInt;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Fields are public for ease of serialization. Methods should be used to access all fields
 * Created by Monday on 9/12/2016.
 */
public class LevelLayers {
    public int cellSize;

    public final BitPointInt gridOffset = new BitPointInt();

    public SortedMap<Integer, SingleLayer> layers = new TreeMap<>();

    public LevelLayers() {
        // Here for JSON serialization
    }

    public LevelLayers(int cellSize) {
        this.cellSize = cellSize;
        layers.put(0, new SingleLayer(cellSize));
        int halfGrid = layers.get(0).grid.length / 2;
        gridOffset.set(-halfGrid, -halfGrid);
    }

    public void addLayer(int index, SingleLayer grid) {
        layers.put(index, grid);
    }

    public boolean hasLayer(int index) {
        return layers.containsKey(index);
    }

    public SingleLayer getLayer(int index) {
        return layers.get(index);
    }
}
