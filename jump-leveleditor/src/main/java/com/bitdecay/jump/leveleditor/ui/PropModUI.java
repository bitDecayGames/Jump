package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.leveleditor.ui.component.ComponentBuilder;
import com.bitdecay.jump.leveleditor.ui.component.ComponentBuilderFactory;

import javax.swing.*;
import java.awt.*;

public class PropModUI extends JDialog {
    private static final long serialVersionUID = 1L;
    PropModUICallback callback;
    private JScrollPane scrollPane;
    private JPanel items;

    public PropModUI(PropModUICallback callback, Object thing) {
        super();
        scrollPane = new JScrollPane();
        items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        scrollPane.getViewport().add(items);
        this.callback = callback;
        setProperties(thing);
        add(scrollPane);
        setMinimumSize(new Dimension(400, 800));
        setMaximumSize(new Dimension(400, 800));
        setPreferredSize(new Dimension(400, 800));
        pack();

        setLocation(0, 100);
        setAlwaysOnTop(true);
    }

    public void setProperties(Object thing) {
        items.removeAll();

        try {
            ComponentBuilder builder = ComponentBuilderFactory.getBuilder(ComponentBuilderFactory.GENERIC_BUILDER);
            for (JComponent component : builder.build(null, thing, callback, 0)) {
                items.add(component);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    public void close() {
        this.dispose();
    }
}
