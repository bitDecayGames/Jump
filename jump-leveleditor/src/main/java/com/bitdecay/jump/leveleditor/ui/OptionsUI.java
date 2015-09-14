package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.leveleditor.setup.EditorObject;
import com.bitdecay.jump.leveleditor.setup.EditorToolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

public class OptionsUI extends JDialog {
    private static final long serialVersionUID = 1L;
    OptionsUICallback callback;
    private JPanel items;
    private EditorToolbox toolBox;

    public OptionsUI(OptionsUICallback callback, EditorToolbox toolBox) {
        super();
        this.toolBox = toolBox;
        items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        this.callback = callback;

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        int group = 0;
        for (OptionsMode option : OptionsMode.values()) {
            if (option.group == -1) {
                addMenuItem(fileMenu, option);
            } else {
                if (option.group != group) {
                    group = option.group;
                    items.add(Box.createVerticalStrut(50));
                } else {
                    items.add(Box.createVerticalStrut(10));
                }

                addButton(option);
            }
        }

        for (Entry<Image, Class<? extends EditorObject>> uiThing : toolBox.tiles.entrySet()) {
            items.add(new JButton(new ImageIcon(uiThing.getKey())));
        }
        for (Entry<Image, Class<? extends EditorObject>> uiThing : toolBox.pathedObject.entrySet()) {
            items.add(new JButton(new ImageIcon(uiThing.getKey())));
        }
        for (Entry<Image, Class<? extends EditorObject>> uiThing : toolBox.placableObject.entrySet()) {
            items.add(new JButton(new ImageIcon(uiThing.getKey())));
        }

        add(items);
        pack();

        setLocation(0, 0);
        setAlwaysOnTop(true);
    }

    private void addMenuItem(JMenu menu, OptionsMode option) {
        JMenuItem item = new JMenuItem(option.label);
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                callback.setMode(option);
            }
        });
        menu.add(item);
    }

    private void addButton(OptionsMode mode) {
        JButton btnLoad = new JButton(mode.label);
        btnLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                callback.setMode(mode);
            }
        });
        items.add(btnLoad);
    }
}
