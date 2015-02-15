package bitDecayJump;

import bitDecayJump.geom.*;

public class BitBody {
	public BitRectangle aabb;

	public BitPoint velocity = new BitPoint(0, 0);
	public boolean grounded;

	public BitBodyProps props;

	public BitBodyController controller;

}
