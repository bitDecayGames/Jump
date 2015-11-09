package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.geom.BitPoint;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Monday on 10/27/2015.
 */
public class ComponentUtils {

    public static JComponent buildSlider(int initial, int min, int max, Callable callable) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JSlider slider = new JSlider(min, max, min);
        JTextField textField = new JTextField(4);
        textField.setText(Integer.toString(initial));
        textField.setMaximumSize(new Dimension(50, 30));

        slider.setMajorTickSpacing((max - min) / 5);
        slider.setMinorTickSpacing((max-min) / 10);
        if (initial < min) {
            slider.setValue(min);
        } else if (initial > max) {
            slider.setValue(max);
        }
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                if (slider.hasFocus()) {
                    textField.setText(Integer.toString(value));
                    if (!slider.getValueIsAdjusting()) {
                        callable.call(value);
                    }
                }
            }
        });
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider);


        textField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                try {
                    int value = Integer.parseInt(textField.getText());
                    if (textField.hasFocus()) {
                        slider.setValue(value);
                        callable.call(value);
                    }
                } catch (Exception parseError) {
                    // ignore
                }
            }
        });
        panel.add(textField);
        return panel;
    }
}
