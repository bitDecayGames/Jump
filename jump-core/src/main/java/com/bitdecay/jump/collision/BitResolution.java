package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * A class to contain all logic and information about a single resolution plan.
 * Each instance of this class will resolve collisions against a single body.
 *
 * @author Monday
 *
 */
public abstract class BitResolution {
	/**
	 * A priority queue that resolves the LARGEST collisions first. Larger
	 * collisions (by area) are easier to determine accurate resolutions for.
	 */
	public HashMap<BitBody, BitCollision> collisions = new HashMap<>();

	protected BitRectangle resolvedPosition;
	protected BitPoint resolution = new BitPoint(0, 0);
	protected BitBody body;

	public BitResolution(BitBody body) {
		this.body = body;
		resolvedPosition = new BitRectangle(body.aabb);
	}

	public void resolve(BitWorld world) {
		satisfy(world);
		// set final resolution values
		resolution.x = resolvedPosition.xy.x - body.aabb.xy.x;
		resolution.y = resolvedPosition.xy.y - body.aabb.xy.y;
	}

	/**
	 * Let our strategy resolve the collision and set the resolved position. It may result in
	 * the body being made inactive as a result of conflicting collisions (such as a body being
	 * squeezed between two bodies)
	 */
	public abstract void satisfy(BitWorld world);
}
