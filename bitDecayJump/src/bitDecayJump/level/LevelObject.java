package bitDecayJump.level;

import bitDecayJump.BitBody;
import bitDecayJump.geom.BitRectangle;

public abstract class LevelObject {
	public BitRectangle rect;

	public LevelObject() {
		// Here for JSON
	}

	public LevelObject(BitRectangle rect) {
		this.rect = rect;
	}

	public abstract BitBody getBody();

	// will also need notion of material / path / style
}
