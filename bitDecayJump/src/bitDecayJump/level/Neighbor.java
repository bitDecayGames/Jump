package bitDecayJump.level;

public class Neighbor {
	public static final int UP = 1;
	public static final int RIGHT = 1 << 1;
	public static final int DOWN = 1 << 2;
	public static final int LEFT = 1 << 3;

	public static final int SIDES = LEFT | RIGHT;
	public static final int UPDOWN = UP | DOWN;
}
