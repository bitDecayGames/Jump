package bitDecayJump.ui;

import java.lang.reflect.Field;

import javax.swing.*;
import javax.swing.event.*;

import bitDecayJump.BitBodyProps;

public class PropModUI extends JPanel {
	private static final long serialVersionUID = 1L;
	PropModUICallback callback;

	//	Map<JSlider, String> sliderMap;

	public PropModUI(PropModUICallback callback, BitBodyProps props) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.callback = callback;
		setProperties(props);
	}

	public void setProperties(BitBodyProps props) {
		removeAll();
		for (Field field : props.getClass().getDeclaredFields()) {
			if (field.getType().isPrimitive()) {
				if (field.getType().equals(boolean.class)) {
					// TODO: make check box
				} else if (field.getType().equals(int.class)) {
					JSlider slider = new JSlider(0, 1000, 0);
					slider.setMajorTickSpacing(100);
					slider.setMinorTickSpacing(25);
					slider.setPaintTicks(true);
					slider.setPaintLabels(true);
					slider.setBorder(BorderFactory.createEtchedBorder());
					try {
						slider.setValue(field.getInt(props));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					slider.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent e) {
							callback.propertyChanged(field.getName(), ((JSlider) e.getSource()).getValue());
						}
					});
					add(new JLabel(field.getName()));
					add(slider);
					add(Box.createVerticalStrut(5));
				}
			}
		}
		revalidate();
		repaint();
	}
}
