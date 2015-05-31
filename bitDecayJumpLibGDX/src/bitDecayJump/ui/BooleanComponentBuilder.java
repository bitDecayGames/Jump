package bitDecayJump.ui;

import java.lang.reflect.Field;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import bitDecayJump.BitBodyProps;

public class BooleanComponentBuilder implements ComponentBuilder {

	@Override
	public List<JComponent> build(Field field, BitBodyProps props, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
		JCheckBox checkBox = new JCheckBox();
		checkBox.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					field.setBoolean(props, checkBox.isSelected());
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return Arrays.asList(checkBox);
	}

}
