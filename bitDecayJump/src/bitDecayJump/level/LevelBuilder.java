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
	private static final int START_SIZE = 20;

	public Level level;

	public List<LevelObject> selection;

	/**
	 * This collection is used to track the static level objects used to build
	 * the grid for the level
	 */
	public Collection<TileObject> tileObjects;

	/**
	 * This collection is used to track all other objects such as power-ups and
	 * moving platforms
	 */
	public Collection<LevelObject> otherObjects;

	public int tileSize;
	public TileObject[][] grid;
	public BitPointInt gridOffset;

	public List<LevelBuilderListener> listeners;

	public LevelBuilder(int tileSize) {
		newLevel(tileSize);
	}

	public void newLevel(int tileSize) {
		this.tileSize = tileSize;
		listeners = new ArrayList<LevelBuilderListener>();
		grid = new TileObject[START_SIZE][START_SIZE];
		gridOffset = new BitPointInt(-(START_SIZE / 2), -(START_SIZE / 2));
		selection = new ArrayList<LevelObject>();
		tileObjects = new HashSet<TileObject>();
	}

	public LevelBuilder(Level level) {
		setLevel(level);
	}

	public void setLevel(Level level) {
		this.level = level;
		tileSize = level.tileSize;
		grid = level.objects;
		gridOffset = level.gridOffset;
		tileSize = level.tileSize;
		tileObjects = level.getObjects();
		for (LevelBuilderListener levelListener : listeners) {
			levelListener.levelChanged(level);
		}
	}

	public void createKineticObject(BitPointInt startPoint, BitPointInt endPoint) {
		// TODO Auto-generated method stub

	}

	public void createLevelObject(BitPointInt startPoint, BitPointInt endPoint) {
		List<LevelObject> newObjects = new ArrayList<LevelObject>();

		BitPointInt objCell = new BitPointInt(0, 0);
		boolean resize = false;
		for (BitRectangle rect : GeomUtils.split(GeomUtils.makeRect(startPoint, endPoint), tileSize, tileSize)) {
			TileObject obj = new TileObject(rect);

			objCell = getOccupiedCell(obj);
			while (!ArrayUtilities.onGrid(grid, objCell.x, objCell.y)) {
				resize = true;
				TileObject[][] newGrid = new TileObject[grid.length * 2][grid[0].length * 2];
				BitPointInt newCell = new BitPointInt(0, 0);
				for (int i = 0; i < grid.length; i++) {
					newCell.x = i + grid.length / 2;
					for (int j = 0; j < grid[0].length; j++) {
						newCell.y = j + grid[0].length / 2;
						newGrid[newCell.x][newCell.y] = grid[i][j];
					}
				}
				grid = newGrid;
				gridOffset = new BitPointInt(gridOffset.x - newGrid.length / 4, gridOffset.y - newGrid[0].length / 4);

				// rebuild our objCell now that we changed the grid
				objCell = getOccupiedCell(obj);
			}

			if (grid[objCell.x][objCell.y] == null) {
				newObjects.add(obj);
				tileObjects.remove(grid[objCell.x][objCell.y]);
				grid[objCell.x][objCell.y] = obj;
				updateNeighbors(objCell.x, objCell.y);
				tileObjects.add(obj);
			}
		}

		if (resize) {
			for (LevelBuilderListener listener : listeners) {
				listener.updateGrid(gridOffset, grid);
			}
		}
	}

	private void updateNeighbors(int x, int y) {
		// check right
		if (ArrayUtilities.onGrid(grid, x + 1, y) && grid[x + 1][y] != null) {
			if (grid[x][y] == null) {
				grid[x + 1][y].nValue &= Neighbor.NOT_LEFT;
			} else {
				grid[x][y].nValue |= Neighbor.RIGHT;
				grid[x + 1][y].nValue |= Neighbor.LEFT;
			}
		}
		// check left
		if (ArrayUtilities.onGrid(grid, x - 1, y) && grid[x - 1][y] != null) {
			if (grid[x][y] == null) {
				grid[x - 1][y].nValue &= Neighbor.NOT_RIGHT;
			} else {
				grid[x][y].nValue |= Neighbor.LEFT;
				grid[x - 1][y].nValue |= Neighbor.RIGHT;
			}
		}
		// check up
		if (ArrayUtilities.onGrid(grid, x, y + 1) && grid[x][y + 1] != null) {
			if (grid[x][y] == null) {
				grid[x][y + 1].nValue &= Neighbor.NOT_DOWN;
			} else {
				grid[x][y].nValue |= Neighbor.UP;
				grid[x][y + 1].nValue |= Neighbor.DOWN;
			}
		}
		// check down
		if (ArrayUtilities.onGrid(grid, x, y - 1) && grid[x][y - 1] != null) {
			if (grid[x][y] == null) {
				grid[x][y - 1].nValue &= Neighbor.NOT_UP;
			} else {
				grid[x][y].nValue |= Neighbor.DOWN;
				grid[x][y - 1].nValue |= Neighbor.UP;
			}
		}
	}

	private BitPointInt getOccupiedCell(LevelObject obj) {
		BitPointInt objCell;
		objCell = new BitPointInt((int) obj.rect.xy.x, (int) obj.rect.xy.y);
		objCell = objCell.floorDivideBy(tileSize, tileSize).minus(gridOffset);
		return objCell;
	}

	public void deleteSelected() {
		for (LevelObject obj : selection) {
			BitPointInt gridCell = getOccupiedCell(obj);
			grid[gridCell.x][gridCell.y] = null;
			updateNeighbors(gridCell.x, gridCell.y);
		}
		tileObjects.removeAll(selection);
		selection.clear();
	}

	public void selectObjects(BitRectangle selectionArea, boolean add) {
		if (!add) {
			selection.clear();
		}
		for (LevelObject object : tileObjects) {
			if (selectionArea.contains(object.rect)) {
				selection.add(object);
			}
		}
	}

	public void selectObject(BitPointInt startPoint, boolean add) {
		if (!add) {
			selection.clear();
		}
		for (LevelObject object : tileObjects) {
			if (object.rect.contains(startPoint)) {
				selection.add(object);
				return;
			}
		}
	}

	public String getJson() {
		return FileUtils.toJson(tilizeLevel());
	}

	public Level tilizeLevel() {
		Level tillizedLevel = new Level(tileSize);
		BitPointInt min = getMinXY(tileObjects);
		BitPointInt max = getMaxXY(tileObjects);

		TileObject[][] levelGrid = new TileObject[(max.x - min.x) / tileSize][(max.y - min.y) / tileSize];

		for (int x = 0; x < levelGrid.length; x++) {
			for (int y = 0; y < levelGrid[0].length; y++) {
				int inX = x * tileSize + min.x;
				int inY = y * tileSize + min.y;
				boolean found = false;
				for (LevelObject check : tileObjects) {
					// offset the check coordinate by half a tile to check the
					// middle of the grid cell
					if (check.rect.contains(inX + tileSize / 2, inY + tileSize / 2)) {
						found = true;
						break;
					}
				}
				if (found) {
					levelGrid[x][y] = new TileObject(new BitRectangle(inX, inY, tileSize, tileSize));
				} else {
					levelGrid[x][y] = null;
				}
			}
		}

		// now build out our neighbor values
		for (int x = 0; x < levelGrid.length; x++) {
			for (int y = 0; y < levelGrid[0].length; y++) {
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

		tillizedLevel.gridOffset = new BitPointInt(min.x / tileSize, min.y / tileSize);
		tillizedLevel.objects = levelGrid;

		return tillizedLevel;
	}

	private BitPointInt getMinXY(Collection<TileObject> objects) {
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		for (LevelObject obj : objects) {
			xmin = Integer.min(xmin, (int) obj.rect.xy.x);
			ymin = Integer.min(ymin, (int) obj.rect.xy.y);
		}
		return new BitPointInt(xmin, ymin);
	}

	private BitPointInt getMaxXY(Collection<TileObject> objects) {
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		for (LevelObject obj : objects) {
			xmax = Integer.max(xmax, (int) (obj.rect.xy.x + obj.rect.width));
			ymax = Integer.max(ymax, (int) (obj.rect.xy.y + obj.rect.height));
		}
		return new BitPointInt(xmax, ymax);
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

	public void addMaterial(String mat) {
		level.materials.put(level.materials.size(), mat);
	}
}
