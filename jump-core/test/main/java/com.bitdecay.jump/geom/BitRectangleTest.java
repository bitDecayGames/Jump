package com.bitdecay.jump.geom;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Monday on 6/12/2016.
 */
public class BitRectangleTest {

    @Test
    public void testEquals() {
        BitRectangle rect1 = makeSimpleRectangle();
        assertTrue(rect1.equals(rect1));

        BitRectangle rect2 = makeSimpleRectangle();
        assertTrue("Same rects should be equal", rect1.equals(rect2));

        BitRectangle rect3 = new BitRectangle(0, 0, 10, 20);
        assertFalse("Different rects should not be equal", rect1.equals(rect3));

        BitRectangle rect4 = new BitRectangle(0, 0, 20, 10);
        assertFalse("Different rects should not be equal", rect1.equals(rect4));

        BitRectangle rect5 = new BitRectangle(0, 1, 10, 10);
        assertFalse("Different rects should not be equal", rect1.equals(rect5));

        BitRectangle rect6 = new BitRectangle(1, 0, 10, 10);
        assertFalse("Different rects should not be equal", rect1.equals(rect6));

        assertFalse(rect1.equals(null));
        assertFalse(rect1.equals(new BitPoint()));
    }

    @Test
    public void testConstructionRestrictions() {
        BitRectangle rect = new BitRectangle(0, 0, 10, 10);
        assertTrue(rect.xy.x == 0);
        assert(rect.xy.y == 0);
        assert(rect.getWidth() == 10);
        assert(rect.getHeight() == 10);

        BitRectangle rectNegValues = new BitRectangle(0, 0, -10, -10);
        assert(rectNegValues.xy.x == -10);
        assert(rectNegValues.xy.y == -10);
        assert(rectNegValues.width == 10);
        assert(rectNegValues.height == 10);
    }

    @Test
    public void testCreate() {
        BitRectangle base = makeSimpleRectangle();

        BitRectangle dupe = new BitRectangle(base);
        assertTrue("Copy constructor creates valid copy", base.equals(dupe));

        BitRectangle copyFunc = base.copyOf();
        assertTrue(base != copyFunc);
        assertTrue("Copy function creates valid copy", base.equals(copyFunc));

        BitRectangle cornersFloat = new BitRectangle(new BitPoint(0.0f, 0.0f), new BitPoint(10.0f, 10.0f));
        assertTrue("Float corner constructor should give same rect as float constructor", base.equals(cornersFloat));

        BitRectangle cornersInt = new BitRectangle(new BitPointInt(0, 0), new BitPointInt(10, 10));
        assertTrue("Float corner constructor should give same rect as float constructor", base.equals(cornersInt));
    }

    @Test
    public void testCenter() {
        BitRectangle rect = makeSimpleRectangle();
        assertTrue(rect.center().equals(new BitPoint(5, 5)));

        BitRectangle rect2 = new BitRectangle(-100, -100, 100, 100);
        assertTrue(rect2.center().equals(new BitPoint(-50, -50)));
    }

    @Test
    public void testSet() {
        BitRectangle rect = makeSimpleRectangle();
        BitRectangle other = new BitRectangle(-50, -50, 100, 100);

        assertFalse(rect.equals(other));

        rect.set(other);

        assertTrue(rect.equals(other));
    }

    @Test
    public void testTranslateWithInt() {
        BitRectangle rect = makeSimpleRectangle();
        float x = rect.xy.x;
        float y = rect.xy.y;

        int movement = 10;

        rect.translate(movement, movement);

       assertTrue("rect x should have moved", rect.xy.x - x == movement);
       assertTrue("rect x should have moved", rect.xy.y - y == movement);
    }

    @Test
    public void testTranslateWithFloat() {
        BitRectangle rect = makeSimpleRectangle();
        float x = rect.xy.x;
        float y = rect.xy.y;

        float movement = 50.5f;

        rect.translate(movement, movement);

       assertTrue("rect x should have moved", rect.xy.x - x == movement);
       assertTrue("rect x should have moved", rect.xy.y - y == movement);
    }

    @Test
    public void testTranslateWithPoint() {
        BitRectangle rect = makeSimpleRectangle();
        float x = rect.xy.x;
        float y = rect.xy.y;

        float movement = 10;

        rect.translate(new BitPoint(movement, movement));

        assertTrue("rect x should have moved", rect.xy.x - x == movement);
        assertTrue("rect x should have moved", rect.xy.y - y == movement);
    }

    @Test
    public void testContainsFloatCoords() {
        BitRectangle rect = makeSimpleRectangle();

        assertTrue(rect.contains(0,0));
        assertTrue(rect.contains(10,10));
        assertTrue(rect.contains(2, 8));

        assertFalse(rect.contains(-1, 5));
        assertFalse(rect.contains(10.1f, 10));
    }

    @Test
    public void testContainsPointInt() {
        BitRectangle rect = makeSimpleRectangle();

        BitPointInt inPoint = new BitPointInt(2, 8);
        assertTrue(rect.contains(inPoint));

        BitPointInt outPoint = new BitPointInt(9, 11);
        assertFalse(rect.contains(outPoint));
    }

    @Test
    public void testContainsPointFloat() {
        BitRectangle rect = makeSimpleRectangle();

        BitPoint inPoint = new BitPoint(2f, 8f);
        assertTrue(rect.contains(inPoint));

        BitPoint outPoint = new BitPoint(9f, 11f);
        assertFalse(rect.contains(outPoint));
    }

    @Test
    public void testContainsOtherRect() {
        BitRectangle rect = makeSimpleRectangle();

        BitRectangle otherSameSize = makeSimpleRectangle();
        assertTrue("Duplicate rects contain each other", rect.contains(otherSameSize));

        BitRectangle otherSmall = new BitRectangle(4, 4, 1, 1);
        assertTrue(rect.contains(otherSmall));

        BitRectangle otherLarger = new BitRectangle(0, 0, 20, 20);
        assertFalse("Does not contain larger rect", rect.contains(otherLarger));
    }

    @Test
    public void testProjectionPoints() {
        BitRectangle rect = makeSimpleRectangle();
        BitPoint[] points = rect.getProjectionPoints();
        assertEquals(points.length, 4);

        List<BitPoint> list = Arrays.asList(points);
        assertTrue(list.contains(new BitPoint(0, 0)));
        assertTrue(list.contains(new BitPoint(0, 10)));
        assertTrue(list.contains(new BitPoint(10, 10)));
        assertTrue(list.contains(new BitPoint(10, 0)));
    }

    private BitRectangle makeSimpleRectangle() {
        return new BitRectangle(0,0,10,10);
    }
}
