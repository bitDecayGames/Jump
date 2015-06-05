package bitDecayJump.level;

import java.util.*;

import bitDecayJump.geom.BitPointInt;

import com.fasterxml.jackson.annotation.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Level {
	public int tileSize = 16;
	public TileObject[][] gridObjects;
	public List<LevelObject> otherObjects;
	public String baseMaterialDir;
	public Map<Integer, String> materials;
	/**
	 * This offset is to compensate from how far from the origin the 2d array
	 * sits. This is needed due to the array being 'shrink wrapped' to the grid
	 * objects.
	 */
	public BitPointInt gridOffset;
	public BitPointInt spawn;

	public Level() {
		// Here for JSON persistence/parsing
	}

	public Level(int unitSize) {
		this.tileSize = unitSize;
		gridObjects = new TileObject[10][10];
		otherObjects = new ArrayList<LevelObject>();
		materials = new HashMap<Integer, String>();
	}

	public Level(Level level) {
		tileSize = level.tileSize;
		gridObjects = level.gridObjects;
		otherObjects = level.otherObjects;
		baseMaterialDir = level.baseMaterialDir;
		materials = level.materials;
		gridOffset = level.gridOffset;
		spawn = level.spawn;
	}

	@JsonIgnore
	public Collection<TileObject> getGridObjectsAsCollection() {
		ArrayList<TileObject> list = new ArrayList<TileObject>();
		if (gridObjects != null) {
			for (int i = 0; i < gridObjects.length; i++) {
				for (int j = 0; j < gridObjects[0].length; j++) {
					if (gridObjects[i][j] != null) {
						list.add(gridObjects[i][j]);
					}
				}
			}
		}
		return list;
	}
}
