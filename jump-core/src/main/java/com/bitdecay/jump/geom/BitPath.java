package com.bitdecay.jump.geom;

import java.util.List;

public class BitPath {
	public List<BitPointInt> nodes;
	private BitPoint last;
	private BitPoint destination;
	private float timePassed;

	/**
	 * Will determine where to be based on the time passed in -- at most the
	 * path will update one node.
	 * 
	 * @param delta
	 * @param speed
	 * @return
	 */
	public BitPointInt update(float delta, float speed) {
		// TODO Compute direction vector between last and destination
		// Then calculate total time passed and determine distance from last we should be in the direction of destination
		BitPoint minus = last.minus(destination);
		return null;
	}
}
