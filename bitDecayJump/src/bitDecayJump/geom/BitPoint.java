package bitDecayJump.geom;

public class BitPoint {
	public float x;
	public float y;

	public BitPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void add(BitPoint point) {
		x += point.x;
		y += point.y;
	}

	public BitPoint getScaled(float scale) {
		return new BitPoint(x * scale, y * scale);
	}
}
