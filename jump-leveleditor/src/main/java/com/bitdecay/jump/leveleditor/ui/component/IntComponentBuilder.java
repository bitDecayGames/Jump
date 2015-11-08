package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.annotation.ValueRange;
import com.bitdecay.jump.leveleditor.ui.PropModUICallback;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class IntComponentBuilder implements ComponentBuilder {

    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {
        int min = 0;
        int max = 1000;
        if (field.isAnnotationPresent(ValueRange.class)) {
            min = field.getAnnotation(ValueRange.class).min();
            max = field.getAnnotation(ValueRange.class).max();
        }
        return Arrays.asList(ComponentUtils.buildSlider(field.getInt(thing), min, max, new Callable() {
            @Override
            public void call(Object value) {
                try {
                    field.set(thing, value);
                    callback.propertyChanged(field.getName(), thing);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
