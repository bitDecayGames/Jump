package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

/**
 * Created by Monday on 9/28/2016.
 */
public class MapComponentBuilder implements ComponentBuilder {
    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {
        Map map = (Map) field.get(thing);
        Set keys = map.keySet();
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }

        // Enforce String keys
        for (Object key : keys) {
            if (!(key instanceof String)) {
                System.err.println("All keys in map on field " + field.getName() + " must be of type String");
                return Collections.emptyList();
            }
        }

        // Enforce String values
        for (Object value : map.values()) {
            if (!(value instanceof String)) {
                System.err.println("All values in map on field " + field.getName() + " must be of type String");
                return Collections.emptyList();
            }
        }


        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));

        int i = 1;
        for (Object o : map.entrySet()) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) o;
            JPanel subFieldPanel = new JPanel();
            subFieldPanel.setLayout(new BorderLayout());
            Color borderColor = new Color(100, 100, 100);
            for (int brightener = 0; brightener < depth; brightener++) {
                borderColor = borderColor.brighter();
            }
            subFieldPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor, 2), (String) entry.getKey()));

            JTextField textField = new JTextField(4);
            textField.setMaximumSize(new Dimension(10, 0));

            String existingValue = (String) map.get(entry.getKey());

            textField.setText(existingValue == null ? "" : existingValue);
            textField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    map.put(entry.getKey(), textField.getText());
                    callback.propertyChanged(field.getName(), thing);
                }
            });
            subFieldPanel.add(textField);
            fieldPanel.add(subFieldPanel);
        }
        return Arrays.asList(fieldPanel);
    }
}
