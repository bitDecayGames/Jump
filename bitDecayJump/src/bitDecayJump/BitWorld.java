package bitDecayJump;

import java.util.*;

import bitDecayJump.geom.*;
import bitDecayJump.level.*;

/**
 * A Pseudo-Physics simulation world. Will step according to all body's
 * properties, but properties are publicly accessible to allow for total
 * control.
 * 
 * @author Monday
 *
 */
public class BitWorld {
	private static final float STEP_SIZE = 1 / 120f;
	/**
	 * Holds left-over time when there isn't enough time for a full
	 * {@link #STEP_SIZE}
	 */
	private float extraStepTime = 0;

	private int tileSize;
	private BitPointInt gridOffset;
	private BitBody[][] gridObjects;
	private List<BitBody> bodies;

	/**
	 * A map of x to y to an occupying body.
	 */
	private Map<Integer, Map<Integer, Set<BitBody>>> occupiedSpaces;
	private Map<BitBody, BitResolution> pendingResolutions;

	private BitPoint gravity = new BitPoint(0, 0);

	private List<BitBody> pendingAdds;
	private List<BitBody> pendingRemoves;
	public static final List<BitRectangle> collisions = new ArrayList<BitRectangle>();

	public static final BitBodyProps levelBodyProps = new BitBodyProps();
	public static final String VERSION = "0.1.2";
	static {
		levelBodyProps.bodyType = BodyType.STATIC;
	}

	public BitWorld() {
		bodies = new ArrayList<BitBody>();
		pendingAdds = new ArrayList<BitBody>();
		pendingRemoves = new ArrayList<BitBody>();
		occupiedSpaces = new HashMap<Integer, Map<Integer, Set<BitBody>>>();
		pendingResolutions = new HashMap<BitBody, BitResolution>();
	}

	public BitPoint getGravity() {
		return gravity;
	}

	public void setGravity(float x, float y) {
		this.gravity.x = x;
		this.gravity.y = y;
	}

	public void addBody(BitBody body) {
		pendingAdds.add(body);
	}

	public void removeBody(BitBody body) {
		pendingRemoves.add(body);
	}

	/**
	 * steps the physics world in {@link BitWorld#STEP_SIZE} time steps. Any
	 * left over will be rolled over in to the next call to this method.
	 * 
	 * @param delta
	 * @return true if the world stepped, false otherwise
	 */
	public boolean step(float delta) {
		boolean stepped = false;
		//add any left over time from last call to step();
		delta += extraStepTime;
		while (delta > STEP_SIZE) {
			stepped = true;
			collisions.clear();
			internalStep(STEP_SIZE);
			delta -= STEP_SIZE;
		}
		// store off our leftover so it can be added in next time
		extraStepTime = delta;
		return stepped;
	}

	private void internalStep(final float delta) {
		if (delta <= 0) {
			return;
		}
		// make sure world contains everything it should
		bodies.removeAll(pendingRemoves);
		pendingRemoves.clear();

		bodies.addAll(pendingAdds);
		pendingAdds.clear();

		occupiedSpaces.clear();

		// first, move everything
		bodies.parallelStream().forEach(body -> {
			// apply gravity to DYNAMIC bodies
			if (BodyType.DYNAMIC == body.props.bodyType) {
				if (body.props.gravitational) {
					body.velocity.add(gravity.getScaled(delta));
				}
			} else if (BodyType.KINETIC == body.props.bodyType) {
				body.velocity.x = 30;
			}
			// then let controller handle the body
			if (body.controller != null) {
				body.controller.update(delta);
			}
			// then move all of our non-static bodies
			if (BodyType.STATIC != body.props.bodyType) {
				body.lastAttempt = body.velocity.getScaled(delta);
				body.aabb.translate(body.lastAttempt);
				if (BodyType.KINETIC == body.props.bodyType) {
					for (BitBody child : body.children) {
							child.aabb.translate(body.lastAttempt);
							// the child did attempt to move this additional amount according to our engine
							child.lastAttempt.add(body.lastAttempt);
					}
					for (BitBody child : body.children) {
						child.parent = null;
					}
					body.children.clear();
				}
			}
			// all bodies are assumed to be not grounded unless a collision happens this step.
			body.grounded = false;
		});

		// resolve collisions for DYNAMIC bodies against Level bodies-
		pendingResolutions.clear();
		bodies.stream().filter(body -> BodyType.DYNAMIC == body.props.bodyType).forEach(body -> resolveLevelCollisions(body));
		//		applyPendingResolutions();
		//		bodies.stream().filter(body -> BodyType.KINETIC == body.props.bodyType).forEach(body -> resolveKineticCollections(body));
		resolveAndApplyPendingResolutions();

		bodies.parallelStream().filter(body -> body.stateWatcher != null).forEach(body -> body.stateWatcher.update());
	}

	private void resolveAndApplyPendingResolutions() {
		for (BitBody body : pendingResolutions.keySet()) {
			pendingResolutions.get(body).satisfy();
			applyResolution(body, pendingResolutions.get(body));
		}
		pendingResolutions.clear();
	}

