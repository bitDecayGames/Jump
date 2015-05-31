package bitDecayJump.ui;

import java.lang.reflect.Field;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import bitDecayJump.BitBodyProps;
import bitDecayJump.geom.BitPoint;

public class BitPointComponentBuilder implements ComponentBuilder {

	@Override
	public List<JComponent> build(Field field, BitBodyProps props, PropModUICallback callback) throws IllegalArgumentException, IllegalAccessException {
		BitPoint bitPoint = (BitPoint) field.get(props);
		JSlider xslider = new JSlider(-200, 200, 0);
		xslider.setMajorTickSpacing(50);
		xslider.setMinorTickSpacing(10);
		xslider.setValue((int) (bitPoint.x * 100));
		xslider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					Field field2 = bitPoint.getClass().getField("x");
					field2.set(bitPoint, ((JSlider) e.getSource()).getValue());
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//				callback.propertyChanged(field.getName(), new BitPoint(((JSlider) e.getSource()).getValue() / 100f, yslider.getValue() / 100f));
			}
		});
		xslider.setPaintTicks(true);
		xslider.setPaintLabels(true);

		JSlider yslider = new JSlider(-200, 200, 0);
		yslider.setMajorTickSpacing(50);
		yslider.setMinorTickSpacing(10);
		yslider.setValue((int) (bitPoint.y * 100));
		yslider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					Field field2 = bitPoint.getClass().getField("y");
					field2.set(bitPoint, ((JSlider) e.getSource()).getValue());
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//				callback.propertyChanged(field.getName(), new BitPoint(xslider.getValue(), ((JSlider) e.getSource()).getValue()));
			}
		});
		yslider.setPaintTicks(true);
		yslider.setPaintLabels(true);
		return Arrays.asList(new JLabel("x"), xslider, new JLabel("y"), yslider);
	}

}
