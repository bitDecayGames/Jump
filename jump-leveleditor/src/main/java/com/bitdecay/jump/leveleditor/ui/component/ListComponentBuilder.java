package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Monday on 11/8/2015.
 */
public class ListComponentBuilder implements ComponentBuilder {
    @Override
    public List<JComponent> build(Field field, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {
        List list = (List) field.get(thing);

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));

        int i = 1;
        for (Object item : list) {
            ComponentBuilder builder = ComponentBuilderFactory.getBuilder(item.getClass().getName());
            if (builder != null) {
                List<JComponent> components = builder.build(null, item, callback, depth + 1);
                JPanel subFieldPanel = new JPanel();
                subFieldPanel.setLayout(new BorderLayout());
                Color borderColor = new Color(100, 100, 100);
                for (int brightener = 0; brightener < depth; brightener++) {
                    borderColor = borderColor.brighter();
                }
                subFieldPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor, 2), item.getClass().getSimpleName() + " (index: " + i++ + ")"));

                for (JComponent jComponent : components) {
                    subFieldPanel.add(jComponent);
                }
                fieldPanel.add(subFieldPanel);
            }
        }
        return Arrays.asList(fieldPanel);
    }
}
