package com.bitdecay.jump;

import java.lang.reflect.Field;

import com.bitdecay.jump.geom.BitPoint;

public class BitBodyProps {
	public BodyType bodyType = BodyType.STATIC;
	public BitPoint velocity = new BitPoint(0, 0);
	public int accelX = 0;
	public int accelY = 0;
	public int maxSpeedX = 0;
	public int maxSpeedY = 0;

	/**
	 * A flag for whether or not gravity should affect this body
	 */
	public boolean gravitational = true;

	public BitBodyProps() {
	}

	public BitBodyProps(BitBodyProps props) {
		copyProps(props);
	}

	protected void copyProps(BitBodyProps props) {
		try {
			for (Field field : BitBodyProps.class.getDeclaredFields()) {
				field.set(this, field.get(props));
			}
		} catch (Exception e) {
		}
	}

	public void set(String prop, Object value) {
		try {
			BitBodyProps.class.getDeclaredField(prop).set(this, value);
		} catch (Exception e) {
			System.out.println("Couldn't set " + prop + " to '" + value + "'");
		}
	}

	@Override
	public BitBodyProps clone() {
		return new BitBodyProps(this);
	}
}
