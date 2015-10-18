package com.bitdecay.jump.collision;

import java.util.*;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.*;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.builder.TileObject;

/**
 * A Pseudo-Physics simulation world. Will step according to all body's
 * properties, but properties are publicly accessible to allow for total
 * control.
 * 
 * @author Monday
 *
 */
public class BitWorld {
	public static final String VERSION = "0.1.2";
	public static final float STEP_SIZE = 1 / 128f;
	/**
	 * Holds left-over time when there isn't enough time for a full
	 * {@link #STEP_SIZE}
	 */
	private float extraStepTime = 0;

	private float timePassed;

	private int tileSize = 0;
	private BitPointInt gridOffset = new BitPointInt(0, 0);
	private BitBody[][] gridObjects = new BitBody[0][0];
	private List<BitBody> dynamicBodies = new ArrayList<>();
	private List<BitBody> kineticBodies = new ArrayList<>();
	private List<BitBody> staticBodies = new ArrayList<>();

	/**
	 * A map of x to y to an occupying body.
	 */
	private Map<Integer, Map<Integer, Set<BitBody>>> occupiedSpaces;
	private Map<BitBody, BitResolution> pendingResolutions;
	private Map<BitBody, Set<BitBody>> contacts;

	public static BitPoint gravity = new BitPoint(0, 0);
	public static BitPoint maxSpeed = new BitPoint(2000, 2000);

	private List<BitBody> pendingAdds;
	private List<BitBody> pendingRemoves;
	public final List<BitRectangle> resolvedCollisions = new ArrayList<>();
	public final List<BitRectangle> unresolvedCollisions = new ArrayList<>();

	public BitWorld() {
		pendingAdds = new ArrayList<>();
		pendingRemoves = new ArrayList<>();
		occupiedSpaces = new HashMap<>();
		pendingResolutions = new HashMap<>();
		contacts = new HashMap<>();
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
		if (gridObjects == null) {
			System.err.println("No level has been set into the world. Exiting...");
			System.exit(-1);
		} else if (tileSize <= 0) {
			System.err.println("Tile size has not been set. Exiting");
			System.exit(-1);
		}
		//		delta *= .05f;
		boolean stepped = false;
		//add any left over time from last call to step();
		delta += extraStepTime;
		while (delta > STEP_SIZE) {
			stepped = true;
			resolvedCollisions.clear();
			unresolvedCollisions.clear();
			internalStep(STEP_SIZE);
			delta -= STEP_SIZE;
		}
		// store off our leftover so it can be added in next time
		extraStepTime = delta;
		return stepped;
	}

	public void nonStep() {
		doAddRemoves();
	}

	private void internalStep(final float delta) {
		if (delta <= 0) {
			nonStep();
			return;
		}
		timePassed += delta;
		// make sure world contains everything it should
		doAddRemoves();

		occupiedSpaces.clear();

		/**
		 * FIRST, MOVE EVERYTHING
		 */
		dynamicBodies.stream().forEach(body -> {
			if (body.active) {
				body.previousAttempt.set(body.currentAttempt);
				body.lastPosition.set(body.aabb.xy);
				updateDynamics(body, delta);
				updateControl(body, delta);
				moveBody(body, delta);
				updateOccupiedSpaces(body);
				resetCollisions(body);
			}
		});

		kineticBodies.stream().forEach(body -> {
			if (body.active) {
				body.previousAttempt.set(body.currentAttempt);
				body.lastPosition.set(body.aabb.xy);
				updateControl(body, delta);
				moveBody(body, delta);
				updateKinetics(body);
				updateOccupiedSpaces(body);
				resetCollisions(body);
			}
		});

		staticBodies.stream().forEach(body -> {
			if (body.active) {
				updateOccupiedSpaces(body);
			}
		});
		/**
		 * END OF MOVING EVERYTHING
		 */

		/**
		 * BUILD COLLISIONS
		 */
		dynamicBodies.stream().forEach(body -> {
			if (body.active) {
				buildLevelCollisions(body);
				expireContact(body);
				findNewContact(body);
			}
		});
		/**
		 * END COLLISIONS
		 */


		resolveAndApplyPendingResolutions();

		dynamicBodies.parallelStream().forEach(body -> {
			if (body.active && body.stateWatcher != null) {
				body.stateWatcher.update(body);
			}
		});
	}

