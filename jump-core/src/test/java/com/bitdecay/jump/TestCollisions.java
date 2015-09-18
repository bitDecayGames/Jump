package com.bitdecay.jump;

import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.Level;
import org.junit.Test;

public class TestCollisions {

    @Test
    public void testStaticBoxToDynamicBoxCollision(){
        BitWorld world = new BitWorld();
        Level l = new Level(1);
        l.gridOffset = new BitPointInt(0, 0);
        world.setLevel(l);
        world.setGravity(0, -1);

        BitBody staticBody = new BitBody();
        staticBody.props = new BitBodyProps();
        staticBody.props.bodyType = BodyType.STATIC;
        staticBody.aabb = new BitRectangle(0, 0, 10, 10);
        world.addBody(staticBody);

        BitBody dynamicBody = new BitBody();
        dynamicBody.props = new BitBodyProps();
        dynamicBody.props.bodyType = BodyType.DYNAMIC;
        dynamicBody.aabb = new BitRectangle(0, 20, 10, 10);
        world.addBody(dynamicBody);

        // TODO: figure out how to listen to collisions

        for (int i = 0; i < 1000; i++){
            world.step(0.1f);
        }

        // TODO: assert that the collision happened
    }
}
