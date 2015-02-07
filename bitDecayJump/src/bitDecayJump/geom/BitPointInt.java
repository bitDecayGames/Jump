package bitDecayJump.geom;

public class BitPointInt {
	public int x;
	public int y;

	public BitPointInt(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public BitPointInt add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BitPointInt) {
			return ((BitPointInt) other).x == this.x && ((BitPointInt) other).y == this.y;
		} else {
			return false;
		}
	}
}
