package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.level.LevelObject;

/**
 * Created by Monday on 11/22/2015.
 */
public class TriggerAction implements BuilderAction {

    private LevelObject triggerer;
    private LevelObject triggeree;
    private boolean create;

    public TriggerAction(LevelObject triggerer, LevelObject triggeree, boolean create) {
        this.triggerer = triggerer;
        this.triggeree = triggeree;
        this.create = create;
    }

    @Override
    public void perform(LevelBuilder builder) {
        if (create) {
            triggerer.objectsTriggeredByThis.add(triggeree.uuid);
        } else {
            triggerer.objectsTriggeredByThis.remove(triggeree.uuid);
        }
    }

    @Override
    public void undo(LevelBuilder builder) {
        if (create) {
            triggerer.objectsTriggeredByThis.remove(triggeree.uuid);
        } else {
            triggerer.objectsTriggeredByThis.add(triggeree.uuid);
        }
    }
}
