package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitRectangle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Monday on 9/5/2016.
 */
public class BodyCollisionPackTest {

    @Test
    public void testFindTrueCollisions() {
        BitBody actor = new BitBody();
        actor.aabb = new BitRectangle(0, 0, 10, 10);

        BodyCollisionPack pack = new BodyCollisionPack(actor, new BitWorld());

        BitBody sus1 = new BitBody();
        sus1.aabb = new BitRectangle(9, 9, 10, 10);


        pack.suspects.add(sus1);

        pack.findTrueCollisions();

        assertEquals(1, pack.allResolutions.size());

        assertEquals(sus1, pack.allResolutions.get(0).with);
    }
}
