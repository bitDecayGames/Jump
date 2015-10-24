package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.TileBody;

public class TileObject extends LevelObject {
	public int material;

	/** Rendering hint for which tile to use
	 * Based on bitwise values specified in {@link com.bitdecay.jump.level.Direction}
	 */
	public int nValue;

	public boolean oneway;

	public TileObject() {
		// Here for JSON
	}

	public TileObject(BitRectangle rect, boolean oneway, int material) {
		super(rect);
		this.oneway = oneway;
		this.material = material;
	}

	@Override
	public BitBody buildBody() {
		// tile objects don't need a body.
		// CONSIDER: we might want to just put the collision shit (nValue) onto the body
		TileBody body = new TileBody();
		body.material = material;
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
