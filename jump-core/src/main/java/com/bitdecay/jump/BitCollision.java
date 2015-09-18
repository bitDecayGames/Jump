package com.bitdecay.jump;

import com.bitdecay.jump.geom.BitRectangle;

/**
 * Collision data object. Handles priority vs other collisions. Larger
 * collisions are given higher priority. Collisions against Kinetic objects are
 * given higher priority over any other type.
 */
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
		boolean thisCollisionWithKinetic = BodyType.KINETIC.equals(otherBody.props.bodyType);
		boolean otherCollisionWithKinetic = BodyType.KINETIC.equals(o.otherBody.props.bodyType);
		if (thisCollisionWithKinetic && !otherCollisionWithKinetic) {
			return -1;
		} else if (otherCollisionWithKinetic && !thisCollisionWithKinetic) {
			return 1;
		} else {
			return -1 * Float.compare(collisionArea, o.collisionArea);
		}
	}
}
