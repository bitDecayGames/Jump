package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.properties.BitBodyProperties;

import java.util.HashMap;
import java.util.Map;

public class ComponentBuilderFactory {

    public static final String GENERIC_BUILDER = "generic";

    private static Map<String, ComponentBuilder> components;

    static {
        components = new HashMap<String, ComponentBuilder>();
        components.put(GENERIC_BUILDER, new GenericComponentBuilder());
        components.put(BitBodyProperties.class.getName(), new GenericComponentBuilder());
        components.put("com.bitdecay.jump.geom.BitPoint", new BitPointComponentBuilder());
        components.put("int", new IntComponentBuilder());
        components.put("float", new FloatComponentBuilder());
        components.put("boolean", new BooleanComponentBuilder());
    }

    public static ComponentBuilder getBuilder(String name) {
        return components.get(name);
    }
}
