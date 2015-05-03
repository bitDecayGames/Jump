package bitDecayJump;

import bitDecayJump.geom.BitRectangle;

public class BitCollision implements Comparable<BitCollision> {

	public BitRectangle collisionZone;
	public BitBody otherBody;
	private float collisionArea;

	public BitCollision(BitRectangle zone, BitBody other) {
		collisionZone = zone;
		this.otherBody = other;
		collisionArea = collisionZone.height * collisionZone.width;
	}

	@Override
	public int compareTo(BitCollision o) {
		//TODO: We might need to invert this depending on if the natural ordering is smallest first or largest first.
		return -1 * Float.compare(collisionArea, o.collisionArea);
	}
}
