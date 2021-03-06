package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.annotation.VisibleForTesting;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.level.*;

import java.util.*;

/**
 * Created by Monday on 9/19/2016.
 */
public class LevelLayersBuilder implements ILevelBuilder {

    @VisibleForTesting
    LinkedList<LayeredBuilderAction> actions = new LinkedList<>();

    @VisibleForTesting
    int lastAction = -1;

    public List<LevelObject> selection;

    public Level activeLevel;

    public Map<Integer, SingleLayerBuilder> layerBuilders;

    private int activeLayer = 0;

    public List<LevelBuilderListener> listeners;

    public LevelLayersBuilder(int tileSize) {
        this(new Level(tileSize));
    }

    public LevelLayersBuilder(Level level) {
        layerBuilders = new HashMap<>();
        listeners = new ArrayList<>();
        selection = new ArrayList<>();
        actions = new LinkedList<>();
        lastAction = -1;

        setLevel(level);
    }

    @Override
    public void newLevel(int cellSize) {
        setLevel(new Level(cellSize));
    }

    @Override
    public void setLevel(Level level) {
        activeLevel = level;

        for (Integer layerNumber : activeLevel.layers.layers.keySet()) {
            layerBuilders.put(layerNumber, new SingleLayerBuilder(activeLevel.layers, layerNumber));
        }

        actions = new LinkedList<>();
        lastAction = -1;

        fireToListeners();
    }

    @Override
    public int getCellSize() {
        return activeLevel.tileSize;
    }

    @Override
    public void setActiveLayer(int layer) {
        activeLayer = layer;

        if (!activeLevel.layers.hasLayer(layer)) {
            SingleLayer newLayer = new SingleLayer(activeLevel.layers.cellSize, activeLevel.layers.getLayer(0).grid.length, activeLevel.layers.getLayer(0).grid[0].length);
            activeLevel.layers.addLayer(layer, newLayer);
        }

        if (!layerBuilders.containsKey(layer)) {
            SingleLayerBuilder newLayerBuilder = new SingleLayerBuilder(activeLevel.layers, layer);
            layerBuilders.put(layer, newLayerBuilder);
        }
    }

    public void createLevelObject(BitPointInt startPoint, BitPointInt endPoint, boolean oneway, int material) {
        List<LevelObject> newObjects = new ArrayList<>();

        GeomUtils.splitRect(new BitRectangle(startPoint, endPoint), activeLevel.layers.cellSize, activeLevel.layers.cellSize).forEach(rect ->
                newObjects.add(new TileObject(rect, oneway, material)));
        if (newObjects.size() > 0) {
            LayeredBuilderAction createLevelObjectAction = new AddRemoveAction(activeLayer, newObjects, Collections.emptyList());
            pushAction(createLevelObjectAction);
        }
    }

    @Override
    public void createKineticObject(BitRectangle rect, List<PathPoint> path, boolean pendulum) {
        // copy the list so we don't share data between the level and the world
        List<PathPoint> listCopy = new ArrayList<>();
        for (PathPoint point : path) {
            listCopy.add(new PathPoint(point.destination.minus(rect.xy.plus(rect.width / 2, rect.height / 2)), point.speed, point.stayTime));
        }
        PathedLevelObject kObj = new PathedLevelObject(rect.copyOf(), listCopy, pendulum);

        LayeredBuilderAction createKineticAction = new AddRemoveAction(activeLayer, Arrays.asList(kObj), Collections.emptyList());
        pushAction(createKineticAction);
    }

