package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.TileBody;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class TileObject extends LevelObject {
	public int material;

	// a rendering hint for which tile to use
	public int nValue;

	public boolean oneway;

	public TileObject() {
		// Here for JSON
	}

	public TileObject(BitRectangle rect, boolean oneway) {
		super(rect);
		this.oneway = oneway;
	}

	@Override
	public BitBody buildBody() {
		// tile objects don't need a body.
		// CONSIDER: we might want to just put the collision shit (nValue) onto the body
		TileBody body = new TileBody();
		body.aabb = rect.copyOf();
		body.nValue = nValue;
		body.bodyType = BodyType.STATIC;
		if (oneway) {
			body.collisionAxis = GeomUtils.Y_AXIS;
		}
		return body;
	}

	@Override
	public String name() {
		return "Tile Platform";
	}
}
