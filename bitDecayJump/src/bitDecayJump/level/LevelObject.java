package bitDecayJump.level;

import bitDecayJump.geom.BitRectangle;

public class LevelObject {
	public BitRectangle rect;
	public int material;

	// a rendering hint for which tile to use
	public int nValue;

	public LevelObject(BitRectangle rect) {
		this.rect = rect;
	}

	// will also need notion of material / path / style
}
