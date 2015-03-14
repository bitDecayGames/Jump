package bitDecayJump.level;

import java.util.*;

import bitDecayJump.geom.BitPointInt;

public class Level {
	public int tileSize = 16;
	public TileObject[][] objects;
	public String baseMaterialDir;
	public Map<Integer, String> materials;
	/**
	 * This offset is to compensate from how far from the origin the 2d array
	 * sits. This is needed due to the array being 'shrink wrapped' to the level
	 * objects.
	 */
	public BitPointInt gridOffset;
	public BitPointInt spawn;

	public Level(int unitSize) {
		this.tileSize = unitSize;
		objects = new TileObject[10][10];
		materials = new HashMap<Integer, String>();
	}

	public Level(Level level) {
		tileSize = level.tileSize;
		objects = level.objects;
		baseMaterialDir = level.baseMaterialDir;
		materials = level.materials;
		gridOffset = level.gridOffset;
		spawn = level.spawn;
	}

	public Collection<TileObject> getObjects() {
		ArrayList<TileObject> list = new ArrayList<TileObject>();
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
