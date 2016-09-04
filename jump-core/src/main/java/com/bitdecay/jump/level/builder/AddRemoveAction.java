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
    public Set<LevelObject> createObjects;
    public Set<LevelObject> removeObjects;

    public Set<LevelObject> collateralRemoves;

    public AddRemoveAction(List<LevelObject> newObjects, List<LevelObject> removeObjects) {
        this.createObjects = new HashSet<>(newObjects);
        this.removeObjects = new HashSet<>(removeObjects);
        this.collateralRemoves = new HashSet<>();
    }

    @Override
    public void perform(LevelBuilder builder) {
        /*
         *when performing our actions, it may be the case that adding an option removes something where the
         * object was placed (specifically in when drawing out tiles)
         */
        collateralRemoves = builder.addObjects(createObjects);

        /*
         * Removing some objects may also remove attached data such as triggers tied to that object
         */
        removeObjects.addAll(builder.removeObjects(removeObjects));
    }

    @Override
    public void undo(LevelBuilder builder) {
        builder.removeObjects(createObjects);
        builder.addObjects(removeObjects);
        builder.addObjects(collateralRemoves);
    }
}
