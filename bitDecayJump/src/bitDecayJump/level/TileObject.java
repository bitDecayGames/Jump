package bitDecayJump.level;

import bitDecayJump.geom.BitRectangle;

public class TileObject extends LevelObject {
	public int material;

	// a rendering hint for which tile to use
	public int nValue;

	public TileObject(BitRectangle rect) {
		super(rect);
	}

}
