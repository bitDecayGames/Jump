package bitDecayJump.geom;

public class GeomUtils {
	public static BitRectangle makeRect(BitPointInt start, BitPointInt end) {
		return new BitRectangle(start.x, start.y, end.x - start.x, end.y - start.y);
	}

	/**
	 * {@link #snap(int, int, int)}
	 */
	public static BitPointInt snap(BitPointInt coords, int snapSize) {
		return snap(coords.x, coords.y, snapSize);
	}

	/**
	 * Snap the xy to the nearest multiple of snapSize
	 * 
	 * @param x
	 * @param y
	 * @param snapSize
	 * @return
	 */
	public static BitPointInt snap(int x, int y, int snapSize) {
		int xSnap = (x + snapSize / 2) / snapSize;
		xSnap *= snapSize;
		int ySnap = (y + snapSize / 2) / snapSize;
		ySnap *= snapSize;

		return new BitPointInt(xSnap, ySnap);
	}
}
