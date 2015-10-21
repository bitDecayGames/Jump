package com.bitdecay.jump.level.builder;

import java.util.*;

import com.bitdecay.jump.geom.*;
import com.bitdecay.jump.level.*;
import com.sun.org.apache.bcel.internal.generic.Type;

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

	private LinkedList<BuilderAction> actions = new LinkedList<>();

	private int lastAction = -1;

	public List<LevelObject> selection;

	/**
	 * This collection is used to track all other objects such as power-ups and
	 * moving platforms
	 */
	public List<LevelObject> otherObjects;

	public int tileSize;
	public TileObject[][] grid;
	public BitPointInt gridOffset;
	public BitPointInt spawn;

	public List<LevelBuilderListener> listeners;

	public LevelBuilder(int tileSize) {
		newLevel(tileSize);
	}

	public void newLevel(int tileSize) {
		this.tileSize = tileSize;
		listeners = new ArrayList<>();
		grid = new TileObject[START_SIZE][START_SIZE];
		gridOffset = new BitPointInt(-(START_SIZE / 2), -(START_SIZE / 2));
		selection = new ArrayList<>();
		otherObjects = new ArrayList<>();
	}

	public LevelBuilder(Level level) {
		setLevel(level);
	}

	public void setLevel(Level level) {
		tileSize = level.tileSize;
		grid = level.gridObjects;
		gridOffset = level.gridOffset;
		tileSize = level.tileSize;
		otherObjects = level.otherObjects != null ? level.otherObjects : new ArrayList<>();
		for (LevelBuilderListener levelListener : listeners) {
			levelListener.levelChanged(level);
		}
	}

	public void createKineticObject(BitRectangle rect, List<PathPoint> path, boolean pendulum) {
		// copy the list so we don't share data between the level and the world
		List<PathPoint> listCopy = new ArrayList<>();
		for (PathPoint point : path) {
			listCopy.add(new PathPoint(point.destination.minus(rect.xy.plus(rect.width / 2, rect.height / 2)), point.speed, point.stayTime));
		}
		PathedLevelObject kObj = new PathedLevelObject(rect.copyOf(), listCopy, pendulum);

		BuilderAction createKineticAction = new BuilderAction(BuilderAction.Type.ADD, kObj);
		pushAction(createKineticAction);
	}

	public void createLevelObject(BitPointInt startPoint, BitPointInt endPoint, boolean oneway) {
		List<LevelObject> newObjects = new ArrayList<>();
		GeomUtils.split(GeomUtils.makeRect(startPoint, endPoint), tileSize, tileSize).forEach(rect ->
				newObjects.add(new TileObject(rect, oneway)));
		if (newObjects.size() > 0) {
			BuilderAction createLevelObjectAction = new BuilderAction(BuilderAction.Type.ADD, newObjects.toArray(new LevelObject[newObjects.size()]));
			pushAction(createLevelObjectAction);
		}
	}

	public void createObject(Class<? extends LevelObject> object, BitPointInt place) {
		try {
			LevelObject newObject = object.newInstance();
			newObject.rect.xy.set(place.x, place.y);
			BuilderAction createObjectAction = new BuilderAction(BuilderAction.Type.ADD, newObject);
			pushAction(createObjectAction);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void undo() {
		BuilderAction undoAction = popAction();
		if (undoAction != null) {
			if (BuilderAction.Type.ADD.equals(undoAction.type)) {
				removeObjects(undoAction.objects);
			} else if (BuilderAction.Type.DELETE.equals(undoAction.type)) {
				addObjects(undoAction.objects);
			}
		}
	}

	public void redo() {
		if (lastAction < actions.size()-1) {
			BuilderAction redoAction = actions.get(lastAction+1);
			if (BuilderAction.Type.ADD.equals(redoAction.type)) {
				addObjects(redoAction.objects);
			} else if (BuilderAction.Type.DELETE.equals(redoAction.type)) {
				removeObjects(redoAction.objects);
			}
			lastAction++;
		}
	}

	private void ensureGridFitsObject(TileObject obj) {
		BitPointInt objCell = getOccupiedCell(obj);
		while (!ArrayUtilities.onGrid(grid, objCell.x, objCell.y)) {
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
	}

	private void addObjects(List<LevelObject> objects) {
		int gridX;
		int gridY;
		for (LevelObject obj : objects) {
			if (obj instanceof TileObject) {
				ensureGridFitsObject((TileObject)obj);
				gridX = (int) ((obj.rect.xy.x / tileSize) - gridOffset.x);
				gridY = (int) ((obj.rect.xy.y / tileSize) - gridOffset.y);
				grid[gridX][gridY] = (TileObject)obj;
				updateNeighbors(gridX, gridY);
			} else {
				otherObjects.add(obj);
			}
		}
		fireToListeners();
	}

	private void removeObjects(List<LevelObject> objects) {
		// clean up out of other objects
		otherObjects.removeAll(objects);
		// clean out our grid
		int gridX;
		int gridY;
		for (LevelObject obj : objects) {
			gridX = (int) ((obj.rect.xy.x / tileSize) - gridOffset.x);
			gridY = (int) ((obj.rect.xy.y / tileSize) - gridOffset.y);
			if (ArrayUtilities.onGrid(grid, gridX, gridY) && grid[gridX][gridY] == obj) {
				grid[gridX][gridY] = null;
				updateNeighbors(gridX, gridY);
			}
		}
		fireToListeners();
	}

	private void fireToListeners() {
		Level optimizedLevel = optimizeLevel();
		for (LevelBuilderListener listener : listeners) {
			listener.levelChanged(optimizedLevel);
		}
	}

	private void pushAction(BuilderAction action) {
		actions = new LinkedList<>(actions.subList(0, lastAction+1));
		actions.add(action);
		redo();
	}

	private BuilderAction popAction() {
		BuilderAction action = null;
		if (lastAction >= 0) {
			action = actions.get(lastAction);
			lastAction--;
		}
		return action;
	}

	private void updateNeighbors(int x, int y) {
		// check right
		if (ArrayUtilities.onGrid(grid, x + 1, y) && grid[x + 1][y] != null && !grid[x + 1][y].oneway) {
			if (grid[x][y] == null) {
				grid[x + 1][y].nValue &= Direction.NOT_LEFT;
			} else {
				grid[x][y].nValue |= Direction.RIGHT;
				grid[x + 1][y].nValue |= Direction.LEFT;
			}
		}
		// check left
		if (ArrayUtilities.onGrid(grid, x - 1, y) && grid[x - 1][y] != null && !grid[x - 1][y].oneway) {
			if (grid[x][y] == null) {
				grid[x - 1][y].nValue &= Direction.NOT_RIGHT;
			} else {
				grid[x][y].nValue |= Direction.LEFT;
				grid[x - 1][y].nValue |= Direction.RIGHT;
			}
		}
		// check up
		if (ArrayUtilities.onGrid(grid, x, y + 1) && grid[x][y + 1] != null && !grid[x][y + 1].oneway) {
			if (grid[x][y] == null) {
				grid[x][y + 1].nValue &= Direction.NOT_DOWN;
			} else {
				grid[x][y].nValue |= Direction.UP;
				grid[x][y + 1].nValue |= Direction.DOWN;
			}
		}
		// check down
		if (ArrayUtilities.onGrid(grid, x, y - 1) && grid[x][y - 1] != null && !grid[x][y - 1].oneway) {
			if (grid[x][y] == null) {
				grid[x][y - 1].nValue &= Direction.NOT_UP;
			} else {
				grid[x][y].nValue |= Direction.DOWN;
				grid[x][y - 1].nValue |= Direction.UP;
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
		if (selection.size() > 0) {
			BuilderAction deleteAction = new BuilderAction(BuilderAction.Type.DELETE, selection.toArray(new LevelObject[selection.size()]));
			pushAction(deleteAction);
			selection.clear();
		}
	}

	public void selectObjects(BitRectangle selectionArea, boolean add) {
		if (!add) {
			selection.clear();
		}
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				LevelObject object = grid[x][y];
				if (object != null && selectionArea.contains(object.rect)) {
					selection.add(object);
				}
			}
		}
		otherObjects.forEach(object -> {
			if (selectionArea.contains(object.rect)) {
				selection.add(object);
			}
		});
	}

	public void selectObject(BitPointInt startPoint, boolean add) {
		if (!add) {
			selection.clear();
		}
		otherObjects.forEach(object -> {
			if (object.rect.contains(startPoint)) {
				selection.add(object);
				return;
			}
		});
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				LevelObject object = grid[x][y];
				if (object != null && object.rect.contains(startPoint)) {
					selection.add(object);
					return;
				}
			}
		}
	}

	public String getJson() {
		return FileUtils.toJson(optimizeLevel());
	}

	/**
	 * Fits the grid to the current level contents and also returns the level constructed in the process
	 * @return
	 */
	public Level optimizeLevel() {
		Level optimizedLevel = new Level(tileSize);
		BitPointInt min = getMinXY();
		BitPointInt max = getMaxXY();

		if (min.x == Integer.MAX_VALUE || min.y == Integer.MAX_VALUE || max.x == Integer.MIN_VALUE || max.y == Integer.MIN_VALUE) {
			min.x = -START_SIZE/2 * tileSize;
			min.y = -START_SIZE/2 * tileSize;
			max.x = START_SIZE/2 * tileSize;
			max.y = START_SIZE/2 * tileSize;
		}

		TileObject[][] optimizedGrid = new TileObject[(max.x - min.x) / tileSize][(max.y - min.y) / tileSize];

		int xOffset = (min.x / tileSize) - gridOffset.x;
		int yOffset = (min.y / tileSize) - gridOffset.y;
		for (int x = 0; x < optimizedGrid.length; x++) {
			for (int y = 0; y < optimizedGrid[0].length; y++) {
				optimizedGrid[x][y] = grid[x+xOffset][y+yOffset];
			}
		}

		grid = optimizedGrid;
		gridOffset = new BitPointInt(min.x / tileSize, min.y / tileSize);

		optimizedLevel.gridOffset = new BitPointInt(gridOffset.x, gridOffset.y);
		optimizedLevel.gridObjects = optimizedGrid;
		optimizedLevel.otherObjects = otherObjects;
		optimizedLevel.spawn = spawn;

		return optimizedLevel;
	}

	private BitPointInt getMinXY() {
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				LevelObject obj = grid[x][y];
				if (obj != null) {
					xmin = Integer.min(xmin, (int) obj.rect.xy.x);
					ymin = Integer.min(ymin, (int) obj.rect.xy.y);
				}
			}
		}
		return new BitPointInt(xmin, ymin);
	}

	private BitPointInt getMaxXY() {
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				LevelObject obj = grid[x][y];
				if (obj != null) {
					xmax = Integer.max(xmax, (int) (obj.rect.xy.x + obj.rect.width));
					ymax = Integer.max(ymax, (int) (obj.rect.xy.y + obj.rect.height));
				}
			}
		}
		return new BitPointInt(xmax, ymax);
	}

	public void addListener(LevelBuilderListener levelListener) {
		listeners.add(levelListener);
		fireToListeners();
	}

	public void removeListener(LevelBuilderListener levelListener) {
		listeners.remove(levelListener);
	}

	public void setSpawn(BitPointInt point) {
		spawn = point;
		fireToListeners();
	}
}
