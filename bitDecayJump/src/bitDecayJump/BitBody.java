package bitDecayJump;

import bitDecayJump.geom.*;

public class BitBody {
	public BitRectangle aabb;
	public BitPoint velocity = new BitPoint(0, 0);

	/**
	 * Simple flag to determine if a body is grounded. Specific to platformer
	 * style physics/movement
	 */
	public boolean grounded;

	/**
	 * Properties object to hold information about how this body should interact
	 * with the world
	 */
	public BitBodyProps props;

	/**
	 * Optional controller to control movement/behavior of the body
	 */
	public BitBodyController controller;

	/**
	 * Optional state manager to automatically handle what state an object is in
	 * relative to the physics world
	 */
	public BitBodyStateWatcher stateWatcher;
}
