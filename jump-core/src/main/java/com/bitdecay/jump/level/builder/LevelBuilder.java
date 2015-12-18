package com.bitdecay.jump.level.builder;

import java.util.*;

import com.bitdecay.jump.geom.*;
import com.bitdecay.jump.level.*;
import jdk.nashorn.internal.runtime.Debug;

/**
 * A wrapper object around a level to handle adding and removing things from the
 * level. Recreates the underlying level any time changes are made. It does this
 * to ensure that all level newObjects are of the proper size.<br>
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
	public HashMap<String, LevelObject> otherObjects;

	/**
	 * Holds all triggers
	 */
	public HashMap<String, TriggerObject> triggers;

	public int tileSize;
	public TileObject[][] grid;
	public BitPointInt gridOffset;
	public DebugSpawnObject debugSpawn;

	public int theme;

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
		otherObjects = new HashMap<>();
		triggers = new HashMap<>();
		actions = new LinkedList<>();
		lastAction = -1;
	}

	public LevelBuilder(Level level) {
		setLevel(level);
	}

	public void setLevel(Level level) {
		tileSize = level.tileSize;
		grid = level.gridObjects;
		gridOffset = level.gridOffset;
		tileSize = level.tileSize;
		otherObjects = new HashMap<>();
		if (level.otherObjects != null) {
			for (LevelObject object : level.otherObjects) {
				otherObjects.put(object.uuid, object);
			}
		}
		triggers = new HashMap<>();
		if (level.triggers != null) {
			for (TriggerObject trigger : level.triggers) {
				triggers.put(trigger.uuid, trigger);
			}
		}
		debugSpawn = level.debugSpawn;
		for (LevelBuilderListener levelListener : listeners) {
			levelListener.levelChanged(level);
		}
		actions = new LinkedList<>();
		lastAction = -1;
	}

	public void createKineticObject(BitRectangle rect, List<PathPoint> path, boolean pendulum) {
		// copy the list so we don't share data between the level and the world
		List<PathPoint> listCopy = new ArrayList<>();
		for (PathPoint point : path) {
			listCopy.add(new PathPoint(point.destination.minus(rect.xy.plus(rect.width / 2, rect.height / 2)), point.speed, point.stayTime));
		}
		PathedLevelObject kObj = new PathedLevelObject(rect.copyOf(), listCopy, pendulum);

		BuilderAction createKineticAction = new AddRemoveAction(Arrays.asList(kObj), Collections.emptyList());
		pushAction(createKineticAction);
	}

	//public void createLevelObject(BitPointInt startPoint, BitPointInt endPoint, boolean oneway, int material) {
	//	List<LevelObject> newObjects = new ArrayList<>();
	//	GeomUtils.split(GeomUtils.makeRect(startPoint, endPoint), tileSize, tileSize).forEach(rect ->
	//			newObjects.add(new TileObject(rect, oneway, material)));
	//	if (newObjects.size() > 0) {
	//		BuilderAction createLevelObjectAction = new AddRemoveAction(newObjects, Collections.emptyList());
	//		pushAction(createLevelObjectAction);
	//	}
	//}
	//erik
	public void createLevelObject(BitPointInt startPoint, BitPointInt endPoint, boolean oneway, boolean foreground, int material) {
		List<LevelObject> newObjects = new ArrayList<>();
		GeomUtils.split(GeomUtils.makeRect(startPoint, endPoint), tileSize, tileSize).forEach(rect ->
				newObjects.add(new TileObject(rect, oneway, foreground, material)));
		if (newObjects.size() > 0) {
			BuilderAction createLevelObjectAction = new AddRemoveAction(newObjects, Collections.emptyList());
			pushAction(createLevelObjectAction);
		}
	}

	public void createObject(LevelObject object) {
		try {
			BuilderAction createObjectAction = new AddRemoveAction(Arrays.asList(object), Collections.emptyList());
			pushAction(createObjectAction);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void undo() {
		BuilderAction undoAction = popAction();
		if (undoAction != null) {
			undoAction.undo(this);
		}
	}

	public void redo() {
		if (lastAction < actions.size()-1) {
			BuilderAction redoAction = actions.get(lastAction+1);
			redoAction.perform(this);
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

	Set<LevelObject> addObjects(Set<LevelObject> objects) {
		Set<LevelObject> removedObjects = new HashSet<>();
		int gridX;
		int gridY;
		for (LevelObject obj : objects) {
			if (obj instanceof TileObject) {
				ensureGridFitsObject((TileObject)obj);
				gridX = (int) ((obj.rect.xy.x / tileSize) - gridOffset.x);
				gridY = (int) ((obj.rect.xy.y / tileSize) - gridOffset.y);
				if (grid[gridX][gridY] != null) {
					removedObjects.add(grid[gridX][gridY]);
				}
				grid[gridX][gridY] = (TileObject)obj;
				updateNeighbors(gridX, gridY);
			} else if (obj instanceof TriggerObject) {
				triggers.put(obj.uuid, (TriggerObject) obj);
			} else if (obj instanceof DebugSpawnObject) {
				debugSpawn = (DebugSpawnObject) obj;
			} else {
				otherObjects.put(obj.uuid, obj);
			}
		}
		fireToListeners();
		return removedObjects;
	}

	/**
	 *
	 * @param objects
	 * @return set of any additionally removed objects that were collateral of removing the provided objects
	 */
	Set<LevelObject> removeObjects(Set<LevelObject> objects) {
		Set<LevelObject> additionalRemovedObjects = new HashSet<>();
		additionalRemovedObjects.addAll(objects);
		// clean up out of other newObjects
		objects.forEach(object -> {
			if (object == debugSpawn) {
				debugSpawn = null;
			}
			otherObjects.remove(object.uuid);
			triggers.remove(object.uuid);

			Iterator<TriggerObject> iterator = triggers.values().iterator();
			TriggerObject trigger;
			while (iterator.hasNext()) {
				trigger = iterator.next();
				if (trigger.triggerer.uuid.equals(object.uuid) || trigger.triggeree.uuid.equals(object.uuid)) {
					// need to remove the trigger.
					iterator.remove();
					additionalRemovedObjects.add(trigger);
				}
			}
		});
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
		return additionalRemovedObjects;
	}

	public void fireToListeners() {
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
		updateOwnNeighbors(x, y);

		updateOwnNeighbors(x+1, y);
		updateOwnNeighbors(x-1, y);
		updateOwnNeighbors(x, y+1);
		updateOwnNeighbors(x, y-1);
	}

	public void updateOwnNeighbors(int x, int y) {
		if (!ArrayUtilities.onGrid(grid, x, y) || grid[x][y] == null) {
			return;
		}

		// check right
		if (ArrayUtilities.onGrid(grid, x + 1, y) && grid[x + 1][y] != null) {
			//if (grid[x+1][y].oneway) {
			//	grid[x][y].collideNValue &= Direction.NOT_RIGHT;
			//} else {
				grid[x][y].collideNValue |= Direction.RIGHT;
			//}
			grid[x][y].renderNValue |= Direction.RIGHT;
		} else {
			grid[x][y].collideNValue &= Direction.NOT_RIGHT;
			grid[x][y].renderNValue &= Direction.NOT_RIGHT;
		}
		// check left
		if (ArrayUtilities.onGrid(grid, x - 1, y) && grid[x - 1][y] != null) {
			//if (grid[x-1][y].oneway) {
			//	grid[x][y].collideNValue &= Direction.NOT_LEFT;
			//} else {
				grid[x][y].collideNValue |= Direction.LEFT;
			//}
			grid[x][y].renderNValue |= Direction.LEFT;
		} else {
			grid[x][y].collideNValue &= Direction.NOT_LEFT;
			grid[x][y].renderNValue &= Direction.NOT_LEFT;
		}
		// check up
		if (ArrayUtilities.onGrid(grid, x, y + 1) && grid[x][y + 1] != null) {
			if ((grid[x][y].oneway && grid[x][y+1].oneway)||(grid[x][y].foreground && grid[x][y+1].foreground)) {
				grid[x][y].collideNValue |= Direction.UP;
			} else if (grid[x][y+1].oneway || grid[x][y+1].foreground ) {
				grid[x][y].collideNValue &= Direction.NOT_UP;
			} else {
				grid[x][y].collideNValue |= Direction.UP;
			}
			grid[x][y].renderNValue |= Direction.UP;
		} else {
			grid[x][y].collideNValue &= Direction.NOT_UP;
			grid[x][y].renderNValue &= Direction.NOT_UP;
		}
		// check down
		if (ArrayUtilities.onGrid(grid, x, y - 1) && grid[x][y - 1] != null) {
			//if (grid[x][y-1].oneway) {
			//	grid[x][y].collideNValue &= Direction.NOT_DOWN;
			//} else {
				grid[x][y].collideNValue |= Direction.DOWN;
			//}
			grid[x][y].renderNValue |= Direction.DOWN;
		} else {
			grid[x][y].collideNValue &= Direction.NOT_DOWN;
			grid[x][y].renderNValue &= Direction.NOT_DOWN;
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
			BuilderAction deleteAction = new AddRemoveAction(Collections.emptyList(), selection);
			pushAction(deleteAction);
			selection.clear();
		}
	}

	public void selectObjects(BitRectangle selectionArea, boolean add) {
		if (!add) {
			selection.clear();
		}
		triggers.values().forEach(trigger -> {
			if (trigger.selects(selectionArea)) {
				selection.add(trigger);
				return;
			}
		});
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				LevelObject object = grid[x][y];
				if (object != null && selectionArea.contains(object.rect)) {
					selection.add(object);
				}
			}
		}
		otherObjects.values().forEach(object -> {
			if (selectionArea.contains(object.rect)) {
				selection.add(object);
			}
		});
		if (debugSpawn != null) {
			// This is gross. I don't like.
			if (debugSpawn.selects(selectionArea)) {
				selection.add(debugSpawn);
				return;
			}
		}
	}

	public void selectObject(BitPointInt point, boolean add) {
		selectObject(point, add, true);
	}

	public void selectObject(BitPointInt point, boolean add, boolean includeGridObjects) {
		if (!add) {
			selection.clear();
		}
		triggers.values().forEach(trigger -> {
			if (trigger.selects(point)) {
				selection.add(trigger);
				return;
			}
		});
		otherObjects.values().forEach(object -> {
			if (object.selects(point)) {
				selection.add(object);
				return;
			}
		});
		if (includeGridObjects) {
			for (int x = 0; x < grid.length; x++) {
				for (int y = 0; y < grid[0].length; y++) {
					LevelObject object = grid[x][y];
					if (object != null && object.selects(point)) {
						selection.add(object);
						return;
					}
				}
			}
		}
		if (debugSpawn != null) {
			// This is gross. I don't like.
			if (debugSpawn.selects(point)) {
				selection.add(debugSpawn);
				return;
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

		TileObject[][] optimizedGrid;
		BitPointInt optimizedOffset = new BitPointInt();

		if (min.x == Integer.MAX_VALUE || min.y == Integer.MAX_VALUE || max.x == Integer.MIN_VALUE || max.y == Integer.MIN_VALUE) {
			min.x = -START_SIZE/2 * tileSize;
			min.y = -START_SIZE/2 * tileSize;
			max.x = START_SIZE/2 * tileSize;
			max.y = START_SIZE/2 * tileSize;
			optimizedGrid = new TileObject[START_SIZE][START_SIZE];
		} else {
			optimizedGrid = new TileObject[(max.x - min.x) / tileSize][(max.y - min.y) / tileSize];
			int xOffset = (min.x / tileSize) - gridOffset.x;
			int yOffset = (min.y / tileSize) - gridOffset.y;
			for (int x = 0; x < optimizedGrid.length; x++) {
				for (int y = 0; y < optimizedGrid[0].length; y++) {
					optimizedGrid[x][y] = grid[x+xOffset][y+yOffset];
				}
			}
		}

		optimizedOffset.x = (min.x / tileSize);
		optimizedOffset.y = (min.y / tileSize);

		optimizedLevel.gridOffset = optimizedOffset;
		optimizedLevel.gridObjects = optimizedGrid;
		optimizedLevel.otherObjects = new ArrayList<>(otherObjects.values());
		optimizedLevel.triggers = new ArrayList<>(triggers.values());
		optimizedLevel.debugSpawn = debugSpawn;
		optimizedLevel.theme = theme;

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

	public void setDebugSpawn(DebugSpawnObject spawn) {
		debugSpawn = spawn;
		fireToListeners();
	}

	public void setTheme(int id) {
		this.theme = id;
	}

	public boolean hasChanges() {
		return actions.size() > 0 && lastAction > -1;
	}
}
