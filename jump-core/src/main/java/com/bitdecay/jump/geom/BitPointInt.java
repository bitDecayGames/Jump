package com.bitdecay.jump.geom;

public class BitPointInt {
	public int x;
	public int y;

	public BitPointInt() {
		// Here for JSON
	}

	public BitPointInt(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public BitPointInt plus(int x, int y) {
		return new BitPointInt(this.x + x, this.y + y);
	}

	public BitPointInt plus(BitPointInt point) {
		return plus(point.x, point.y);
	}

	public BitPointInt minus(int x, int y) {
		return new BitPointInt(this.x - x, this.y - y);
	}

	public BitPointInt minus(BitPointInt point) {
		return minus(point.x, point.y);
	}

	public BitPointInt floorDivideBy(int xDiv, int yDiv) {
		return new BitPointInt(Math.floorDiv(x, xDiv), Math.floorDiv(y, yDiv));
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void set(BitPointInt other) {
		set(other.x, other.y);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BitPointInt) {
			return ((BitPointInt) other).x == this.x && ((BitPointInt) other).y == this.y;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
