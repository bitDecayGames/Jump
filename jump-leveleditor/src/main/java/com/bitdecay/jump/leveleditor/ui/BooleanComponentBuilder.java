package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.BitBody;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BooleanComponentBuilder implements ComponentBuilder {

    @Override
    public List<JComponent> build(Field field, BitBody body, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
        JCheckBox checkBox = new JCheckBox();
        checkBox.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    field.setBoolean(body, checkBox.isSelected());
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        return Arrays.asList(checkBox);
    }

}
