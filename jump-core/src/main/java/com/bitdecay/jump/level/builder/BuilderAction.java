package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.level.builder.LevelBuilder;

/**
 * Created by Monday on 10/14/2015.
 */
public interface BuilderAction {
    void perform(LevelBuilder builder);
    void undo(LevelBuilder builder);
}
