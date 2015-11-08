package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.BitBodyProperties;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 10/25/2015.
 */
public class DebugSpawnObject extends LevelObject{
    public static final int INNER_DIAMETER = 4;
    public static final int OUTER_DIAMETER = 8;

    public BitBodyProperties props;
    public JumperProperties jumpProps;

    public DebugSpawnObject(BitPointInt point) {
        super(new BitRectangle(point, point));

        props = new BitBodyProperties();
        props.airAcceleration.set(600, 0);
        props.airDeceleration.set(300, 0);
        props.acceleration.set(600, 0);
        props.deceleration.set(300, 0);

        jumpProps = new JumperProperties();
        jumpProps.jumpCount = 2;
        jumpProps.jumpStrength = 300;
        jumpProps.jumpDoubleJumpStrength = 150;
        jumpProps.jumpVariableHeightWindow = .2f;
        jumpProps.jumpGraceWindow = .2f;
        jumpProps.jumpHittingHeadStopsJump = true;
        jumpProps.wallJumpEnabled = true;
        jumpProps.wallSlideEnabled = true;
    }

    /**
     * A debugSpawn object doesn't have a body
     * @return
     */
    @Override
    public BitBody buildBody() {
        return null;
    }

    @Override
    public String name() {
        return "Debug Spawn Point";
    }
}
