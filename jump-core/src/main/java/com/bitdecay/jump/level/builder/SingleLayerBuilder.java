package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.geom.ArrayUtilities;
import com.bitdecay.jump.level.*;

import java.util.HashSet;
import java.util.Iterator;
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
                LayerUtilities.ensureGridFitsObject(parent, layer, (TileObject)obj);
                gridX = (int) ((obj.rect.xy.x / parent.cellSize) - parent.gridOffset.x);
                gridY = (int) ((obj.rect.xy.y / parent.cellSize) - parent.gridOffset.y);
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

    public Set<LevelObject> removeObjects(Set<LevelObject> objects) {
        SingleLayer layer = parent.getLayer(editingLayer);

        Set<LevelObject> additionalRemovedObjects = new HashSet<>();
        additionalRemovedObjects.addAll(objects);
        // clean up out of other newObjects
        objects.forEach(object -> {
//            if (object == debugSpawn) {
//                debugSpawn = null;
//            }
            layer.otherObjects.remove(object.uuid);
            layer.triggers.remove(object.uuid);

            Iterator<TriggerObject> iterator = layer.triggers.values().iterator();
            TriggerObject trigger;
            while (iterator.hasNext()) {
                trigger = iterator.next();
                if (trigger.triggerer.uuid.equals(object.uuid) || trigger.triggeree.uuid.equals(object.uuid)) {
                    // need to remove the trigger.
                    iterator.remove();
                    additionalRemovedObjects.add(trigger);
                }
            }
        });
        // clean out our grid
        TileObject[][] grid = layer.grid;

        int gridX;
        int gridY;
        for (LevelObject obj : objects) {
            gridX = (int) ((obj.rect.xy.x / parent.cellSize) - parent.gridOffset.x);
            gridY = (int) ((obj.rect.xy.y / parent.cellSize) - parent.gridOffset.y);
            if (ArrayUtilities.onGrid(grid, gridX, gridY) && grid[gridX][gridY] == obj) {
                grid[gridX][gridY] = null;
                LayerUtilities.updateNeighbors(layer, gridX, gridY);
            }
        }
//        fireToListeners();
        return additionalRemovedObjects;
    }
}
