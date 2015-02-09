package bitDecayJump.geom;

import java.util.*;

public class GeomUtils {
	public static BitRectangle makeRect(BitPointInt start, BitPointInt end) {
		return new BitRectangle(start.x, start.y, end.x - start.x, end.y - start.y);
	}

	public static List<BitRectangle> split(BitRectangle rect, int splitWidth, int splitHeight) {
		List<BitRectangle> list = new ArrayList<BitRectangle>();
		if (rect.width >= splitWidth && rect.height >= splitHeight) {
			int xPoint = rect.xy.x;
			while (xPoint + splitWidth <= rect.xy.x + rect.width) {
				int yPoint = rect.xy.y;
				while (yPoint + splitHeight <= rect.xy.y + rect.height) {
					BitRectangle subRect = new BitRectangle(xPoint, yPoint, splitWidth, splitHeight);
					list.add(subRect);
					yPoint += splitHeight;
				}
				xPoint += splitWidth;
			}
		}
		return list;
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

	public static BitRectangle intersection(BitRectangle rec1, BitRectangle rec2) {
		int x1 = Math.max(rec1.xy.x, rec2.xy.x);
		int y1 = Math.max(rec1.xy.y, rec2.xy.y);
		int x2 = Math.min(rec1.xy.x + rec1.width, rec2.xy.x + rec2.width);
		int y2 = Math.min(rec1.xy.y + rec1.height, rec2.xy.y + rec2.height);

		if (x1 > x2 || y1 > y2) {
			return null;
		} else {
			return new BitRectangle(x1, y1, x2 - x1, y2 - y1);
		}
	}
}
