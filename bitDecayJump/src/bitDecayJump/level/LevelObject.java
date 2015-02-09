package bitDecayJump.level;

import bitDecayJump.geom.BitRectangle;

public class LevelObject {
	public BitRectangle rect;

	// a rendering hint for which tile to use
	public int nValue;

	public LevelObject(BitRectangle rect) {
		this.rect = rect;
		// TODO Auto-generated constructor stub
	}

	// will also need notion of material / path / style
}
