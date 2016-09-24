package com.bitdecay.jump.level.builder;

public interface LayeredBuilderAction {
    void perform(LevelLayersBuilder builder);
    void undo(LevelLayersBuilder builder);
}
