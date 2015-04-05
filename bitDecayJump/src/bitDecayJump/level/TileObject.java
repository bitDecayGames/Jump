package bitDecayJump.level;

import bitDecayJump.*;
import bitDecayJump.geom.BitRectangle;

public class TileObject extends LevelObject {
	public int material;

	// a rendering hint for which tile to use
	public int nValue;

	public TileObject(BitRectangle rect) {
		super(rect);
	}

	@Override
	public BitBody getBody() {
		// tile objects don't need a body.
		// CONSIDER: we might want to just put the collision shit (nValue) onto the body
		BitBody body = new BitBody();
		body.aabb = rect;
		body.props = new BitBodyProps();
		body.props.bodyType = BodyType.STATIC;
		return body;
	}
}
