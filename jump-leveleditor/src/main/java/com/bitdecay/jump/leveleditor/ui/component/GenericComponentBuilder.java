package com.bitdecay.jump.leveleditor.ui.component;

import com.bitdecay.jump.annotation.CanLoadFromFile;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.level.FileUtils;
import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Monday on 10/27/2015.
 */
public class GenericComponentBuilder implements ComponentBuilder {
    @Override
    public List<JComponent> build(Field narrowingField, Object thing, PropModUICallback callback, int depth) throws IllegalArgumentException, IllegalAccessException {
        boolean canLoadFromFile = false;
        final Object inspectedObject;
        if (narrowingField != null) {
            inspectedObject = narrowingField.get(thing);
            if (narrowingField.isAnnotationPresent(CanLoadFromFile.class)) {
                canLoadFromFile = true;
            }
        } else {
            inspectedObject = thing;
        }
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(inspectedObject.getClass().getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(inspectedObject.getClass().getDeclaredFields()));

        if (canLoadFromFile) {
            JPanel filePanel = new JPanel();
            filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
            JButton loadBtn = new JButton();
            loadBtn.setText("Load from file");
            filePanel.add(loadBtn);
            loadBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object loaded = FileUtils.loadFileAs(narrowingField.getType());
                    if (loaded != null) {
                        try {
                            narrowingField.set(thing, loaded);
                            fieldPanel.removeAll();
                            build(narrowingField, thing, callback, depth).forEach(component -> fieldPanel.add(component));
                            fieldPanel.revalidate();
                            callback.propertyChanged("Loaded From file", thing);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });

            JButton saveBtn = new JButton();
            saveBtn.setText("Save to file");
            filePanel.add(saveBtn);
            saveBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FileUtils.saveToFile(inspectedObject);
                }
            });

            fieldPanel.add(filePanel);
        }

        boolean first = true;
        for (Field field : fields) {
            if (field.isAnnotationPresent(CantInspect.class)) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (!first) {
                fieldPanel.add(new JSeparator(JSeparator.CENTER));
            } else {
                first = false;
            }
            try {
                ComponentBuilder builder = ComponentBuilderFactory.getBuilder(field.getType().getSimpleName());
                if (builder != null) {
                    List<JComponent> components = builder.build(field, inspectedObject, callback, depth + 1);
                    if (components != null && components.size() > 0) {
                        JPanel subFieldPanel = new JPanel();
                        subFieldPanel.setLayout(new BorderLayout());
                        Color borderColor = new Color(100, 100, 100);
                        for (int i = 0; i < depth; i++) {
                            borderColor = borderColor.brighter();
                        }
                        subFieldPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor, 2), field.getName()));

                        for (JComponent jComponent : components) {
                            subFieldPanel.add(jComponent);
                        }
                        fieldPanel.add(subFieldPanel);
                        fieldPanel.add(Box.createVerticalStrut(5));
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Unable to build component for " + field.getName() + " of type " + field.getType());
                e.printStackTrace();
            }
        }
        return Arrays.asList(fieldPanel);
    }
}
