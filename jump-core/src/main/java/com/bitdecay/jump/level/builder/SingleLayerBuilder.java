package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.level.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Monday on 9/19/2016.
 */
public class SingleLayerBuilder {

    public LevelLayers parent;

    public final int editingLayer;

    public SingleLayerBuilder(LevelLayers parent, int layer) {
        this.parent = parent;
        this.editingLayer = layer;
    }

    Set<LevelObject> addObjects(Set<LevelObject> objects) {
        SingleLayer layer = parent.getLayer(editingLayer);

        Set<LevelObject> removedObjects = new HashSet<>();
        int gridX;
        int gridY;
        for (LevelObject obj : objects) {
            if (obj instanceof TileObject) {
                LayerUtilities.ensureGridFitsObject(layer, (TileObject)obj);
                gridX = (int) ((obj.rect.xy.x / parent.cellSize) - layer.gridOffset.x);
                gridY = (int) ((obj.rect.xy.y / parent.cellSize) - layer.gridOffset.y);
                if (layer.grid[gridX][gridY] != null) {
                    removedObjects.add(layer.grid[gridX][gridY]);
                }
                layer.grid[gridX][gridY] = (TileObject)obj;
                LayerUtilities.updateNeighbors(layer, gridX, gridY);
            } else if (obj instanceof TriggerObject) {
                layer.triggers.put(obj.uuid, (TriggerObject) obj);
            } else if (obj instanceof DebugSpawnObject) {
//                debugSpawn = (DebugSpawnObject) obj;
            } else {
                layer.otherObjects.put(obj.uuid, obj);
            }
        }
//        layer.addLayer(0, grid);
//        fireToListeners();
        return removedObjects;
    }
}
