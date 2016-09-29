package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Monday on 9/28/2016.
 */
public class StringComponentBuilder implements ComponentBuilder {
    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {
        JTextField textField = new JTextField(4);
        textField.setMaximumSize(new Dimension(10, 0));
        textField.setText(field.get(thing).toString());
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    field.set(thing, textField.getText());
                    callback.propertyChanged(field.getName(), thing);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        });
        return Arrays.asList(textField);
    }
}
