package com.bitdecay.jump.leveleditor.ui;

import java.util.HashMap;
import java.util.Map;

public class ComponentBuilderFactory {

    private static Map<String, ComponentBuilder> components;

    static {
        components = new HashMap<String, ComponentBuilder>();
        components.put("com.bitdecay.jump.geom.BitPoint", new BitPointComponentBuilder());
        components.put("int", new IntComponentBuilder());
        components.put("float", new FloatComponentBuilder());
        components.put("boolean", new BooleanComponentBuilder());
    }

    public static ComponentBuilder getBuilder(String name) {
        return components.get(name);
    }
}
