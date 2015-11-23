package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;

public class TileObject extends LevelObject {
	@CantInspect
	public int material;

	/** Rendering hint for which tile to use
	 * Based on bitwise values specified in {@link com.bitdecay.jump.level.Direction}
	 */
	@CantInspect
	public int renderNValue;

	@CantInspect
	public int collideNValue;

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
		TileBody body = new TileBody();
		body.material = material;
		body.aabb = rect.copyOf();
		body.nValue = collideNValue;
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
