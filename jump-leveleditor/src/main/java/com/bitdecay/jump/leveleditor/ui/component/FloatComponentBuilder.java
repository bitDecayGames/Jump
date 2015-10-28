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
        JSlider slider = new JSlider(0, 100, 0);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        for (int i = 0; i <= 10; i++) {
            labelTable.put(i * 10, new JLabel(Integer.toString(i)));
        }
        slider.setLabelTable(labelTable);
        slider.setMajorTickSpacing(1);
        slider.setValue((int) (field.getFloat(thing) * 100));
        slider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    try {
                        field.set(thing, ((JSlider) e.getSource()).getValue() / 100f);
                        callback.propertyChanged(field.getName(), thing);
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return Arrays.asList(slider);
    }
}
