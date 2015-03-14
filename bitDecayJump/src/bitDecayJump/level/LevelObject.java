package bitDecayJump.level;

import bitDecayJump.geom.BitRectangle;

public abstract class LevelObject {
	public BitRectangle rect;

	public LevelObject(BitRectangle rect) {
		this.rect = rect;
	}

	// will also need notion of material / path / style
}
