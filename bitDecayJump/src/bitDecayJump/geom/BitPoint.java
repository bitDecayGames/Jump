package bitDecayJump.geom;

public class BitPoint {
	public float x;
	public float y;

	public BitPoint(float x, float y) {
		this.x = x;
		this.y = y;
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
		return new BitPoint(x + point.x, y + point.y);
	}

	public BitPoint minus(BitPoint point) {
		return new BitPoint(x - point.x, y - point.y);
	}

	public BitPoint minus(BitPointInt point) {
		return new BitPoint(x - point.x, y - point.y);
	}

	public BitPoint getScaled(float scale) {
		return new BitPoint(x * scale, y * scale);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public BitPoint dividedBy(float divisor) {
		return new BitPoint(x / divisor, y / divisor);
	}

	public BitPoint floorDivideBy(int xDiv, int yDiv) {
		return new BitPoint(Math.floorDiv((int) Math.floor(x), xDiv), Math.floorDiv((int) Math.floor(y), yDiv));
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BitPoint) {
			return ((BitPoint) other).x == this.x && ((BitPoint) other).y == this.y;
		} else {
			return false;
		}
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
		return Math.abs(this.x - other.x) < .0001 && Math.abs(this.y - other.y) < .0001;
	}
}