	private void resolveKineticCollections(BitBody kineticBody) {
		// 1. determine tile that x,y lives in
		BitPoint startCell = kineticBody.aabb.xy.floorDivideBy(tileSize, tileSize).minus(gridOffset);

		// 2. determine width/height in tiles
		int endX = (int) (startCell.x + Math.ceil(1.0 * kineticBody.aabb.width / tileSize));
		int endY = (int) (startCell.y + Math.ceil(1.0 * kineticBody.aabb.height / tileSize));

		for (int x = (int) startCell.x; x <= endX; x++) {
			if (!occupiedSpaces.containsKey(x)) {
				continue;
			}
			for (int y = (int) startCell.y; y <= endY; y++) {
				if (!occupiedSpaces.get(x).containsKey(y)) {
					continue;
				}
				for (BitBody otherBody : occupiedSpaces.get(x).get(y)) {
					if (!pendingResolutions.containsKey(otherBody)) {
						pendingResolutions.put(otherBody, new BitResolution(otherBody));
					}
					//					int resolve = resolve(pendingResolutions.get(otherBody), otherBody, kineticBody);
					int resolve = 0;
					if (resolve == Direction.UP) {
						// only add body as child if it hit the kinetic body from the top
						if (otherBody.parent == null) {
							otherBody.parent = kineticBody;
							kineticBody.children.add(otherBody);
						}
						if (Math.abs(pendingResolutions.get(otherBody).resolution.y) > 5) {
							System.out.println("way up stylers");
						}
					} else if ((resolve & Direction.NOT_UP) != 0) {
						if (Math.abs(pendingResolutions.get(otherBody).resolution.y) > 5) {
							System.out.println("other styles");
						}
					}
				}
			}
		}
	}

	private void resolveLevelCollisions(BitBody body) {
		if (!pendingResolutions.containsKey(body)) {
			pendingResolutions.put(body, new BitResolution(body));
		}
		// 1. determine tile that x,y lives in
		BitPoint startCell = body.aabb.xy.floorDivideBy(tileSize, tileSize).minus(gridOffset);

		// 2. determine width/height in tiles
		int endX = (int) (startCell.x + Math.ceil(1.0 * body.aabb.width / tileSize));
		int endY = (int) (startCell.y + Math.ceil(1.0 * body.aabb.height / tileSize));

		// 3. loop over those all occupied tiles
		//		BitResolution resolution = new BitResolution();
		for (int x = (int) startCell.x; x <= endX; x++) {
			if (!occupiedSpaces.containsKey(x)) {
				occupiedSpaces.put(x, new HashMap<Integer, Set<BitBody>>());
			}
			for (int y = (int) startCell.y; y <= endY; y++) {
				if (!occupiedSpaces.get(x).containsKey(y)) {
					occupiedSpaces.get(x).put(y, new HashSet<BitBody>());
				}
				// mark the body as occupying the current grid coordinate
				occupiedSpaces.get(x).get(y).add(body);
				// ensure valid cell
				if (ArrayUtilities.onGrid(gridObjects, x, y) && gridObjects[x][y] != null) {
					BitBody checkObj = gridObjects[x][y];
					checkForCollision(pendingResolutions.get(body), body, checkObj);
					//					resolve(pendingResolutions.get(body), body, checkObj);
				}
			}
		}
	}

	private void applyResolution(BitBody body, BitResolution resolution) {
		if (resolution.resolution.x != 0 || resolution.resolution.y != 0) {
			body.aabb.translate(resolution.resolution);
			// CONSIDER: have grounded check based on gravity direction rather than just always assuming down
			if (Math.abs(gravity.y - resolution.resolution.y) > Math.abs(gravity.y)) {
				// if the body was resolved against the gravity's y, we assume grounded.
				// CONSIDER: 4-directional gravity might become a possibility.
				body.grounded = true;
			}
		}
		if (resolution.haltX) {
			body.velocity.x = 0;
		}
		if (resolution.haltY) {
			body.velocity.y = 0;
		}

		body.lastResolution = resolution.resolution;
	}

	/**
	 * A simple method that sees if there is a collision and adds it to the
	 * {@link BitResolution} as something that needs to be handled at the time
	 * of resolution.
	 * 
	 * @param resolution
	 * @param body
	 * @param against
	 */
	private void checkForCollision(BitResolution resolution, BitBody body, BitBody against) {
		BitRectangle insec = GeomUtils.intersection(body.aabb, against.aabb);
		if (insec != null) {
			resolution.collisions.add(new BitCollision(insec, against));
		}
	}

	public BitBody createBody(BitRectangle rect, BitBodyProps props) {
		return createBody(rect.xy.x, rect.xy.y, rect.width, rect.height, props);
	}

	public BitBody createBody(float x, float y, float width, float height, BitBodyProps props) {
		BitBody body = new BitBody();
		body.aabb = new BitRectangle(x, y, width, height);
		body.props = props.clone();
		addBody(body);
		return body;
	}

	public List<BitBody> getBodies() {
		return Collections.unmodifiableList(bodies);
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public void setGridOffset(BitPointInt bodyOffset) {
		this.gridOffset = bodyOffset;
	}

	public void setLevel(Level level) {
		tileSize = level.tileSize;
		gridOffset = level.gridOffset;
		parseGrid(level.gridObjects);
	}

	public void setGrid(TileObject[][] grid) {
		parseGrid(grid);
	}

	private void parseGrid(TileObject[][] grid) {
		gridObjects = new BitBody[grid.length][grid[0].length];
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y] != null) {
					gridObjects[x][y] = grid[x][y].getBody();
				}
			}
		}
	}

	public BitBody[][] getGrid() {
		return gridObjects;
	}

	public int getTileSize() {
		return tileSize;
	}

	public BitPointInt getBodyOffset() {
		return gridOffset;
	}

	public void setObjects(Collection<BitBody> otherObjects) {
		pendingRemoves.addAll(bodies);
		pendingAdds.addAll(otherObjects);
	}
}
