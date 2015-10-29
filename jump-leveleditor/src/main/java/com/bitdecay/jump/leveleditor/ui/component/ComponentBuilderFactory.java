package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.properties.BitBodyProperties;

import java.util.HashMap;
import java.util.Map;

public class ComponentBuilderFactory {

    public static final String GENERIC_BUILDER = "generic";

    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String BOOLEAN = "boolean";

    private static Map<String, ComponentBuilder> components;

    static {
        components = new HashMap<String, ComponentBuilder>();
        components.put(GENERIC_BUILDER, new GenericComponentBuilder());
        components.put(BitBodyProperties.class.getName(), new GenericComponentBuilder());
        components.put(BitPoint.class.getName(), new BitPointComponentBuilder());
        components.put(INT, new IntComponentBuilder());
        components.put(FLOAT, new FloatComponentBuilder());
        components.put(BOOLEAN, new BooleanComponentBuilder());
    }

    public static ComponentBuilder getBuilder(String name) {
        ComponentBuilder builder = components.get(name);
        if (builder == null) {
            return components.get(GENERIC_BUILDER);
        } else {
            return builder;
        }
    }
}
