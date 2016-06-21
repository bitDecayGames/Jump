package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
/**
 * Created by Monday on 6/20/2016.
 */
public class SATStrategyComparatorTest {
    @Test
    public void testCompare() {
        BitBody body = new BitBody();
        BitBody kineticBody = new BitBody();
        kineticBody.bodyType = BodyType.KINETIC;

        BitBody staticBody = new BitBody();
        staticBody.bodyType = BodyType.STATIC;

        SATStrategy strat1 = new SATStrategy(body);
        strat1.potentialCollisions.add(new BitCollision(body, kineticBody));

        SATStrategy strat2 = new SATStrategy(body);
        strat2.potentialCollisions.add(new BitCollision(body, staticBody));

        SATStrategyComparator compare = new SATStrategyComparator();

        assertTrue(compare.compare(strat1, strat2) > 0);
    }
}
