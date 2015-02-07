package bitDecayJump.level;

import java.util.*;

public class Level {
	public List<LevelObject> objects;
	public int tileSize;

	public Level(int tileSize) {
		this.tileSize = tileSize;
		objects = new ArrayList<LevelObject>();
	}
}
