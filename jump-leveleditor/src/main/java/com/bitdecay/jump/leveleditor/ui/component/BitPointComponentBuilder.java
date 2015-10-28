package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BitPointComponentBuilder implements ComponentBuilder {

    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
        BitPoint bitPoint = (BitPoint) field.get(thing);
        JSlider xslider = new JSlider(-200, 200, 0);
        xslider.setMajorTickSpacing(50);
        xslider.setMinorTickSpacing(10);
        xslider.setValue((int) (bitPoint.x * 100));
        xslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    Field field2 = bitPoint.getClass().getField("x");
                    field2.set(bitPoint, ((JSlider) e.getSource()).getValue());
                    callback.propertyChanged(field2.getName(), thing);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                //				callback.propertyChanged(field.getName(), new BitPoint(((JSlider) e.getSource()).getValue() / 100f, yslider.getValue() / 100f));
            }
        });
        xslider.setPaintTicks(true);
        xslider.setPaintLabels(true);

        JSlider yslider = new JSlider(-200, 200, 0);
        yslider.setMajorTickSpacing(50);
        yslider.setMinorTickSpacing(10);
        yslider.setValue((int) (bitPoint.y * 100));
        yslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    Field field2 = bitPoint.getClass().getField("y");
                    field2.set(bitPoint, ((JSlider) e.getSource()).getValue());
                    callback.propertyChanged(field2.getName(), thing);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                //				callback.propertyChanged(field.getName(), new BitPoint(xslider.getValue(), ((JSlider) e.getSource()).getValue()));
            }
        });
        yslider.setPaintTicks(true);
        yslider.setPaintLabels(true);
        return Arrays.asList(new JLabel("x"), xslider, new JLabel("y"), yslider);
    }

}
