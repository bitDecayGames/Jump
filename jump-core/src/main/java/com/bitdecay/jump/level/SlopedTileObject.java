package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.GeomUtils;

/**
 * Created by Monday on 10/18/2016.
 */
public class SlopedTileObject extends TileObject {
    // floats that describe the height, as a percentage, of the body height that the slope sits on the sides of the tile
    float leftY = 1;
    float rightY = 1;

    @Override
    public BitBody buildBody() {
        SlopedTileBody body = new SlopedTileBody();
        body.leftY = leftY;
        body.rightY = rightY;
        body.material = material;
        body.aabb = rect.copyOf();
        body.nValue = collideNValue;
        body.bodyType = BodyType.STATIC;
        if (oneway) {
            body.collisionAxis = GeomUtils.Y_AXIS;
        }
        return body;
    }
}
