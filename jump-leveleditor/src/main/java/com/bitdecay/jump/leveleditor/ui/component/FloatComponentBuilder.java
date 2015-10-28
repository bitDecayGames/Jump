package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class FloatComponentBuilder implements ComponentBuilder {
    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
        return Arrays.asList(ComponentUtils.buildSlider((int) (field.getFloat(thing) * 100), 0, 100, new Callable() {
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
