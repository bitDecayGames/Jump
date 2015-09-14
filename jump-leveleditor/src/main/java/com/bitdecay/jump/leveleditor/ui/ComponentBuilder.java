package com.bitdecay.jump.leveleditor.ui;

import com.bitdecay.jump.BitBodyProps;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.List;

public interface ComponentBuilder {
    public List<JComponent> build(Field field, BitBodyProps props, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException;
}
