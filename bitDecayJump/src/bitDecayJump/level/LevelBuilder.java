package bitDecayJump.level;

import java.util.*;

import bitDecayJump.geom.*;

/**
 * A wrapper object around a level to handle adding and removing things from the
 * level. Recreates the underlying level any time changes are made. It does this
 * to ensure that all level objects are of the proper size.<br>
 * <br>
 * If this causes performance issues down the road with larger levels, we will
 * figure out what to do then. For now it works quite well.
 * 
 * @author Monday
 */
public class LevelBuilder {
	public Level level;

	public List<LevelObject> selection;

	public Collection<LevelObject> objects;

	public List<LevelBuilderListener> listeners;

	public LevelBuilder(Level level) {
		listeners = new ArrayList<LevelBuilderListener>();
		setLevel(level);
	}

	public void setLevel(Level level) {
		this.level = level;
		for (LevelBuilderListener levelListener : listeners) {
			levelListener.levelChanged(level);
		}
		objects = level.getObjects();
		selection = new ArrayList<LevelObject>();
	}

	public void createObject(BitPointInt startPoint, BitPointInt endPoint) {
		for (BitRectangle rect : GeomUtils.split(GeomUtils.makeRect(startPoint, endPoint), level.tileSize, level.tileSize)) {
			LevelObject obj = new LevelObject(rect);
			objects.add(obj);
		}
		refresh();
	}

	public void deleteSelected() {
		objects.removeAll(selection);
		selection.clear();
		refresh();
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

	private void refresh() {
		setLevel(tilizeLevel());
	}

	public String getJson() {
		return FileUtils.toJson(tilizeLevel());
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
							value |= Neighbor.RIGHT;
						}
					}
					// check left
					if (ArrayUtilities.onGrid(levelGrid, x - 1, y) && levelGrid[x - 1][y] != null) {
						if (levelGrid[x - 1][y].nValue != -1) {
							value |= Neighbor.LEFT;
						}
					}
					// check up
					if (ArrayUtilities.onGrid(levelGrid, x, y + 1) && levelGrid[x][y + 1] != null) {
						if (levelGrid[x][y + 1].nValue != -1) {
							value |= Neighbor.UP;
						}
					}
					// check down
					if (ArrayUtilities.onGrid(levelGrid, x, y - 1) && levelGrid[x][y - 1] != null) {
						if (levelGrid[x][y - 1].nValue != -1) {
							value |= Neighbor.DOWN;
						}
					}
					levelGrid[x][y].nValue = value;
				}
			}
		}

		tillizedLevel.gridOffset = new BitPointInt(xoffset / level.tileSize, yoffset / level.tileSize);
		tillizedLevel.objects = levelGrid;
		tillizedLevel.spawn = level.spawn;

		return tillizedLevel;
	}

	public void addListener(LevelBuilderListener levelListener) {
		listeners.add(levelListener);
	}

	public void removeListener(LevelBuilderListener levelListener) {
		listeners.remove(levelListener);
	}

	public void setSpawn(BitPointInt point) {
		level.spawn = point;
	}
}
