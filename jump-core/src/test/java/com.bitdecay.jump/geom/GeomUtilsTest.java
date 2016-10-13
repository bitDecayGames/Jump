package com.bitdecay.jump.geom;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 6/12/2016.
 */
public class GeomUtilsTest {
    @Test
    public void testSplitRect() {
        BitRectangle startRect = new BitRectangle(0, 0, 10, 10);

        List<BitRectangle> splits = GeomUtils.splitRect(startRect, 5, 5);
        assertTrue(splits.size() == 4);

        assertTrue(splits.get(0).xy.equals(new BitPoint(0, 0)));
        assertTrue(splits.get(0).getWidth() == 5);
        assertTrue(splits.get(0).getHeight() == 5);

        assertTrue(splits.get(1).xy.equals(new BitPoint(0, 5)));
        assertTrue(splits.get(1).getWidth() == 5);
        assertTrue(splits.get(1).getHeight() == 5);

        assertTrue(splits.get(2).xy.equals(new BitPoint(5, 0)));
        assertTrue(splits.get(2).getWidth() == 5);
        assertTrue(splits.get(2).getHeight() == 5);

        assertTrue(splits.get(3).xy.equals(new BitPoint(5, 5)));
        assertTrue(splits.get(3).getWidth() == 5);
        assertTrue(splits.get(3).getHeight() == 5);
    }

    @Test
    public void testSplitUneven() {
        BitRectangle startRect = new BitRectangle(0, 0, 10, 10);

        List<BitRectangle> splits = GeomUtils.splitRect(startRect, 4, 4);
        assertTrue(splits.size() == 4);

        assertTrue(splits.get(0).xy.equals(new BitPoint(0, 0)));
        assertTrue(splits.get(0).getWidth() == 4);
        assertTrue(splits.get(0).getHeight() == 4);

        assertTrue(splits.get(1).xy.equals(new BitPoint(0, 4)));
        assertTrue(splits.get(1).getWidth() == 4);
        assertTrue(splits.get(1).getHeight() == 4);

        assertTrue(splits.get(2).xy.equals(new BitPoint(4, 0)));
        assertTrue(splits.get(2).getWidth() == 4);
        assertTrue(splits.get(2).getHeight() == 4);

        assertTrue(splits.get(3).xy.equals(new BitPoint(4, 4)));
        assertTrue(splits.get(3).getWidth() == 4);
        assertTrue(splits.get(3).getHeight() == 4);

        splits = GeomUtils.splitRect(startRect, 9, 9);
        assertTrue(splits.size() == 1);
        assertTrue(splits.get(0).xy.x == 0);
        assertTrue(splits.get(0).xy.y == 0);
        assertTrue(splits.get(0).getHeight() == 9);
        assertTrue(splits.get(0).getWidth() == 9);
    }

    @Test
    public void testSplitOversize() {
        BitRectangle startRect = new BitRectangle(0, 0, 10, 10);

        List<BitRectangle> splits = GeomUtils.splitRect(startRect, 20, 20);
        assertTrue(splits.size() == 0);
    }

    @Test
    public void testSnapInts() {
        BitPointInt snap = GeomUtils.snap(76, 76, 50, 0, 0);
        assertTrue(snap.x == 100);
        assertTrue(snap.y == 100);

        snap = GeomUtils.snap(101, 101, 50, 0, 0);

        assertTrue(snap.x == 100);
        assertTrue(snap.y == 100);

        snap = GeomUtils.snap(-76, -76, 50, 0, 0);
        assertTrue(snap.x == -100);
        assertTrue(snap.y == -100);


        snap = GeomUtils.snap(-101, -101, 50, 0, 0);
        assertTrue(snap.x == -100);
        assertTrue(snap.y == -100);
    }

    @Test
    public void testSnapIntsWithRelative() {
        BitPointInt snap = GeomUtils.snap(7, 7, 10, 5, 5);
        assertTrue(snap.x == 15);
        assertTrue(snap.y == 15);
    }

    @Test
    public void testSnapPoint() {
        BitPointInt start = new BitPointInt(76, 76);

        BitPointInt snapPoint = GeomUtils.snap(start, 50);
        assertTrue(snapPoint.x == 100);
        assertTrue(snapPoint.y == 100);

        start = new BitPointInt(101, 101);

        snapPoint = GeomUtils.snap(start, 50);
        assertTrue(snapPoint.x == 100);
        assertTrue(snapPoint.y == 100);

        start = new BitPointInt(-76, -76);

        snapPoint = GeomUtils.snap(start, 50);
        assertTrue(snapPoint.x == -100);
        assertTrue(snapPoint.y == -100);

        start = new BitPointInt(-101, -101);

        snapPoint = GeomUtils.snap(start, 50);
        assertTrue(snapPoint.x == -100);
        assertTrue(snapPoint.y == -100);
    }

