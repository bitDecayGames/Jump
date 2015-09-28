package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.BitBody;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PropModUI extends JDialog {
    private static final long serialVersionUID = 1L;
    PropModUICallback callback;
    private JScrollPane scrollPane;
    private JPanel items;

    public PropModUI(PropModUICallback callback, BitBody body) {
        super();
        scrollPane = new JScrollPane();
        items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        scrollPane.getViewport().add(items);
        this.callback = callback;
        setProperties(body);
        add(scrollPane);
        setMinimumSize(new Dimension(400, 800));
        setMaximumSize(new Dimension(400, 800));
        setPreferredSize(new Dimension(400, 800));
        pack();

        setLocation(0, 100);
        setAlwaysOnTop(true);
    }

    public void setProperties(BitBody body) {
        items.removeAll();
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(body.getClass().getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(body.getClass().getDeclaredFields()));
        fields.sort(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        boolean first = true;
        for (Field field : fields) {
            System.out.println(field.getName());
            if (!first) {
                items.add(new JSeparator(JSeparator.CENTER));
            } else {
                first = false;
            }
            try {
                ComponentBuilder builder = ComponentBuilderFactory.getBuilder(field.getType().getName());
                if (builder != null) {
                    List<JComponent> component = builder.build(field, body, callback);
                    if (component != null) {
                        JLabel label = new JLabel(field.getName());
                        items.add(label);
                        for (JComponent jComponent : component) {
                            items.add(jComponent);
                        }
                        items.add(Box.createVerticalStrut(5));
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Unable to build component for " + field.getName() + " of type " + field.getType());
            }

        }
        revalidate();
        repaint();
    }
}
