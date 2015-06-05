package bitDecayJump.test;

import java.util.List;

import bitDecayJump.BitBody;
import bitDecayJump.level.Level;

/**
 * A test case is a simple definition of a level with bodies in it. It is
 * intended to hold data to allow the testing a specific collision situation
 * between a dynamic body and one or more level objects.
 */
public class BitDecayTestCase {
	public Level level;
	public List<BitBody> bodies;
	public List<BitBody> endPositions;
}