	public void updateControl(BitBody body, float delta) {
		if (body.controller != null) {
			body.controller.update(delta, body);
		}
	}

	public void updateDynamics(BitBody body, float delta) {
		if (body.gravitational) {
			body.velocity.add(gravity.scale(delta));
		}
	}

	public void updateKinetics(BitBody body) {
		for (BitBody child : body.children) {
			/*
			 * we make sure to move the child just slightly less
			 * than the parent to guarantee that it still
			 * collides if nothing else influences it's motion
			 */
			BitPoint influence = body.currentAttempt.shrink(MathUtils.FLOAT_PRECISION);
			child.aabb.translate(influence);
			// the child did attempt to move this additional amount according to our engine
			child.currentAttempt.add(influence);
		}
		body.children.clear();
	}

	public void moveBody(BitBody body, float delta) {
		body.velocity.x = Math.min(Math.abs(body.velocity.x), maxSpeed.x) * (body.velocity.x < 0 ? -1 : 1);
		body.velocity.y = Math.min(Math.abs(body.velocity.y), maxSpeed.y) * (body.velocity.y < 0 ? -1 : 1);
		body.currentAttempt = body.velocity.scale(delta);
		body.aabb.translate(body.currentAttempt);
	}

	public void resetCollisions(BitBody body) {
		// all dynamicBodies are assumed to be not grounded unless a collision happens this step.
		body.grounded = false;
		// all dynamicBodies assumed to be independent unless a lineage collision happens this step.
		body.parent = null;
	}

	private void doAddRemoves() {
		dynamicBodies.removeAll(pendingRemoves);
		kineticBodies.removeAll(pendingRemoves);
		staticBodies.removeAll(pendingRemoves);
		pendingRemoves.stream().forEach(body -> contacts.remove(body));
		pendingRemoves.clear();

		for (BitBody body : pendingAdds) {
			if (BodyType.DYNAMIC == body.bodyType) {
				dynamicBodies.add(body);
			}
			if (BodyType.KINETIC == body.bodyType) {
				kineticBodies.add(body);
			}
			if (BodyType.STATIC == body.bodyType) {
				staticBodies.add(body);
			}
			contacts.put(body, new HashSet<>());
		}
		pendingAdds.clear();
	}

	private void resolveAndApplyPendingResolutions() {
		dynamicBodies.forEach(body -> {
			if (pendingResolutions.containsKey(body)) {
				pendingResolutions.get(body).resolve(this);
				applyResolution(body, pendingResolutions.get(body));
			} else {
				body.lastResolution.set(0,0);
			}
		});
		pendingResolutions.clear();
	}

	private void expireContact(BitBody body) {
		Iterator<BitBody> iterator = contacts.get(body).iterator();
		BitBody otherBody = null;
		while(iterator.hasNext()) {
			otherBody = iterator.next();
			if (GeomUtils.intersection(body.aabb, otherBody.aabb) == null) {
				iterator.remove();
				for (ContactListener listener : body.getContactListeners()) {
					listener.contactEnded(otherBody);
				}
				for (ContactListener listener : otherBody.getContactListeners()) {
					listener.contactEnded(body);
				}
			}
		}
	}

	private void findNewContact(BitBody body) {
		// We need to update each body against the level grid so we only collide things worth colliding
		BitPoint startCell = body.aabb.xy.floorDivideBy(tileSize, tileSize).minus(gridOffset);

		int endX = (int) (startCell.x + Math.ceil(1.0 * body.aabb.width / tileSize));
		int endY = (int) (startCell.y + Math.ceil(1.0 * body.aabb.height / tileSize));

		for (int x = (int) startCell.x; x <= endX; x++) {
			if (!occupiedSpaces.containsKey(x)) {
				continue;
			}
			for (int y = (int) startCell.y; y <= endY; y++) {
				if (!occupiedSpaces.get(x).containsKey(y)) {
					continue;
				}
				for (BitBody otherBody : occupiedSpaces.get(x).get(y)) {
					if (otherBody.bodyType == BodyType.KINETIC) {
						// kinetic platforms currently also flag contacts with dynamic bodies
						checkForNewCollision(body, otherBody);
					}

					checkContact(body, otherBody);

				}
			}
		}
	}