    @Test
    public void testIntersectionBasic() {
        BitRectangle first = new BitRectangle(0, 0, 10, 10);
        BitRectangle second = new BitRectangle(-5, -5, 10, 10);

        // partial overlap
        BitRectangle insec = GeomUtils.intersection(first, second);
        assertTrue(insec.xy.x == 0);
        assertTrue(insec.xy.y == 0);
        assertTrue(insec.getWidth() == 5);
        assertTrue(insec.getHeight() == 5);

        BitRectangle third = new BitRectangle(0, 10, 10, 10);

        // touching
        insec = GeomUtils.intersection(first, third);
        assertTrue(insec == null);

        BitRectangle fourth = new BitRectangle(0, 20, 10, 10);

        // entirely separate
        insec = GeomUtils.intersection(first, fourth);
        assertTrue(insec == null);
    }

    @Test
    public void testIntersectionCover() {
        BitRectangle small = new BitRectangle(0, 0, 10, 10);
        BitRectangle large = new BitRectangle(-5, -5, 20, 20);

        // small is entirely inside large
        BitRectangle insec = GeomUtils.intersection(small, large);
        assertTrue(insec.equals(small));

        insec = GeomUtils.intersection(large, small);
        assertTrue(insec.equals(small));
    }

    @Test
    public void testIntersectionNone() {
        BitRectangle left = new BitRectangle(0, 0, 10, 10);
        BitRectangle right = new BitRectangle(20, 0, 10, 10);

        assertTrue(GeomUtils.intersection(left, right) == null);
    }

    @Test
    public void testNotTouching() {
        BitRectangle left = new BitRectangle(0, 0, 10, 10);
        BitRectangle right = new BitRectangle(20, 0, 10, 10);

        assertFalse(GeomUtils.areRectsTouching(left, right));
    }

    @Test
    public void testTouchingWithOverlap() {
        BitRectangle left = new BitRectangle(0, 0, 10, 10);
        BitRectangle right = new BitRectangle(5, 3, 10, 10);

        assertTrue(GeomUtils.areRectsTouching(left, right));
    }

    @Test
    public void testTouchingSharedEdge() {
        BitRectangle left = new BitRectangle(0, 0, 10, 10);
        BitRectangle right = new BitRectangle(10, 5, 10, 10);

        assertTrue(GeomUtils.areRectsTouching(left, right));
        assertTrue(GeomUtils.areRectsTouching(right, left));

        BitRectangle top = new BitRectangle(0, 0, 10, 10);
        BitRectangle bottom = new BitRectangle(5, 10, 10, 10);

        assertTrue(GeomUtils.areRectsTouching(top, bottom));
        assertTrue(GeomUtils.areRectsTouching(bottom, top));
    }

    @Test
    public void testTouchingWithin() {
        BitRectangle left = new BitRectangle(0, 0, 10, 10);
        BitRectangle right = new BitRectangle(5, 2, 0, 6);

        assertTrue(GeomUtils.areRectsTouching(left, right));
    }

    @Test
    public void testPointsToFloats() {
        List<BitPoint> points = Arrays.asList(new BitPoint(0, 0),
                new BitPoint(5, 5), new BitPoint(10, 10), new BitPoint(15, 15));
        float[] floats = GeomUtils.pointsToFloats(points);
        assertTrue(floats[0] == 0);
        assertTrue(floats[1] == 0);

        assertTrue(floats[2] == 5);
        assertTrue(floats[3] == 5);

        assertTrue(floats[4] == 10);
        assertTrue(floats[5] == 10);

        assertTrue(floats[6] == 15);
        assertTrue(floats[7] == 15);
    }

    @Test
    public void testRotatePoints() {
        List<BitPoint> points = Arrays.asList(new BitPoint(0, 0),
                new BitPoint(5, 0), new BitPoint(0, 5));

        List<BitPoint> rotated = GeomUtils.rotatePoints(points, MathUtils.PI_OVER_TWO);
        assertFalse(points == rotated);

        assertTrue(MathUtils.close(rotated.get(0).x, 0));
        assertTrue(MathUtils.close(rotated.get(0).y, 0));

        assertTrue(MathUtils.close(rotated.get(1).x, 0));
        assertTrue(MathUtils.close(rotated.get(1).y, 5));

        assertTrue(MathUtils.close(rotated.get(2).x, -5));
        assertTrue(MathUtils.close(rotated.get(2).y, 0));
    }

    @Test
    public void testTranslatePoints() {
        List<BitPoint> points = Arrays.asList(new BitPoint(0, 0),
                new BitPoint(5, 5), new BitPoint(10, 10), new BitPoint(15, 15));

        List<BitPoint> returned = GeomUtils.translatePoints(points, new BitPoint(-5, -5));
        assertTrue(points == returned);
        assertTrue(points.get(0).x == -5);
        assertTrue(points.get(0).y == -5);

        assertTrue(points.get(1).x == 0);
        assertTrue(points.get(1).y == 0);

        assertTrue(points.get(2).x == 5);
        assertTrue(points.get(2).y == 5);

        assertTrue(points.get(3).x == 10);
        assertTrue(points.get(3).y == 10);
    }

    @Test
    public void testAngle() {
        BitPoint vec1 = new BitPoint(1, 1);
        BitPoint vec2 = new BitPoint(1, -1);

        assertTrue(MathUtils.close(GeomUtils.angle(vec1, vec2), -MathUtils.PI_OVER_TWO));
    }
}
