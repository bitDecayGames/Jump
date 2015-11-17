package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;

/**
 * Collision data object. Handles priority vs other collisions. Larger
 * collisions are given higher priority. Collisions against Kinetic objects are
 * given higher priority over any other type.
 */
public class BitCollision implements Comparable<BitCollision> {

	public BitBody body;
	public BitBody against;
	public SATCollision manifoldData;

	public BitCollision(BitBody body, BitBody against, SATCollision manifoldData) {
		this.body = body;
		this.against = against;
		this.manifoldData = manifoldData;
	}

	@Override
	public int compareTo(BitCollision o) {
		boolean thisCollisionWithKinetic = BodyType.KINETIC.equals(against.bodyType);
		boolean otherCollisionWithKinetic = BodyType.KINETIC.equals(o.against.bodyType);
		if (thisCollisionWithKinetic && !otherCollisionWithKinetic) {
			return -1;
		} else if (otherCollisionWithKinetic && !thisCollisionWithKinetic) {
			return 1;
		} else {
//			return -1 * Float.compare(GeomUtils.intersection(body.aabb, against.aabb).area(), GeomUtils.intersection(body.aabb, o.against.aabb).area());
			return -1 * Float.compare(manifoldData.manifoldTotal(), o.manifoldData.manifoldTotal());
		}
	}
}
