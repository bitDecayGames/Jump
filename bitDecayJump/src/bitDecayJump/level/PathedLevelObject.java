package bitDecayJump.level;

import bitDecayJump.*;
import bitDecayJump.geom.BitRectangle;

public class PathedLevelObject extends LevelObject {

	public PathedLevelObject(BitRectangle rect) {
		super(rect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BitBody getBody() {
		BitBody body = new BitBody();
		body.aabb = rect;
		body.props = new BitBodyProps();
		body.props.bodyType = BodyType.KINETIC;
		return body;
	}

	//TODO needs notion of path and move speed.
}
