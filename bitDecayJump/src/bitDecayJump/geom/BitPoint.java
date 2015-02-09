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

	public BitPoint getScaled(float scale) {
		return new BitPoint(x * scale, y * scale);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
