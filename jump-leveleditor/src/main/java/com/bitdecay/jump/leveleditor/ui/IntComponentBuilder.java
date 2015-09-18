package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.BitBody;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class IntComponentBuilder implements ComponentBuilder {

    @Override
    public List<JComponent> build(Field field, BitBody body, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
        JSlider slider = new JSlider(0, 1000, 0);
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(25);
        slider.setValue(field.getInt(body));
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    field.set(body, ((JSlider) e.getSource()).getValue());
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return Arrays.asList(slider);
    }

}
