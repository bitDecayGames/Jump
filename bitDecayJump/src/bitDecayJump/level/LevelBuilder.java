package bitDecayJump.level;

import java.util.*;

import bitDecayJump.geom.*;

import com.google.gson.*;

public class LevelBuilder {
	private static final int UP = 1;
	private static final int RIGHT = 1 << 1;
	private static final int DOWN = 1 << 2;
	private static final int LEFT = 1 << 3;

	public Level level;

	public List<LevelObject> selection;

	public Map<BitRectangle, List<LevelObject>> groups;
	public Collection<LevelObject> objects;

	public LevelBuilder(Level level) {
		this.level = level;
		selection = new ArrayList<LevelObject>();
		groups = new HashMap<BitRectangle, List<LevelObject>>();
		objects = level.getObjects();
	}

	public LevelBuilder(String json) {
		this(new GsonBuilder().create().fromJson(json, Level.class));
	}

	public void createObject(BitPointInt startPoint, BitPointInt endPoint) {
		for (BitRectangle rect : GeomUtils.split(GeomUtils.makeRect(startPoint, endPoint), level.tileSize, level.tileSize)) {
			objects.add(new LevelObject(rect));
		}
	}

	public void selectObjects(BitRectangle selectionArea, boolean add) {
		if (!add) {
			selection.clear();
		}
		for (LevelObject object : objects) {
			if (selectionArea.contains(object.rect)) {
				selection.add(object);
			}
		}
	}

	public void selectObject(BitPointInt startPoint, boolean add) {
		if (!add) {
			selection.clear();
		}
		for (LevelObject object : objects) {
			if (object.rect.contains(startPoint)) {
				selection.add(object);
				return;
			}
		}
	}

	public void deleteSelected() {
		objects.removeAll(selection);
		selection.clear();
	}

	public String getJson() {
		Level tilizeLevel = tilizeLevel();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(tilizeLevel);
		System.out.println(json);
		return json;
	}

	public Level tilizeLevel() {
		Level tillizedLevel = new Level(level.tileSize);
		int xmin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymin = Integer.MAX_VALUE;
		int ymax = Integer.MIN_VALUE;
		for (LevelObject obj : objects) {
			xmin = Integer.min(xmin, obj.rect.xy.x);
			xmax = Integer.max(xmax, obj.rect.xy.x + obj.rect.width);
			ymin = Integer.min(ymin, obj.rect.xy.y);
			ymax = Integer.max(ymax, obj.rect.xy.y + obj.rect.height);
		}

		int xoffset = xmin;
		int yoffset = ymin;

		LevelObject[][] levelGrid = new LevelObject[(xmax - xmin) / level.tileSize][(ymax - ymin) / level.tileSize];
		System.out.println(levelGrid.length + " by " + levelGrid[0].length);

		for (int x = 0; x < levelGrid.length; x++) {
			for (int y = 0; y < levelGrid[0].length; y++) {
				int inX = x * level.tileSize + xoffset;
				int inY = y * level.tileSize + yoffset;
				boolean found = false;
				for (LevelObject check : objects) {
					// offset the check coordinate by half a tile to check the
					// middle of the grid cell
					if (check.rect.contains(inX + level.tileSize / 2, inY + level.tileSize / 2)) {
						found = true;
						break;
					}
				}
				if (found) {
					levelGrid[x][y] = new LevelObject(new BitRectangle(inX, inY, level.tileSize, level.tileSize));
				} else {
					levelGrid[x][y] = null;
				}
				// if (level.objects.stream().filter(levelObj ->
				// levelObj.rect.contains(inX + xoffset, inY + yoffset)).count()
				// > 0) {
			}
		}

		// now build out our neighbor values
		for (int x = 0; x < levelGrid.length; x++) {
			for (int y = 0; y < levelGrid[0].length; y++) {
				// for (int y = levelGrid[0].length - 1; y >= 0; y--) {
				if (levelGrid[x][y] != null) {
					int value = 0;
					// check right
					if (ArrayUtilities.onGrid(levelGrid, x + 1, y) && levelGrid[x + 1][y] != null) {
						if (levelGrid[x + 1][y].nValue != -1) {
							value |= RIGHT;
						}
					}
					// check left
					if (ArrayUtilities.onGrid(levelGrid, x - 1, y) && levelGrid[x - 1][y] != null) {
						if (levelGrid[x - 1][y].nValue != -1) {
							value |= LEFT;
						}
					}
					// check up
					if (ArrayUtilities.onGrid(levelGrid, x, y + 1) && levelGrid[x][y + 1] != null) {
						if (levelGrid[x][y + 1].nValue != -1) {
							value |= UP;
						}
					}
					// check down
					if (ArrayUtilities.onGrid(levelGrid, x, y - 1) && levelGrid[x][y - 1] != null) {
						if (levelGrid[x][y - 1].nValue != -1) {
							value |= DOWN;
						}
					}
					levelGrid[x][y].nValue = value;
				}
			}
		}

		tillizedLevel.gridOffset = new BitPointInt(xoffset / level.tileSize, yoffset / level.tileSize);
		tillizedLevel.objects = levelGrid;

		return tillizedLevel;
	}
}
