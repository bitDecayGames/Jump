package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.annotation.CanLoadFromFile;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.BitBodyProperties;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 10/25/2015.
 */
public class DebugSpawnObject extends LevelObject {
    public static final int INNER_DIAMETER = 4;
    public static final int OUTER_DIAMETER = 8;

    @CanLoadFromFile
    public BitBodyProperties props;
    @CanLoadFromFile
    public JumperProperties jumpProps;

    public DebugSpawnObject(BitPointInt point) {
        super(new BitRectangle(point, point));

        props = new BitBodyProperties();
        props.airAcceleration = 600;
        props.airDeceleration = 300;
        props.acceleration = 600;
        props.deceleration = 300;

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

    @Override
    public boolean selects(BitPointInt point) {
        return rect.xy.minus(point).len() < DebugSpawnObject.OUTER_DIAMETER;
    }
}
