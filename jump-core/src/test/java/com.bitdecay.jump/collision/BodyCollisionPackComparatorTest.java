package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Monday on 9/5/2016.
 */
public class BodyCollisionPackComparatorTest {
    @Test
    public void testCompare() {
        BitBody body1 = new BitBody();
        BitBody body2 = new BitBody();

        BitBody kineticBody = new BitBody();
        kineticBody.bodyType = BodyType.KINETIC;

        BitBody staticBody = new BitBody();
        staticBody.bodyType = BodyType.STATIC;

        BodyCollisionPack pack1 = new BodyCollisionPack(body1, new BitWorld());
        pack1.suspects.add(kineticBody);

        BodyCollisionPack pack2 = new BodyCollisionPack(body2, new BitWorld());
        pack2.suspects.add(staticBody);

        BodyCollisionPackComparator comparator = new BodyCollisionPackComparator();

        assertEquals(1, comparator.compare(pack1, pack2));
        assertEquals(-1, comparator.compare(pack2, pack1));
        assertEquals(0, comparator.compare(pack1, pack1));
    }
}
