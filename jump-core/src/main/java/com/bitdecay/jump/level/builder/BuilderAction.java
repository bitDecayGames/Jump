package com.bitdecay.jump.level.builder;

import java.util.*;

/**
 * Created by Monday on 10/14/2015.
 */
public class BuilderAction {
    // All actions seem to be a mix of adds and deletes
    Set<LevelObject> newObjects;
    Set<LevelObject> removeObjects;

    public BuilderAction(List<LevelObject> newObjects, List<LevelObject> removeObjects) {
        this.newObjects = new HashSet<>(newObjects);
        this.removeObjects = new HashSet<>(removeObjects);
    }
}
