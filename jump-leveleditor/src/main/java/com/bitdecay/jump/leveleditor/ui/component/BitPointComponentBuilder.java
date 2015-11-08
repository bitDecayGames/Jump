package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BitPointComponentBuilder implements ComponentBuilder {

    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {

        BitPoint bitPoint = (BitPoint) field.get(thing);
        JComponent xComponent = ComponentUtils.buildSlider((int) bitPoint.x, -200, 200, new Callable() {
            @Override
            public void call(Object value) {
                try {
                    Field field2 = bitPoint.getClass().getField("x");
                    field2.set(bitPoint, value);
                    callback.propertyChanged(field2.getName(), thing);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        JComponent yComponent = ComponentUtils.buildSlider((int) bitPoint.y, -200, 200, new Callable() {
            @Override
            public void call(Object value) {
                try {
                    Field field = bitPoint.getClass().getField("y");
                    field.set(bitPoint, value);
                    callback.propertyChanged(field.getName(), thing);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        return Arrays.asList(new JLabel("x"), xComponent, new JLabel("y"), yComponent);
    }
}
