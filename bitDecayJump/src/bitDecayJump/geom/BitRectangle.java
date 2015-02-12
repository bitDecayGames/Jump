package bitDecayJump.geom;

public class BitRectangle {
	public BitPointInt xy;
	public float remainderX;
	public float remainderY;
	public int width;
	public int height;

	public BitRectangle(int x, int y, int width, int height) {
		xy = new BitPointInt(x, y);
		this.width = width;
		this.height = height;

		if (width < 0) {
			xy.x += width;
			this.width *= -1;
		}

		if (height < 0) {
			xy.y += height;
			this.height *= -1;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void translate(BitPoint point) {
		translate(point.x, point.y);
	}

	/**
	 * resets any axis the body is translated along (for resolving collisions)
	 * 
	 * @param point
	 * @param reset
	 */
	public void translate(BitPointInt point, boolean reset) {
		if (reset) {
			if (point.x != 0) {
				remainderX = 0;
			}
			if (point.y != 0) {
				remainderY = 0;
			}
		}
		translate(point.x, point.y);
	}

	public void translate(float x, float y) {
		float totalX = xy.x + remainderX + x;
		xy.x = (int) totalX;
		remainderX = totalX - xy.x;

		float totalY = xy.y + remainderY + y;
		xy.y = (int) totalY;
		remainderY = totalY - xy.y;
	}

	public BitPoint center() {
		return new BitPoint(xy.x + width / 2, xy.y + height / 2);
	}

	public boolean contains(BitPointInt point) {
		return contains(point.x, point.y);
	}

	public boolean contains(int x, int y) {
		return x >= xy.x && x <= xy.x + width && y >= xy.y && y <= xy.y + height;
	}

	public boolean contains(BitRectangle other) {
		return other.xy.x >= this.xy.x && other.xy.y >= this.xy.y && other.xy.x + other.width <= this.xy.x + this.width
				&& other.xy.y + other.height <= this.xy.y + this.height;
	}

	public boolean containsInternal(BitPointInt point) {
		int x = point.x;
		int y = point.y;
		return x > xy.x && x < xy.x + width && y > xy.y && y < xy.y + height;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BitRectangle other = (BitRectangle) obj;
		if (height != other.height) {
			return false;
		}
		if (width != other.width) {
			return false;
		}
		if (xy == null) {
			if (other.xy != null) {
				return false;
			}
		} else if (!xy.equals(other.xy)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "(x: " + xy.x + ", y: " + xy.y + " - w: " + width + ", h: " + height + ")";
	}
}
