package bitDecayJump.ui;

import java.lang.reflect.Field;
import java.util.List;

import javax.swing.JComponent;

import bitDecayJump.BitBodyProps;

public interface ComponentBuilder {
	public List<JComponent> build(Field field, BitBodyProps props, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException;
}
