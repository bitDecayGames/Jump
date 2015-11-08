package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.annotation.ValueRange;
import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class FloatComponentBuilder implements ComponentBuilder {
    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {
        int min = 0;
        int max = 1000;
        if (field.isAnnotationPresent(ValueRange.class)) {
            min = field.getAnnotation(ValueRange.class).min();
            max = field.getAnnotation(ValueRange.class).max();
        }
        return Arrays.asList(ComponentUtils.buildSlider((int) (field.getFloat(thing) * 100), min * 100, max * 100, new Callable() {
            @Override
            public void call(Object value) {
                try {
                    field.set(thing, ((Integer)value) / 100f);
                    callback.propertyChanged(field.getName(), thing);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
