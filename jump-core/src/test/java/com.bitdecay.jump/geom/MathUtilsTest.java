package com.bitdecay.jump.geom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Monday on 6/12/2016.
 */
public class MathUtilsTest {

    @Test
    public void testClose() {
        assertTrue(MathUtils.close(0.5f, .50001f));
        assertTrue(MathUtils.close(0f, -0.00001f));
    }

    @Test
    public void testWithin() {
        assertTrue(MathUtils.within(1, 1, 0));
        assertTrue(MathUtils.within(1, 0, 1));

        assertFalse(MathUtils.within(1f, 1.1f, 0.05f));
        assertFalse(MathUtils.within(4.9f, 5f, MathUtils.FLOAT_PRECISION));
    }

    @Test
    public void testSameSign() {
        assertTrue(MathUtils.sameSign(1, 100));

        assertTrue(MathUtils.sameSign(100, 0));

        assertTrue(MathUtils.sameSign(-1, -100));
    }

    @Test
    public void testOpposing() {
        assertTrue(MathUtils.opposing(-1, 1));
        assertFalse(MathUtils.opposing(10, 100));
        assertFalse(MathUtils.opposing(1, 0));
        assertFalse(MathUtils.opposing(0, 1));
    }
}
