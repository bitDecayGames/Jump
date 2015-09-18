package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.BitBodyProps;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropModUI extends JDialog {
    private static final long serialVersionUID = 1L;
    PropModUICallback callback;
    private JPanel items;

    public PropModUI(PropModUICallback callback, BitBodyProps props) {
        super();
        items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        this.callback = callback;
        setProperties(props);
        add(items);
        pack();
        setMinimumSize(new Dimension(400, (int) getSize().getHeight()));
        setMaximumSize(new Dimension(400, (int) getSize().getHeight()));
        setPreferredSize(new Dimension(400, (int) getSize().getHeight()));

        setLocation(0, 100);
        setAlwaysOnTop(true);
    }

    public void setProperties(BitBodyProps props) {
        items.removeAll();
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(props.getClass().getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(props.getClass().getDeclaredFields()));
        boolean first = true;
        for (Field field : fields) {
            if (!first) {
                items.add(new JSeparator(JSeparator.CENTER));
            } else {
                first = false;
            }
            try {
                ComponentBuilder builder = ComponentBuilderFactory.getBuilder(field.getType().getName());
                if (builder != null) {
                    List<JComponent> component = builder.build(field, props, callback);
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
