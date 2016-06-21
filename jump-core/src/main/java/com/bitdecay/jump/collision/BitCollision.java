package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;

/**
 * Collision data object. Handles priority vs other collisions. Larger
 * collisions are given higher priority. Collisions against Kinetic objects are
 * given higher priority over any other type.
 */
public class BitCollision implements Comparable<BitCollision> {

	public BitBody body;
	public BitBody against;
	public boolean contactOccurred;

	public BitCollision(BitBody body, BitBody against) {
		this.body = body;
		this.against = against;
	}

	public boolean canBeIgnored() {
		return body.resolutionLocked || (!body.props.collides || !against.props.collides);
	}

	@Override
	public int compareTo(BitCollision o) {
		return Integer.compare(against.bodyType.order, o.against.bodyType.order);
	}
}
