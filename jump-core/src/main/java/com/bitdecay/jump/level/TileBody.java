package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;

public class TileBody extends BitBody {
	public int nValue = 0;
	public int material = 0;
	/**
	 * If defined, this collision axis is the only axis this tile can be collided with from.
	 * This axis is directional.
	 */
	public BitPoint collisionAxis = null;

	public TileBody(){
		super();
	}
}
