package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.BitBody;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.List;

public interface ComponentBuilder {
    public List<JComponent> build(Field field, BitBody body, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException;
}
