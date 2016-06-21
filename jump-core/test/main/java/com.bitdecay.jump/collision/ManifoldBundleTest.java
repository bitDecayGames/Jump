package com.bitdecay.jump.collision;

import com.bitdecay.jump.geom.BitPoint;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 6/16/2016.
 */
public class ManifoldBundleTest {

    @Test
    public void testCandidatesMadePositive() {
        ManifoldBundle bundle = new ManifoldBundle();

        bundle.addCandidate(new Manifold(new BitPoint(10, 0), -10));

        assertTrue(bundle.getCandidates().size() == 1);
        assertTrue(bundle.getCandidates().get(0).distance == 10);
        assertTrue(bundle.getCandidates().get(0).axis.x == -10);
        assertTrue(true);
    }
}
