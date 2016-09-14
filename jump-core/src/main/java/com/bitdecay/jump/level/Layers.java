package com.bitdecay.jump.level;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Fields are public for ease of serialization. Methods should be used to access all fields
 * Created by Monday on 9/12/2016.
 */
public class Layers {
    public SortedMap<Integer, TileObject[][]> layers = new TreeMap<>();

    public Layers() {
        // Here for JSON serialization
    }

    public Layers(int celSize) {
        layers.put(0, new TileObject[celSize][celSize]);
    }

    public void addLayer(int index, TileObject[][] grid) {
        layers.put(index, grid);
    }

    public boolean hasLayer(int index) {
        return layers.containsKey(index);
    }

    public TileObject[][] getLayer(int index) {
        return layers.get(index);
    }
}
