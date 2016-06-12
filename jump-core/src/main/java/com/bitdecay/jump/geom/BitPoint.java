package com.bitdecay.jump.geom;

public class BitPoint {
	public float x;
	public float y;

	public BitPoint() {
		// Here for JSON
	}

	public BitPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public BitPoint(BitPoint other) {
		this(other.x, other.y);
	}


	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(BitPoint other) {
		set(other.x, other.y);
	}

	/**
	 * adds point's values to this object
	 *
	 * @param point
	 */
	public void add(BitPoint point) {
		add(point.x, point.y);
	}

	public void add(float x, float y) {
		this.x += x;
		this.y += y;
	}

	/**
	 * returns the total of this plus point
	 *
	 * @param point
	 * @return result
	 */
	public BitPoint plus(BitPoint point) {
		return plus(point.x,point.y);
	}

	public BitPoint plus(float x, float y) {
		return new BitPoint(this.x + x, this.y + y);
	}

	public BitPoint minus(BitPoint point) {
		return minus(point.x, point.y);
	}

	public BitPoint minus(float x, float y) {
		return new BitPoint(this.x - x, this.y - y);
	}


	public BitPoint minus(BitPointInt point) {
		return minus(point.x, point.y);
	}

	public BitPoint scale(float scale) {
		return new BitPoint(x * scale, y * scale);
	}

	public BitPoint dividedBy(float divisor) {
		return new BitPoint(x / divisor, y / divisor);
	}

	public BitPoint floorDivideBy(int xDiv, int yDiv) {
		return new BitPoint(Math.floorDiv((int) Math.floor(x), xDiv), Math.floorDiv((int) Math.floor(y), yDiv));
	}

	public float dot(float ox, float oy) {
		return x * ox + y * oy;
	}

	public float dot(BitPoint other) {
		return dot(other.x, other.y);
	}

	public float len() {
		return (float)Math.sqrt(x * x + y * y);
	}

	public BitPoint normalize() {
		BitPoint normal = new BitPoint(this);
		float len = normal.len();
		if (len != 0) {
			normal.x /= len;
			normal.y /= len;
		}
		return normal;
	}

	/**
	 * Allows for being within 1/10,000th tolerance before being considered not
	 * equal.
	 *
	 * @param other
	 * @return true if the other point is within 1/10,000th in both the x and y
	 *         coordinate
	 */
	public boolean looseEquals(BitPoint other) {
		return MathUtils.close(this.x, other.x) && MathUtils.close(this.y, other.y);
	}

	/**
	 * Pulls a point towards zero. Will stop at zero if attempting to shrink
	 * past zero
	 *
	 * @param amount
	 *            how far to shrink the point
	 * @return
	 */
	public BitPoint shrink(float amount) {
		BitPoint result = new BitPoint(this);
		if (result.x > 0) {
			result.x = Math.max(0, x - amount);
		} else if (result.x < 0) {
			result.x = Math.min(0, x + amount);
		}

		if (result.y > 0) {
			result.y = Math.max(0, y - amount);
		} else if (result.y < 0) {
			result.y = Math.min(0, y + amount);
		}
		return result;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BitPoint) {
			return ((BitPoint) other).x == this.x && ((BitPoint) other).y == this.y;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
		result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
		return result;
	}
}
