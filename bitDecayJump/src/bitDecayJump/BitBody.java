package bitDecayJump;

import java.util.*;

import bitDecayJump.controller.BitBodyController;
import bitDecayJump.geom.*;
import bitDecayJump.state.BitBodyStateWatcher;

public class BitBody {
	public BitRectangle aabb;

	public BitBody parent;
	/**
	 * Holds children bodies of this body. Allows for 'attaching' bodies to each
	 * other
	 */
	public Set<BitBody> children = new HashSet<BitBody>();

	/**
	 * Simple flag to determine if a body is grounded. Specific to platformer
	 * style physics/movement
	 */
	public boolean grounded;

	/**
	 * Simple flag to distinguish which direction the body is facing. This does
	 * not necessarily correlate with what direction the body is <i>moving</i>.
	 */
	public Facing facing;

	/**
	 * Properties object to hold information about how this body should interact
	 * with the world. Contains information such as body type, speed,
	 * acceleration, etc
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

	/**
	 * Tracks the <b>last</b> movement the body <b>tried</b> to make. This does
	 * not mean the body did move.
	 */
	public BitPoint lastAttempt = new BitPoint(0, 0);

	/**
	 * Tracks what the physics world <b>last</b> did to resolve any collisions
	 * that may have happened
	 */
	public BitPoint lastResolution = new BitPoint(0, 0);

	/**
	 * Only active bodies are updated in the world
	 */
	public boolean active = true;
}
