package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BooleanComponentBuilder implements ComponentBuilder {
    private boolean lastState;

    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(field.getBoolean(thing));
        lastState = checkBox.isSelected();
        checkBox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    if (checkBox.isSelected() != lastState) {
                        field.setBoolean(thing, checkBox.isSelected());
                        callback.propertyChanged(field.getName(), thing);
                        lastState = checkBox.isSelected();
                    }
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        return Arrays.asList(checkBox);
    }

}
