package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;

public class TileObject extends LevelObject {
	public int material;

	// a rendering hint for which tile to use
	public int nValue;

	public boolean oneway;

	public TileObject() {
		// TODO Here for JSON
	}

	public TileObject(BitRectangle rect) {
		super(rect);
	}

	@Override
	public BitBody getBody() {
		// tile objects don't need a body.
		// CONSIDER: we might want to just put the collision shit (nValue) onto the body
		TileBody body = new TileBody();
		body.aabb = rect;
		body.nValue = nValue;
		body.bodyType = BodyType.STATIC;
		if (oneway) {
			body.collisionAxis = GeomUtils.Y_AXIS;
		}
		return body;
	}
}