    public void createObject(LevelObject object) {
        try {
            LayeredBuilderAction createObjectAction = new AddRemoveAction(activeLayer, Arrays.asList(object), Collections.emptyList());
            pushAction(createObjectAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void undo() {
        LayeredBuilderAction undoAction = popAction();
        if (undoAction != null) {
            undoAction.undo(this);
            fireToListeners();
        }
    }

    public void redo() {
        if (lastAction < actions.size()-1) {
            LayeredBuilderAction redoAction = actions.get(lastAction+1);
            redoAction.perform(this);
            lastAction++;
            fireToListeners();
        }
    }

    private void pushAction(LayeredBuilderAction action) {
        actions = new LinkedList<>(actions.subList(0, lastAction+1));
        actions.add(action);
        redo();
    }

    private LayeredBuilderAction popAction() {
        LayeredBuilderAction action = null;
        if (lastAction >= 0) {
            action = actions.get(lastAction);
            lastAction--;
        }
        return action;
    }

    /**
     * Fits the grid to the current level contents and also returns the level constructed in the process
     * @return
     */
    public Level optimizeLevel() {
        Level optimizedLevel = new Level(activeLevel.layers.cellSize);
        optimizedLevel.theme = activeLevel.theme;

        BitPointInt min = getMinXY();
        BitPointInt max = getMaxXY();

        BitPointInt optimizedOffset = new BitPointInt();
        optimizedOffset.x = (min.x / activeLevel.layers.cellSize);
        optimizedOffset.y = (min.y / activeLevel.layers.cellSize);

        LevelLayers optimizedLevelLayers = new LevelLayers(activeLevel.layers.cellSize);
        optimizedLevel.layers = optimizedLevelLayers;
        optimizedLevel.layers.gridOffset.set(optimizedOffset);

        for (Integer layerNumber : activeLevel.layers.layers.keySet()) {
            SingleLayer currentLayer = activeLevel.layers.getLayer(layerNumber);
            TileObject[][] currentGrid = currentLayer.grid;

            SingleLayer optimizedLayer = new SingleLayer(activeLevel.layers.cellSize);
            optimizedLayer.otherObjects = new HashMap<>(currentLayer.otherObjects);
            optimizedLayer.triggers = new HashMap<>(currentLayer.triggers);

            TileObject[][] optimizedGrid;

            int START_SIZE = 10;
            int tileSize = activeLevel.layers.cellSize;

            if (min.x == Integer.MAX_VALUE || min.y == Integer.MAX_VALUE || max.x == Integer.MIN_VALUE || max.y == Integer.MIN_VALUE) {
                min.x = -START_SIZE/2 * tileSize;
                min.y = -START_SIZE/2 * tileSize;
                max.x = START_SIZE/2 * tileSize;
                max.y = START_SIZE/2 * tileSize;
                optimizedGrid = new TileObject[START_SIZE][START_SIZE];
            } else {
                optimizedGrid = new TileObject[(max.x - min.x) / tileSize][(max.y - min.y) / tileSize];
                int xOffset = (min.x / tileSize) - activeLevel.layers.gridOffset.x;
                int yOffset = (min.y / tileSize) - activeLevel.layers.gridOffset.y;
                for (int x = 0; x < optimizedGrid.length; x++) {
                    for (int y = 0; y < optimizedGrid[0].length; y++) {
                        optimizedGrid[x][y] = currentGrid[x+xOffset][y+yOffset];
                    }
                }
            }
            optimizedLayer.grid = optimizedGrid;
            optimizedLevelLayers.addLayer(layerNumber, optimizedLayer);

        optimizedLevel.debugSpawn = activeLevel.debugSpawn;
        }

        return optimizedLevel;
    }

    @VisibleForTesting
    BitPointInt getMinXY() {
        int xmin = Integer.MAX_VALUE;
        int ymin = Integer.MAX_VALUE;

        for (SingleLayer layer : activeLevel.layers.layers.values()) {
            TileObject[][] grid = layer.grid;

            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[0].length; y++) {
                    LevelObject obj = grid[x][y];
                    if (obj != null) {
                        xmin = Integer.min(xmin, (int) obj.rect.xy.x);
                        ymin = Integer.min(ymin, (int) obj.rect.xy.y);
                    }
                }
            }
        }

        return new BitPointInt(xmin, ymin);
    }

    @VisibleForTesting
    BitPointInt getMaxXY() {
        int xmax = Integer.MIN_VALUE;
        int ymax = Integer.MIN_VALUE;

        for (SingleLayer layer : activeLevel.layers.layers.values()) {
            TileObject[][] grid = layer.grid;

            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[0].length; y++) {
                    LevelObject obj = grid[x][y];
                    if (obj != null) {
                        xmax = Integer.max(xmax, (int) (obj.rect.xy.x + obj.rect.width));
                        ymax = Integer.max(ymax, (int) (obj.rect.xy.y + obj.rect.height));
                    }
                }
            }
        }
        return new BitPointInt(xmax, ymax);
    }

    public void setDebugSpawn(DebugSpawnObject spawn) {
        activeLevel.debugSpawn = spawn;
        fireToListeners();
    }

    public void setTheme(int id) {
        activeLevel.theme = id;
    }

    public boolean hasChanges() {
        return actions.size() > 0 && lastAction > -1;
    }

    @Override
    public void selectObject(BitPointInt point, boolean add) {
        selectObject(point, add, true);
    }

    @Override
    public void selectObject(BitPointInt point, boolean add, boolean includeGridObjects) {
        if (!add) {
            selection.clear();
        }
        SingleLayer layer = activeLevel.layers.getLayer(activeLayer);

        layer.triggers.values().forEach(trigger -> {
            if (trigger.selects(point)) {
                selection.add(trigger);
                return;
            }
        });
        layer.otherObjects.values().forEach(object -> {
            if (object.selects(point)) {
                selection.add(object);
                return;
            }
        });
        if (includeGridObjects) {
            TileObject[][] grid = layer.grid;

            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[0].length; y++) {
                    LevelObject object = grid[x][y];
                    if (object != null && object.selects(point)) {
                        selection.add(object);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void selectObjects(BitRectangle selectionArea, boolean addToSelection) {
        if (!addToSelection) {
            selection.clear();
        }
        SingleLayer layer = activeLevel.layers.getLayer(activeLayer);
        layer.triggers.values().forEach(trigger -> {
            if (trigger.selects(selectionArea)) {
                selection.add(trigger);
                return;
            }
        });

        TileObject[][] grid = layer.grid;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                LevelObject object = grid[x][y];
                if (object != null && selectionArea.contains(object.rect)) {
                    selection.add(object);
                }
            }
        }
        layer.otherObjects.values().forEach(object -> {
            if (selectionArea.contains(object.rect)) {
                selection.add(object);
            }
        });
    }

    @Override
    public void deleteSelected() {
        if (selection.size() > 0) {
            LayeredBuilderAction deleteAction = new AddRemoveAction(activeLayer, Collections.emptyList(), selection);
            pushAction(deleteAction);
            selection.clear();
        }
    }

    @Override
    public List<LevelObject> getSelection() {
        return selection;
    }

    public Level getLevel() {
        return activeLevel;
    }

    public void addListener(LevelBuilderListener levelListener) {
        listeners.add(levelListener);
        fireToListeners();
    }

    public void removeListener(LevelBuilderListener levelListener) {
        listeners.remove(levelListener);
    }

    public void fireToListeners() {
        Level optimizedLevel = optimizeLevel();
        for (LevelBuilderListener listener : listeners) {
            listener.levelChanged(optimizedLevel);
        }
    }
}
