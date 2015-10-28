package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.properties.JumperProperties;

/**
 * Created by Monday on 10/25/2015.
 */
public class SpawnObject extends LevelObject{
    public static final int INNER_DIAMETER = 4;
    public static final int OUTER_DIAMETER = 8;

    public JumperProperties props;

    public SpawnObject() {
        // Here for JSON
    }

    public SpawnObject(BitPointInt point) {
        super(new BitRectangle(point, point));

        // filler for now. Eventually the UI will let this be loaded/saved/tweaked
        JumperProperties props = new JumperProperties();
        props.airAcceleration.set(600, 0);
        props.airDeceleration.set(300, 0);
        props.acceleration.set(600, 0);
        props.deceleration.set(300, 0);
        props.jumpCount = 2;
        props.jumpStrength = 300;
        props.jumpDoubleJumpStrength = 150;
        props.jumpVariableHeightWindow = .2f;
        props.jumpGraceWindow = .2f;
        props.jumpHittingHeadStopsJump = true;
        this.props = props;
    }

    /**
     * A spawn object doesn't have a body
     * @return
     */
    @Override
    public BitBody buildBody() {
        return new BitBody();
    }

    @Override
    public String name() {
        return "Spawn Point";
    }
}
