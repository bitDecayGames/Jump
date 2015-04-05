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
	private TileObject[][] gridObjects;
	private List<BitBody> bodies;

	/**
	 * A map of x to y to an occupying body.
	 */
	private Map<Integer, Map<Integer, Set<BitBody>>> occupiedSpaces;

	private BitPoint gravity = new BitPoint(0, 0);

	private List<BitBody> pendingAdds;
	private List<BitBody> pendingRemoves;
	public List<BitRectangle> collisions;

	public static final BitBodyProps levelBodyProps = new BitBodyProps();
	public static final String VERSION = "0.1.1";
	static {
		levelBodyProps.bodyType = BodyType.STATIC;
	}

	public BitWorld() {
		collisions = new ArrayList<BitRectangle>();
		bodies = new ArrayList<BitBody>();
		pendingAdds = new ArrayList<BitBody>();
		pendingRemoves = new ArrayList<BitBody>();
		occupiedSpaces = new HashMap<Integer, Map<Integer, Set<BitBody>>>();
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
		collisions.clear();
		//add any left over time from last call to step();
		delta += extraStepTime;
		while (delta > STEP_SIZE) {
			stepped = true;
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
		bodies.addAll(pendingAdds);
		pendingAdds.clear();
		bodies.removeAll(pendingRemoves);
		pendingRemoves.clear();

		occupiedSpaces.clear();

		// first, move everything
		bodies.parallelStream().forEach(body -> {
			// apply gravity to DYNAMIC bodies
				if (BodyType.DYNAMIC == body.props.bodyType) {
					if (body.props.gravitational) {
						BitPoint stepGravity = gravity.getScaled(delta);
						body.velocity.add(stepGravity);
					}
				}
				// then let controller handle the body
				if (body.controller != null) {
					body.controller.update(delta);
				}
				// then move all of our non-static bodies
				if (BodyType.STATIC != body.props.bodyType) {
					body.lastAttempt = body.velocity.getScaled(delta);
					body.aabb.translate(body.lastAttempt);
				}
			});

		// resolve collisions for DYNAMIC bodies against Level bodies
		bodies.stream().filter(body -> BodyType.DYNAMIC == body.props.bodyType).forEach(body -> resolveLevelCollisions(body));
		bodies.stream().filter(body -> BodyType.KINETIC == body.props.bodyType).forEach(body -> reolveKineticCollections(body));

		bodies.parallelStream().filter(body -> body.stateWatcher != null).forEach(body -> body.stateWatcher.update());
	}

	private Object reolveKineticCollections(BitBody body) {
		// 1. determine tile that x,y lives in
		BitPoint startCell = body.aabb.xy.floorDivideBy(tileSize, tileSize).minus(gridOffset);

		// 2. determine width/height in tiles
		int endX = (int) (startCell.x + Math.ceil(1.0 * body.aabb.width / tileSize));
		int endY = (int) (startCell.y + Math.ceil(1.0 * body.aabb.height / tileSize));

		BitPoint resolution = new BitPoint(0, 0);

		for (int x = (int) startCell.x; x <= endX; x++) {
			for (int y = (int) startCell.y; y <= endY; y++) {
				for (BitBody otherBody : occupiedSpaces.get(x).get(y)) {
					//					BitRectangle insec = GeomUtils.intersection(body.aabb, otherBody.aabb);
				}
			}
		}

		return null;
	}

	private void resolveLevelCollisions(BitBody body) {
		// we will always assume a body is not grounded unless it collides
		// against something on a per-step basis
		boolean grounded = false;

		// 1. determine tile that x,y lives in
		BitPoint startCell = body.aabb.xy.floorDivideBy(tileSize, tileSize).minus(gridOffset);

		// 2. determine width/height in tiles
		int endX = (int) (startCell.x + Math.ceil(1.0 * body.aabb.width / tileSize));
		int endY = (int) (startCell.y + Math.ceil(1.0 * body.aabb.height / tileSize));

		// 3. loop over those all occupied tiles
		BitResolution resolution = new BitResolution();
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
					TileObject checkObj = gridObjects[x][y];
					BitRectangle insec = GeomUtils.intersection(body.aabb, gridObjects[x][y].rect);
					resolve(resolution, body, checkObj.rect, checkObj.nValue, insec);
				}
			}
		}

		if (resolution.resolution.x != 0 || resolution.resolution.y != 0) {
			body.aabb.translate(resolution.resolution, true);
			// CONSIDER: have grounded check based on gravity direction rather than just always assuming down
			if (Math.abs(gravity.y - resolution.resolution.y) > Math.abs(gravity.y)) {
				// if the body was resolved against the gravity's y, we assume grounded.
				// CONSIDER: 4-directional gravity might become a possibility.
				grounded = true;
			} else {
				grounded = false;
			}
		}
		if (resolution.haltX) {
			body.velocity.x = 0;
		}
		if (resolution.haltY) {
			body.velocity.y = 0;
		}

		body.lastResolution = resolution.resolution;
		body.grounded = grounded;
	}

	// top/bottom collisions are slightly more lenient on velocity restrictions (velocity.y can = 0 compared to velocity.x != 0 for left/right)
	private boolean validBodyBottomCollisionLoose(BitBody body, int nValue, BitRectangle insec) {
		return (nValue & Neighbor.UP) == 0 && insec.height != body.aabb.height && insec.xy.y == body.aabb.xy.y && body.velocity.y <= 0;
	}

	private boolean validBodyTopCollisionLoose(BitBody body, int nValue, BitRectangle insec) {
		return (nValue & Neighbor.DOWN) == 0 && insec.height != body.aabb.height && insec.xy.y + insec.height == body.aabb.xy.y + body.aabb.height
				&& body.velocity.y >= 0;
	}

	private boolean validBodyRightCollision(BitBody body, int nValue, BitRectangle insec) {
		return (nValue & Neighbor.LEFT) == 0 && insec.width != body.aabb.width && insec.xy.x + insec.width == body.aabb.xy.x + body.aabb.width
				&& (insec.width < insec.height || noUpDownSpace(nValue)) && body.velocity.x > 0;
	}

	private boolean validBodyLeftCollision(BitBody body, int nValue, BitRectangle insec) {
		return (nValue & Neighbor.RIGHT) == 0 && insec.width != body.aabb.width && insec.xy.x == body.aabb.xy.x
				&& (insec.width < insec.height || noUpDownSpace(nValue)) && body.velocity.x < 0;
	}

	private boolean noUpDownSpace(int nValue) {
		return (nValue & Neighbor.UPDOWN) == Neighbor.UPDOWN;
	}

	private void resolveBodySurround() {
		// TODO Auto-generated method stub
	}

	private void resolveBodyInside(BitResolution resolution, BitBody body, BitRectangle against, int nValue, BitRectangle insec, boolean xSpeedDominant) {
		// handle where a body is entirely inside another object
		// check speed and move the character straight out based on the dominant vector axis
		if (xSpeedDominant && body.velocity.x <= 0 && (nValue & Neighbor.RIGHT) == 0) {
			//push them out to the right
			resolveRight(resolution, against, insec);
		} else if (xSpeedDominant && body.velocity.x > 0 && (nValue & Neighbor.LEFT) == 0) {
			// push them out to the left
			resolveLeft(resolution, against, insec);
		} else if (!xSpeedDominant && body.velocity.y <= 0 && (nValue & Neighbor.UP) == 0) {
			// push them out to the top
			resolveUp(resolution, against, insec);
		} else if (!xSpeedDominant && body.velocity.y > 0 && (nValue & Neighbor.DOWN) == 0) {
			// push them out to the bottom
			resolveDown(resolution, against, insec);
		}
	}

	private void resolveLeft(BitResolution resolution, BitRectangle against, BitRectangle insec) {
		resolution.resolution.x = Math.min(resolution.resolution.x, against.xy.x - (insec.xy.x + insec.width));
	}

	private void resolveDown(BitResolution resolution, BitRectangle against, BitRectangle insec) {
		resolution.resolution.y = Math.min(resolution.resolution.y, against.xy.y - (insec.xy.y + insec.height));
	}

	private void resolveUp(BitResolution resolution, BitRectangle against, BitRectangle insec) {
		resolution.resolution.y = Math.max(resolution.resolution.y, against.xy.y + against.height - insec.xy.y);
	}

	private void resolveRight(BitResolution resolution, BitRectangle against, BitRectangle insec) {
		resolution.resolution.x = Math.max(resolution.resolution.x, against.xy.x + against.width - insec.xy.x);
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
		gridObjects = level.gridObjects;
	}

	public void setGrid(TileObject[][] grid) {
		gridObjects = grid;
	}

	public TileObject[][] getGrid() {
		return gridObjects;
	}

	public int getTileSize() {
		return tileSize;
	}

	public BitPointInt getBodyOffset() {
		return gridOffset;
	}

	public void setObjects(Collection<LevelObject> otherObjects) {
		//		this.bodies = otherObjects;
	}

	private void resolve(BitResolution resolution, BitBody body, BitRectangle against, int nValue, BitRectangle insec) {
		if (insec != null) {
			collisions.add(insec);
			final boolean xSpeedDominant = Math.abs(body.velocity.x) > Math.abs(body.velocity.y);
			boolean validLeftCollision = validBodyLeftCollision(body, nValue, insec);
			boolean validRightCollision = validBodyRightCollision(body, nValue, insec);
			boolean validTopCollision = validBodyTopCollisionLoose(body, nValue, insec);
			boolean validBottomCollision = validBodyBottomCollisionLoose(body, nValue, insec);
			if (insec.equals(body.aabb)) {
				// check if the body covers the object entirely
				resolveBodyInside(resolution, body, against, nValue, insec, xSpeedDominant);
				if (xSpeedDominant) {
					resolution.haltX = true;
				} else {
					resolution.haltY = true;
				}
			} else if (insec.equals(against)) {
				// handle where another object is entirely inside the body
				resolveBodySurround();
				if (xSpeedDominant) {
					resolution.haltX = true;
				} else {
					resolution.haltY = true;
				}
			} else if (validBottomCollision && body.velocity.y <= 0 && body.grounded) {
				// body collided on its top side
				resolution.resolution.y = Math.max(resolution.resolution.y, insec.height);
				resolution.haltY = true;
			} else if (validLeftCollision) {
				// body collided on its left side
				resolution.resolution.x = Math.max(resolution.resolution.x, insec.width);
				resolution.haltX = true;
			} else if (validRightCollision) {
				// body collided on its right side
				resolution.resolution.x = Math.min(resolution.resolution.x, -insec.width);
				resolution.haltX = true;
			} else if (validTopCollision) {
				// body collided on its top side
				resolution.resolution.y = Math.min(resolution.resolution.y, -insec.height);
				resolution.haltY = true;
			} else if (validBottomCollision) {
				// body collided on its bottom side
				resolution.resolution.y = Math.max(resolution.resolution.y, insec.height);
				resolution.haltY = true;
			} else {
				// handle where the body is partially inside a box that was not resolvable with the basic checks
				if (insec.height == body.aabb.height) {
					if (body.velocity.y <= 0) {
						resolveUp(resolution, against, insec);
					} else {
						resolveDown(resolution, against, insec);
					}
					resolution.haltY = true;
				} else if (insec.width == body.aabb.width) {
					if (body.velocity.x < 0) {
						resolveRight(resolution, against, insec);
					} else if (body.velocity.x > 0) {
						resolveLeft(resolution, against, insec);
					}
					resolution.haltX = true;
				}
			}
		}
	}
}
