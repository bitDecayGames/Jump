package com.bitdecay.jump.leveleditor.render.mouse;

import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.LevelBuilder;

/**
 * Created by Monday on 10/6/2015.
 */
public class CreateOneWayMouseMode extends CreateMouseMode {
    public CreateOneWayMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseUpLogic(BitPointInt point, MouseButton button) {
        endPoint = GeomUtils.snap(point, builder.tileSize);
        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
            builder.createLevelObject(startPoint, endPoint, true);
        }
    }
}
