package bitDecayJump.level;

import java.util.*;

import bitDecayJump.geom.BitPointInt;

public class Level {
	public int tileSize = 16;
	public LevelObject[][] objects;
	/**
	 * This offset is to compensate from how far from the origin the 2d array
	 * sits. This is needed due to the array being 'shrink wrapped' to the level
	 * objects.
	 */
	public BitPointInt gridOffset;

	public Level(int unitSize) {
		this.tileSize = unitSize;
	}

	public Collection<LevelObject> getObjects() {
		ArrayList<LevelObject> list = new ArrayList<LevelObject>();
		if (objects != null) {
			for (int i = 0; i < objects.length; i++) {
				for (int j = 0; j < objects[0].length; j++) {
					if (objects[i][j] != null) {
						list.add(objects[i][j]);
					}
				}
			}
		}
		return list;
	}
}
