package bitDecayJump.level;

public class Neighbor {
	public static final int UP = 1;
	public static final int RIGHT = 1 << 1;
	public static final int DOWN = 1 << 2;
	public static final int LEFT = 1 << 3;

	public static final int NOT_UP = RIGHT | DOWN | LEFT;
	public static final int NOT_RIGHT = UP | DOWN | LEFT;
	public static final int NOT_DOWN = UP | RIGHT | LEFT;
	public static final int NOT_LEFT = UP | RIGHT | DOWN;

	public static final int SIDES = LEFT | RIGHT;
	public static final int UPDOWN = UP | DOWN;
}
