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

	public void add(int x, int y) {
		this.x += x;
		this.y += y;
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

	public BitPointInt floorDivideBy(int xDiv, int yDiv) {
		return new BitPointInt(Math.floorDiv(x, xDiv), Math.floorDiv(y, yDiv));
	}

	public BitPointInt minus(BitPointInt point) {
		return new BitPointInt(x - point.x, y - point.y);
	}

	public void addAndReset(int x, int y) {
		add(x, y);
	}
}
