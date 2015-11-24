package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.level.TriggerObject;

/**
 * Created by Monday on 11/22/2015.
 */
public class TriggerAction implements BuilderAction {

    private TriggerObject trigger;

    public TriggerAction(TriggerObject trigger) {
        this.trigger = trigger;
    }

    @Override
    public void perform(LevelBuilder builder) {
        builder.triggers.put(trigger.uuid, trigger);
    }

    @Override
    public void undo(LevelBuilder builder) {
        builder.triggers.remove(trigger.uuid);
    }
}
