package bitDecayJump.ui;

import java.lang.reflect.Field;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import bitDecayJump.BitBodyProps;

public class FloatComponentBuilder implements ComponentBuilder {
	@Override
	public List<JComponent> build(Field field, BitBodyProps props, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
		JSlider slider = new JSlider(0, 100, 0);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		for (int i = 0; i <= 10; i++) {
			labelTable.put(i * 10, new JLabel(Integer.toString(i)));
		}
		slider.setLabelTable(labelTable);
		slider.setMajorTickSpacing(1);
		slider.setValue((int) (field.getFloat(props) * 100));
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					field.set(props, ((JSlider) e.getSource()).getValue() / 100f);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		return Arrays.asList(slider);
	}
}
