package com.bitdecay.jump.geom;

import com.bitdecay.jump.collision.Manifold;

import java.util.*;

public class GeomUtils {
	public static final BitPoint X_AXIS = new BitPoint(1, 0);
	public static final BitPoint NEG_X_AXIS = new BitPoint(-1, 0);

	public static final BitPoint Y_AXIS = new BitPoint(0, 1);
	public static final BitPoint NEG_Y_AXIS = new BitPoint(0, -1);

	public static final BitPoint ZERO_AXIS = new BitPoint(0, 0);

	public static final Manifold ZERO_MANIFOLD = new Manifold(ZERO_AXIS, 0);

	public static BitRectangle makeRect(BitPointInt start, BitPointInt end) {
		return new BitRectangle(start.x, start.y, end.x - start.x, end.y - start.y);
	}

	public static List<BitRectangle> split(BitRectangle rect, int splitWidth, int splitHeight) {
		List<BitRectangle> list = new ArrayList<BitRectangle>();
		if (rect.width >= splitWidth && rect.height >= splitHeight) {
			float xPoint = rect.xy.x;
			while (xPoint + splitWidth <= rect.xy.x + rect.width) {
				float yPoint = rect.xy.y;
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
	 * {@link #snap(int, int, int, int, int)}
	 */
	public static BitPointInt snap(BitPointInt coords, int snapSize) {
		return snap(coords.x, coords.y, snapSize, 0, 0);
	}

	/**
	 * Snap the xy to the nearest multiple of snapSize
	 * 
	 * @param x
	 * @param y
	 * @param snapSize
	 * @param relativeX
	 * @param relativeY
	 * @return
	 */
	public static BitPointInt snap(int x, int y, int snapSize, int relativeX, int relativeY) {
		int xSnapDir = x >= 0 ? 1 : -1;
		int ySnapDir = y >= 0 ? 1 : -1;
		int xOffset = relativeX % snapSize;
		int yOffset = relativeY % snapSize;
		int xSnap = (x + xSnapDir * snapSize / 2) / snapSize;
		xSnap *= snapSize;
		xSnap += xOffset;
		int ySnap = (y + ySnapDir * snapSize / 2) / snapSize;
		ySnap *= snapSize;
		ySnap += yOffset;

		return new BitPointInt(xSnap, ySnap);
	}

	/**
	 * @param rec1
	 * @param rec2
	 * @return a {@link BitRectangle} of the overlapping area of rec1 and rec2.
	 *         If they are not overlapping or they are just touching, this
	 *         returns null.
	 */
	public static BitRectangle intersection(BitRectangle rec1, BitRectangle rec2) {
		float x1 = Math.max(rec1.xy.x, rec2.xy.x);
		float y1 = Math.max(rec1.xy.y, rec2.xy.y);
		float x2 = Math.min(rec1.xy.x + rec1.width, rec2.xy.x + rec2.width);
		float y2 = Math.min(rec1.xy.y + rec1.height, rec2.xy.y + rec2.height);

		if (x1 >= x2 || y1 >= y2) {
			return null;
		} else {
			return new BitRectangle(x1, y1, x2 - x1, y2 - y1);
		}
	}
}
