package com.bitdecay.jump.geom;

import com.bitdecay.jump.collision.Manifold;

import java.util.ArrayList;
import java.util.List;

public class GeomUtils {
	public static final BitPoint X_AXIS = new BitPoint(1, 0);
	public static final BitPoint NEG_X_AXIS = new BitPoint(-1, 0);

	public static final BitPoint Y_AXIS = new BitPoint(0, 1);
	public static final BitPoint NEG_Y_AXIS = new BitPoint(0, -1);

	public static final BitPoint ZERO_AXIS = new BitPoint(0, 0);

	public static final Manifold ZERO_MANIFOLD = new Manifold(ZERO_AXIS, 0);

	/**
	 * Given a rectangle
	 * @param rect
	 * @param splitWidth
	 * @param splitHeight
	 * @return
	 */
	public static List<BitRectangle> splitRect(BitRectangle rect, int splitWidth, int splitHeight) {
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
	 * Snap the a pair of ints to the nearest multiple of snapSize. The relative coordinates
	 * allow for the snapping result to be offset based on the relative origin.<br>
	 * <br>
	 * Examples:<br>
	 * <br>
	 * 10 with a snap of 50 will snap to 0<br>
	 * 26 with a snap of 50 will snap to 50<br>
	 * -10 with a snap of 50 will snap to 0<br>
	 * -26 with a snap of 50 will snap to -50<br>
	 * <br>
	 * Examples of relative coordinates:<br>
	 * <br>
	 * 7 with a snap of 10 and a relative of 5 will result in 15
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
	 * Snaps a point to the nearest multiple of the given snap size.
	 * @see GeomUtils#snap(int, int, int, int, int)
	 * @param coords
	 * @param snapSize
	 * @return
	 */
	public static BitPointInt snap(BitPointInt coords, int snapSize) {
		return snap(coords.x, coords.y, snapSize, 0, 0);
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

	/**
	 * Rotates a list of points counterclickby the given angle around the origin.
	 * @see GeomUtils#rotatePoint(BitPoint, float, BitPoint)
	 * @param points
	 * @param angle
	 * @return A list of points corresponding to the rotated version of the provided points
	 */
	public static List<BitPoint> rotatePoints(List<BitPoint> points, float angle) {
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		for (BitPoint point : points) {
			minX = Math.min(point.x, minX);
			maxX = Math.max(point.x, maxX);
			minY = Math.min(point.y, minY);
			maxY = Math.max(point.y, maxY);
		}
		List<BitPoint> newPoints = new ArrayList<>();
		for (BitPoint point : points) {
			newPoints.add(rotatePoint(new BitPoint(point), angle, GeomUtils.ZERO_AXIS));
		}
		return newPoints;
	}

	/**
	 * Given a working point and a reference point, rotate the working point by
	 * the given angle around the reference point.
	 * @param p
	 * @param angle
	 * @param around
	 * @return
	 */
	private static BitPoint rotatePoint(BitPoint p, float angle, BitPoint around)
	{
		double s = Math.sin(angle);
		double c = Math.cos(angle);

		// translate point back to origin:
		p.x -= around.x;
		p.y -= around.y;

		// rotate point
		double xnew = p.x * c - p.y * s;
		double ynew = p.x * s + p.y * c;

		// translate point back:
		p.x = (float) (xnew + around.x);
		p.y = (float) (ynew + around.y);
		return p;
	}

	/**
	 * Converts a list of points to an array of floats in the format of
	 * [x1, y1, x2, y2 ... xn, yn]
	 * @param points
	 * @return float of arrays
	 */
	public static float[] pointsToFloats(List<BitPoint> points) {
		float[] floats  = new float[points.size() * 2];
		for (int i = 0; i < points.size(); i++) {
			floats[i*2] = points.get(i).x;
			floats[i*2+1] = points.get(i).y;
		}
		return floats;
	}

	/**
	 * Translate each point in a list by the x and y of the given translation point
	 * @param bitPoints
	 * @param translation
	 * @return bitPoints with each point having been translated. Useful for chaining.
	 */
	public static List<BitPoint> translatePoints(List<BitPoint> bitPoints, BitPoint translation) {
		bitPoints.forEach(point -> {
			point.x += translation.x;
			point.y += translation.y;
		});
		return bitPoints;
	}

	public static float angle(BitPoint p1, BitPoint p2) {
		double xDiff = p2.x - p1.x;
		double yDiff = p2.y - p1.y;
		return (float) Math.atan2(yDiff, xDiff);
	}
}
