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
		remainderX += point.x;
		xy.x += remainderX;
		remainderX = remainderX - (int) remainderX;

		remainderY += point.y;
		xy.y += remainderY;
		remainderY = remainderY - (int) remainderY;
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
}
