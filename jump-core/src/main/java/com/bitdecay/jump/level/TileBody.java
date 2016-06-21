package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;

public class TileBody extends BitBody {
	/**
	 * this value is a bit flag integer to indicate which sides of an object have something
	 * present. A 1 in the bit indicates something is present, a 0 indicates nothing is.<br>
	 *<br>
	 * Ex:<br>
	 * a value of 1 indicates that there is only something above this tile.<br>
	 * a value of 6 indicates that there is something to the right and something below.<br>
	 *
	 * @see Direction
	 */
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
