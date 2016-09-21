//package com.bitdecay.jump.level.builder;
//
//import com.bitdecay.jump.annotation.VisibleForTesting;
//import com.bitdecay.jump.geom.BitPointInt;
//import com.bitdecay.jump.geom.BitRectangle;
//import com.bitdecay.jump.geom.GeomUtils;
//import com.bitdecay.jump.geom.PathPoint;
//import com.bitdecay.jump.level.*;
//
//import java.util.*;
//
///**
// * Created by Monday on 9/15/2016.
// */
//public class LayersBuilder {
//    @VisibleForTesting
//    LinkedList<BuilderAction> actions = new LinkedList<>();
//
//    @VisibleForTesting
//    int lastAction = -1;
//
//    public LevelLayers layers;
//
//    /**
//     * Holds all triggers
//     */
//    public HashMap<String, TriggerObject> triggers;
//
//    public DebugSpawnObject debugSpawn;
//
//    public List<LevelBuilderListener> listeners;
//
//    private int activeLayer;
//
//
//    public LayersBuilder(int tileSize) {
//        listeners = new ArrayList<>();
//
//        setLevel(new Level(tileSize));
//    }
//
//    public LayersBuilder(Level level) {
//        listeners = new ArrayList<>();
//
//        setLevel(level);
//    }
//
//    public void setLevel(Level level) {
//        layers = level.layers;
//        actions = new LinkedList<>();
//
//        triggers = new HashMap<>();
//        if (level.triggers != null) {
//            for (TriggerObject trigger : level.triggers) {
//                triggers.put(trigger.uuid, trigger);
//            }
//        }
//        debugSpawn = level.debugSpawn;
//        for (LevelBuilderListener levelListener : listeners) {
//            levelListener.levelChanged(level);
//        }
//    }
//
//    public void setActiveLayer(int i) {
//        activeLayer = i;
//    }
//
//    public void undo() {
//        BuilderAction undoAction = popAction();
//        if (undoAction != null) {
//            undoAction.undo(this);
//        }
//    }
//
//    public void redo() {
//        if (lastAction < actions.size()-1) {
//            BuilderAction redoAction = actions.get(lastAction+1);
//            redoAction.perform(this);
//            lastAction++;
//        }
//    }
//
//    private void pushAction(BuilderAction action) {
//        actions = new LinkedList<>(actions.subList(0, lastAction+1));
//        actions.add(action);
//        redo();
//    }
//
//    private BuilderAction popAction() {
//        BuilderAction action = null;
//        if (lastAction >= 0) {
//            action = actions.get(lastAction);
//            lastAction--;
//        }
//        return action;
//    }
//
//    public void createLevelObject(BitPointInt startPoint, BitPointInt endPoint, boolean oneway, int material) {
//        List<LevelObject> newObjects = new ArrayList<>();
//
//        GeomUtils.splitRect(new BitRectangle(startPoint, endPoint), layers.cellSize, layers.cellSize).forEach(rect ->
//                newObjects.add(new TileObject(rect, oneway, material)));
//        if (newObjects.size() > 0) {
//            BuilderAction createLevelObjectAction = new AddRemoveAction(activeLayer, newObjects, Collections.emptyList());
//            pushAction(createLevelObjectAction);
//        }
//    }
//
//    public void createKineticObject(BitRectangle rect, List<PathPoint> path, boolean pendulum) {
//        // copy the list so we don't share data between the level and the world
//        List<PathPoint> listCopy = new ArrayList<>();
//        for (PathPoint point : path) {
//            listCopy.add(new PathPoint(point.destination.minus(rect.xy.plus(rect.width / 2, rect.height / 2)), point.speed, point.stayTime));
//        }
//        PathedLevelObject kObj = new PathedLevelObject(rect.copyOf(), listCopy, pendulum);
//
//        BuilderAction createKineticAction = new AddRemoveAction(activeLayer, Arrays.asList(kObj), Collections.emptyList());
//        pushAction(createKineticAction);
//    }
//}
