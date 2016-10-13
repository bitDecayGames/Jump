package com.bitdecay.jump.geom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Monday on 6/12/2016.
 */
public class BitPointTest {

    @Test
    public void testConstruct() {
        BitPoint point = getSimplePoint();
        BitPoint point2 = new BitPoint(point);

        assertTrue(point.equals(point2));
    }

    @Test
    public void testEquals() {
        BitPoint point = getSimplePoint();
        assertTrue(point.equals(point));

        BitPoint sameCoords = getSimplePoint();
        assertTrue(point.equals(sameCoords));

        BitPoint differentPoint = new BitPoint(1, 5);
        assertFalse(point.equals(differentPoint));

        assertFalse(point.equals(new String()));
    }

    @Test
    public void testSet() {
        BitPoint point = getSimplePoint();
        point.set(100, 100);

        assertTrue(point.x == 100);
        assertTrue(point.y == 100);

        BitPoint other = new BitPoint(15, 50);
        point.set(other);

        assertTrue(point.x == 15);
        assertTrue(point.y == 50);
    }

    @Test
    public void testAdd() {
        BitPoint point = getSimplePoint();
        float curX = point.x;
        float curY = point.y;

        float amount = 15;

        point.add(amount, amount);

        assertTrue(point.x == curX + amount);
        assertTrue(point.y == curY + amount);
    }

    @Test
    public void testAddPoint() {
        BitPoint point = getSimplePoint();
        float curX = point.x;
        float curY = point.y;

        BitPoint other = new BitPoint(15, 50);

        point.add(other);

        assertTrue(point.x == curX + other.x);
        assertTrue(point.y == curY + other.y);
    }

    @Test
    public void testPlusWithFloats() {
        BitPoint point = getSimplePoint();

        BitPoint plusPoint = point.plus(15, 50);

        assertTrue(plusPoint.x == point.x + 15);
        assertTrue(plusPoint.y == point.y + 50);
    }

    @Test
    public void testPlusWithPoint() {
        BitPoint point = getSimplePoint();
        BitPoint point2 = getSimplePoint();

        BitPoint point3 = point.plus(point2);
        assertFalse(point == point3);
        assertFalse(point2 == point3);

        assertTrue(point3.x == point.x + point2.x);
        assertTrue(point3.y == point.y + point2.y);
    }

    @Test
    public void testMinusWithFloats() {
        BitPoint point = getSimplePoint();

        BitPoint plusPoint = point.minus(15, 50);

        assertTrue(plusPoint.x == point.x - 15);
        assertTrue(plusPoint.y == point.y - 50);
    }

    @Test
    public void testMinusWithPoint() {
        BitPoint point = getSimplePoint();
        BitPoint point2 = getSimplePoint();

        BitPoint point3 = point.minus(point2);
        assertFalse(point == point3);
        assertFalse(point2 == point3);

        assertTrue(point3.x == point.x - point2.x);
        assertTrue(point3.y == point.y - point2.y);
    }

    @Test
    public void testMinusWithPointInt() {
        BitPoint point = getSimplePoint();
        BitPointInt point2 = new BitPointInt(50, 50);

        BitPoint point3 = point.minus(point2);
        assertFalse(point == point3);

        assertTrue(point3.x == point.x - point2.x);
        assertTrue(point3.y == point.y - point2.y);
    }

    @Test
    public void testScale() {
        BitPoint point = getSimplePoint();

        int amount = 5;

        BitPoint point2 = point.scale(amount);
        assertFalse(point == point2);

        assertTrue(point2.x == point.x * amount);
    }

    @Test
    public void testDivide() {
        BitPoint point = getSimplePoint();

        float divisor = 2.5f;

        BitPoint result = point.dividedBy(divisor);
        assertFalse(point == result);
        assertTrue(result.x == 4);
        assertTrue(result.y == 4);
    }

    @Test
    public void testFloorDivide() {
        BitPoint point = getSimplePoint();

        int divisor = 3;

        BitPoint result = point.floorDivideBy(divisor, divisor);
        assertFalse(point == result);
        assertTrue(result.x == 3);
        assertTrue(result.y == 3);
    }

    @Test
    public void testDotWithFloats() {
        BitPoint point = getSimplePoint();

        float dotterX = 2;

        float result = point.dot(dotterX, 0);
        assertTrue(result == 20);
    }

    @Test
    public void testDotWithPoint() {
        BitPoint point = getSimplePoint();

        BitPoint other = new BitPoint(2, 0);

        float result = point.dot(other);
        assertTrue(result == 20);
    }

    @Test
    public void testLen() {
        BitPoint point1 = new BitPoint(3,4);
        assertTrue(point1.len() == 5);

        BitPoint point2 = new BitPoint(0, 10);
        assertTrue(point2.len() == 10);

        BitPoint point3 = new BitPoint(10, 0);
        assertTrue(point3.len() == 10);
    }

    @Test
    public void testNormalize() {
        BitPoint point1 = getSimplePoint();
        BitPoint normalize = point1.normalize();
        // rounding makes this not exact
        assertTrue(1 - normalize.len() < .00001f);

        BitPoint normal2 = new BitPoint(10, 0).normalize();
        assertTrue(normal2.equals(new BitPoint(1, 0)));
    }

    @Test
    public void testShrinkPositivePoint() {
        BitPoint point = new BitPoint(10, 10);
        float amount = 1;
        BitPoint shrink = point.shrink(amount);

        assertTrue(shrink.x == point.x - amount);
        assertTrue(shrink.y == point.y - amount);
    }

    @Test
    public void testShrinkNegativePoint() {
        BitPoint point = new BitPoint(-10, -10);
        float amount = 1;
        BitPoint shrink = point.shrink(1);

        assertTrue(shrink.x == point.x + amount);
        assertTrue(shrink.y == point.y + amount);
    }

    @Test
    public void testShrinkLimitAtZero() {
        BitPoint point = new BitPoint(1,1);
        float amount = 10;
        BitPoint shrink = point.shrink(amount);

        assertTrue(shrink.x == 0);
        assertTrue(shrink.y == 0);

        point.x = -10;
        point.y = -10;
        shrink = point.shrink(amount);

        assertTrue(shrink.x == 0);
        assertTrue(shrink.y == 0);
    }

    @Test
    public void testLooseEquals() {
        BitPoint point1 = getSimplePoint();
        BitPoint point2 = new BitPoint(point1);
        assertTrue(point1.looseEquals(point2));

        point2.x += MathUtils.FLOAT_PRECISION/2;
        assertTrue(point1.looseEquals(point2));

        point2.y += MathUtils.FLOAT_PRECISION/2;
        assertTrue(point1.looseEquals(point2));

        point2.x += 0.01f;
        assertFalse(point1.looseEquals(point2));
    }

    private BitPoint getSimplePoint() {
        return new BitPoint(10, 10);
    }
}
