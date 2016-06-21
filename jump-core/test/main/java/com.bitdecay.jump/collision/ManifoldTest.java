package com.bitdecay.jump.collision;

import com.bitdecay.jump.geom.BitPoint;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 6/16/2016.
 */
public class ManifoldTest {

    @Test
    public void testResultIsScaled() {
        BitPoint axis = new BitPoint(1, 0);
        Manifold man = new Manifold(axis, 5);
        assertTrue(man.axis.equals(axis));
        assertTrue(man.distance == 5);
        assertTrue(man.result.x == 5);
        assertTrue(man.result.y == 0);
    }
}
