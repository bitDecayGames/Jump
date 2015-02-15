package bitDecayJump.ui;

import java.awt.Dimension;
import java.lang.reflect.Field;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import bitDecayJump.BitBodyProps;

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
		for (Field field : fields) {
			if (field.getType().isPrimitive()) {
				if (field.getType().equals(boolean.class)) {
					// TODO: make check box
				} else if (field.getType().equals(int.class) || field.getType().equals(float.class)) {
					JSlider slider = new JSlider(0, 1000, 0);
					slider.setMajorTickSpacing(100);
					slider.setMinorTickSpacing(25);
					slider.setPaintTicks(true);
					slider.setPaintLabels(true);
					slider.setBorder(BorderFactory.createEtchedBorder());
					try {
						if (field.getType().equals(float.class)) {
							slider.setValue((int) (field.getFloat(props) * 100));
							slider.addChangeListener(new ChangeListener() {

								@Override
								public void stateChanged(ChangeEvent e) {
									callback.propertyChanged(field.getName(), ((JSlider) e.getSource()).getValue() / 100f);
								}
							});
						} else {
							slider.setValue(field.getInt(props));
							slider.addChangeListener(new ChangeListener() {

								@Override
								public void stateChanged(ChangeEvent e) {
									callback.propertyChanged(field.getName(), ((JSlider) e.getSource()).getValue());
								}
							});
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					items.add(new JLabel(field.getName()));
					items.add(slider);
					items.add(Box.createVerticalStrut(5));
				}
			}
		}
		revalidate();
		repaint();
	}
}
