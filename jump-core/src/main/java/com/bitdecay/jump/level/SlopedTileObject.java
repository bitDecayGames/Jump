package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;

/**
 * Created by Monday on 10/18/2016.
 */
public class SlopedTileObject extends TileObject {
    // floats that describe the height, as a percentage, of the body height that the slope sits on the sides of the tile
    public float leftY = 1;
    public float rightY = 1;

    public boolean isFloor = true;

    public SlopedTileObject() {
        // Here for JSON
    }

    public SlopedTileObject(BitRectangle rect, boolean oneway, int material, float leftHeight, float rightHeight, boolean isFloor) {
        super(rect, oneway, material);
        leftY = leftHeight;
        rightY = rightHeight;
        this.isFloor = isFloor;
    }

    @Override
    public BitBody buildBody() {
        SlopedTileBody body = new SlopedTileBody();
        body.leftY = leftY;
        body.rightY = rightY;
        body.isFloor = isFloor;
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
