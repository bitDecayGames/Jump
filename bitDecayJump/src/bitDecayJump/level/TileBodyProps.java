package bitDecayJump.level;

import bitDecayJump.BitBodyProps;

public class TileBodyProps extends BitBodyProps {
	public TileObject body;

	public int nValue = 0;

	public TileBodyProps(TileObject body) {
		super();
		this.body = body;
	}
}
