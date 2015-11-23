package com.bitdecay.jump.level.builder;


import com.bitdecay.jump.level.LevelObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Monday on 11/22/2015.
 */
public class AddRemoveAction implements BuilderAction {
    // All actions seem to be a mix of adds and deletes
    public Set<LevelObject> newObjects;
    public Set<LevelObject> removeObjects;

    public AddRemoveAction(List<LevelObject> newObjects, List<LevelObject> removeObjects) {
        this.newObjects = new HashSet<>(newObjects);
        this.removeObjects = new HashSet<>(removeObjects);
    }

    @Override
    public void perform(LevelBuilder builder) {
        removeObjects.addAll(builder.addObjects(newObjects));
        builder.removeObjects(removeObjects);
    }

    @Override
    public void undo(LevelBuilder builder) {
        builder.removeObjects(newObjects);
        newObjects.addAll(builder.addObjects(removeObjects));
    }
}