	private void checkContact(BitBody body, BitBody otherBody) {
		BitRectangle intersection = GeomUtils.intersection(body.aabb, otherBody.aabb);
		if (intersection != null) {
			if (!contacts.get(body).contains(otherBody)) {
				contacts.get(body).add(otherBody);
				for (ContactListener listener : body.getContactListeners()) {
					listener.contactStarted(otherBody);
				}
				for (ContactListener listener : otherBody.getContactListeners()) {
					listener.contactStarted(body);
				}
			}
		}
	}

	private void buildLevelCollisions(BitBody body) {
		// 1. determine tile that x,y lives in
		BitPoint startCell = body.aabb.xy.floorDivideBy(tileSize, tileSize).minus(gridOffset);

		// 2. determine width/height in tiles
		int endX = (int) (startCell.x + Math.ceil(1.0 * body.aabb.width / tileSize));
		int endY = (int) (startCell.y + Math.ceil(1.0 * body.aabb.height / tileSize));

		// 3. loop over those all occupied tiles
		for (int x = (int) startCell.x; x <= endX; x++) {
			for (int y = (int) startCell.y; y <= endY; y++) {
				// ensure valid cell
				if (ArrayUtilities.onGrid(gridObjects, x, y) && gridObjects[x][y] != null) {
					BitBody checkObj = gridObjects[x][y];
					checkForNewCollision(body, checkObj);
				}
			}
		}
	}

	private void updateOccupiedSpaces(BitBody body) {
		// 1. determine tile that x,y lives in
		BitPoint startCell = body.aabb.xy.floorDivideBy(tileSize, tileSize).minus(gridOffset);

		// 2. determine width/height in tiles
		int endX = (int) (startCell.x + Math.ceil(1.0 * body.aabb.width / tileSize));
		int endY = (int) (startCell.y + Math.ceil(1.0 * body.aabb.height / tileSize));

		// 3. loop over those all occupied tiles
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
			}
		}
	}

	private void applyResolution(BitBody body, BitResolution resolution) {
		if (resolution.resolution.x != 0 || resolution.resolution.y != 0) {
			body.aabb.translate(resolution.resolution);
			if (resolution.haltX) {
				body.velocity.x = 0;
			}
			if (resolution.haltY) {
				body.velocity.y = 0;
			}
		}
		body.lastResolution = resolution.resolution;
	}

	/**
	 * A simple method that sees if there is a collision and adds it to the
	 * {@link BitResolution} as something that needs to be handled at the time
	 * of resolution.
	 * 
	 * @param body will always be a dynamic body with current code
	 * @param against
	 */
	private void checkForNewCollision(BitBody body, BitBody against) {
		BitRectangle insec = GeomUtils.intersection(body.aabb, against.aabb);
		if (insec != null) {
			if (!pendingResolutions.containsKey(body)) {
				pendingResolutions.put(body, new SATStrategy(body));
			}
			BitResolution resolution = pendingResolutions.get(body);
			//TODO: This can definitely be made more efficient via a hash map or something of the like
			for (BitCollision collision : resolution.collisions) {
				if (collision.otherBody == against) {
					return;
				}
			}
			resolution.collisions.add(new BitCollision(insec, against));
		}
	}

	public List<BitBody> getDynamicBodies() {
		return Collections.unmodifiableList(dynamicBodies);
	}

	public List<BitBody> getKineticBodies() {
		return Collections.unmodifiableList(kineticBodies);
	}

	public List<BitBody> getStaticBodies() {
		return Collections.unmodifiableList(staticBodies);
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public void setGridOffset(BitPointInt bodyOffset) {
		this.gridOffset = bodyOffset;
	}

	public void setLevel(Level level) {
		if (tileSize <= 0) {
			System.err.println("Tile Size cannot be less than 1");
			System.exit(-2);
		}
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
					gridObjects[x][y] = grid[x][y].buildBody();
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
		removeAllBodies();
		pendingAdds.addAll(otherObjects);
	}

	public void removeAllBodies() {
		pendingRemoves.addAll(dynamicBodies);
		pendingRemoves.addAll(kineticBodies);
		pendingRemoves.addAll(staticBodies);
		pendingAdds.clear();
	}

	public float getTimePassed() {
		return timePassed;
	}

	public void resetTimePassed() {
		timePassed = 0;
	}
}
