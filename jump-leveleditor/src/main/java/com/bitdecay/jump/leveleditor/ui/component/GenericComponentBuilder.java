package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Monday on 10/27/2015.
 */
public class GenericComponentBuilder implements ComponentBuilder {
    @Override
    public List<JComponent> build(Field narrowingField, Object thing, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
        if (narrowingField != null) {
            thing = narrowingField.get(thing);
        }
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(thing.getClass().getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(thing.getClass().getDeclaredFields()));
        fields.sort(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        boolean first = true;
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            System.out.println(field.getName());
            if (!first) {
                fieldPanel.add(new JSeparator(JSeparator.CENTER));
            } else {
                first = false;
            }
            try {
                ComponentBuilder builder = ComponentBuilderFactory.getBuilder(field.getType().getName());
                if (builder != null) {
                    List<JComponent> component = builder.build(field, thing, callback);
                    if (component != null) {
                        JLabel label = new JLabel(field.getName());
                        fieldPanel.add(label);
                        for (JComponent jComponent : component) {
                            fieldPanel.add(jComponent);
                        }
                        fieldPanel.add(Box.createVerticalStrut(5));
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Unable to build component for " + field.getName() + " of type " + field.getType());
            }
        }
        return Arrays.asList(fieldPanel);
    }
}
