package com.bitdecay.jump.level.builder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Monday on 10/14/2015.
 */
public class BuilderAction {
    // All actions seem to be a mix of adds and deletes
    public enum Type {
        ADD,
        DELETE,
    }

    Type type;
    List<LevelObject> objects;

    public BuilderAction(Type type, LevelObject... objects) {
        this.type = type;
        this.objects = Arrays.asList(objects);
    }
}
