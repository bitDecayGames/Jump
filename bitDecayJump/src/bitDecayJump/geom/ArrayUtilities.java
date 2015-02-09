package bitDecayJump.geom;


public class ArrayUtilities {
	public static <T> boolean onGrid(T[][] grid, int x, int y) {
		return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
	}
}
