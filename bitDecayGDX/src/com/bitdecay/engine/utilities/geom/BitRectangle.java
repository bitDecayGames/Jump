package com.bitdecay.engine.utilities.geom;

import com.badlogic.gdx.math.*;
import com.bitdecay.engine.utilities.BitPoint;

/**
 * A wrapper for libGDX's Rectangle that forces all dimensions to be integers instead of floats. This may be confusing... if it is, we'll remove this class. Use
 * the getIntX() and getIntY() methods because it's better to use ints right now
 * 
 * @author Monday
 * 
 */
public class BitRectangle extends Rectangle {
	private static final long serialVersionUID = 1L;

	public BitRectangle(BitPoint origin, int sizeX, int sizeY) {
		super(origin.x, origin.y, sizeX, sizeY);
	}

	public BitRectangle() {
		super();
	}

	public BitRectangle(Rectangle rectangle) {
		super(rectangle);
	}

	@Override
	public boolean contains(Vector2 vector) {
		return super.contains(vector.x, vector.y);
	}

	public int getIntX() {
		return (int) super.getX();
	}

	public int getIntY() {
		return (int) super.getY();
	}

	/**
	 * Rectangles are considered equals if their position, width, and height are all equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle rect = (Rectangle) obj;
			return rect.x == this.x && rect.y == this.y && rect.width == this.width && rect.height == this.height;
		} else {
			return false;
		}
	}

	public boolean contains(BitPoint position) {
		return contains(position.x, position.y);
	}
}