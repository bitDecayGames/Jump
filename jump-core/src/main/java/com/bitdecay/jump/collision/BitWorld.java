package com.bitdecay.jump.collision;

import java.util.*;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.*;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.builder.TileObject;
import com.bitdecay.jump.properties.KineticProperties;

/**
 * A Pseudo-Physics simulation world. Will step according to all body's
 * properties, but properties are publicly accessible to allow for total
 * control.
 * 
 * @author Monday
 *
 */
public class BitWorld {
	public static final String VERSION = "0.2";
	public static final float STEP_PER_SEC = 128f;
	public static final float STEP_SIZE = 1 / STEP_PER_SEC;
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
	private Map<BitBody, SATStrategy> pendingResolutions;
	private SATStrategyComparator strategyComparator = new SATStrategyComparator();

	private Map<BitBody, Set<BitBody>> newContacts;
	private Map<BitBody, Set<BitBody>> ongoingContacts;
	private Map<BitBody, Set<BitBody>> endedContacts;

	/**
	 * A READ-ONLY easy-access to gravity. Use setGravity(...) so that other things are properly set
	 * internally.
	 */
	public static BitPoint gravity = new BitPoint(0, 0);
	private static BitPoint perpendicularGravity = new BitPoint(0, 0);

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
		newContacts = new HashMap<>();
		ongoingContacts = new HashMap<>();
		endedContacts = new HashMap<>();
	}

	public BitPoint getGravity() {
		return gravity;
	}

	public void setGravity(float x, float y) {
		this.gravity.x = x;
		this.gravity.y = y;
		this.perpendicularGravity.x = -y;
		this.perpendicularGravity.y = x;
		perpendicularGravity = perpendicularGravity.normalize();
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

		if (delta <= 0) {
			nonStep();
			return false;
		} else if (delta > 1) {
			// if we lose focus or something, never calculate more than 1 second worth of simulation
			delta = 1;
		}

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

		int remainingIterations = 10;
		boolean continueCollisions = true;
		while (continueCollisions) {
			if (remainingIterations-- <= 0) {
				break;
			}

			/**
			 * BUILD COLLISIONS
			 */
			dynamicBodies.stream().forEach(body -> {
				if (body.active) {
					buildLevelCollisions(body);
					updateExistingContact(body);
					findNewInteractions(body);
				}
			});
			/**
			 * END COLLISIONS
			 */

			continueCollisions = pendingResolutions.size() > 0;
			resolveAndApplyPendingResolutions();
		}

		dynamicBodies.parallelStream().forEach(body -> {
			if (body.active && body.renderStateWatcher != null) {
				body.renderStateWatcher.update(body);
			}
			fireExpiredContacts(body);
			fireContinuedContacts(body);
			fireNewContacts(body);
		});
	}

	public void updateControl(BitBody body, float delta) {
		if (body.controller != null) {
			body.controller.update(delta, body);
		}
	}

	public void updateDynamics(BitBody body, float delta) {
		if (body.props.gravitational) {
			body.velocity.add(gravity.scale(body.props.gravityModifier).scale(delta));
		}
	}

	public void updateKinetics(BitBody body) {
		for (BitBody child : body.children) {
			/*
			 * we make sure to move the child just slightly less
			 * than the parents to guarantee that it still
			 * collides if nothing else influences it's motion
			 */
			BitPoint influence;
			if (body.props instanceof KineticProperties && ((KineticProperties)body.props).sticky) {
				influence = body.currentAttempt.shrink(MathUtils.FLOAT_PRECISION);
			} else {
				influence = perpendicularGravity.scale(body.currentAttempt.dot(perpendicularGravity));
			}

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
		body.parents.clear();

		body.resolutionLocked = false;
		body.lastResolution.set(0, 0);
	}

	private void doAddRemoves() {
		dynamicBodies.removeAll(pendingRemoves);
		kineticBodies.removeAll(pendingRemoves);
		staticBodies.removeAll(pendingRemoves);
		pendingRemoves.stream().forEach(body -> {
			removeFromContacts(body);
		});
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
			addToContacts(body);
		}
		pendingAdds.clear();
	}

	private void resolveAndApplyPendingResolutions() {
		ArrayList<SATStrategy> list = new ArrayList<>(pendingResolutions.values());
		Collections.sort(list, strategyComparator);
		list.forEach(pending -> {
			pending.satisfy(this);
			applyResolution(pending);
		});
		pendingResolutions.clear();
	}


	private void findNewInteractions(BitBody body) {
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
					if (otherBody != body) {
						checkForNewEvent(body, otherBody, true);
					}
				}
			}
		}
	}

	private void addToContacts(BitBody body) {
		newContacts.put(body, new HashSet<>());
		ongoingContacts.put(body, new HashSet<>());
		endedContacts.put(body, new HashSet<>());
	}

	private void removeFromContacts(BitBody body) {
		newContacts.remove(body);
		ongoingContacts.remove(body);
		endedContacts.remove(body);
	}

	private void fireNewContacts(BitBody body) {
		for (ContactListener listener : body.getContactListeners()) {
			for (BitBody other : newContacts.get(body)) {
				listener.contactStarted(other);
			}
		}
		ongoingContacts.get(body).addAll(newContacts.get(body));
		newContacts.get(body).clear();
	}

	private void fireExpiredContacts(BitBody body) {
		for (ContactListener listener : body.getContactListeners()) {
			for (BitBody other : endedContacts.get(body)) {
				listener.contactEnded(other);
			}
		}
		endedContacts.get(body).clear();
	}

	private void fireContinuedContacts(BitBody body) {
		for (ContactListener listener : body.getContactListeners()) {
			for (BitBody other : ongoingContacts.get(body)) {
				listener.contact(other);
			}
		}
	}

	private void updateExistingContact(BitBody body) {
		Iterator<BitBody> iterator = ongoingContacts.get(body).iterator();
		BitBody otherBody;
		while(iterator.hasNext()) {
			otherBody = iterator.next();
			if (SATUtilities.getCollision(body.aabb, otherBody.aabb) == null) {
				iterator.remove();
				endedContacts.get(body).add(otherBody);
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
					checkForNewEvent(body, checkObj, false);
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

	private void applyResolution(SATStrategy resolution) {
		if (resolution.resolution.x != 0 || resolution.resolution.y != 0) {
			resolution.body.aabb.translate(resolution.resolution);
			BitPoint velocityAdjustment = resolution.resolution.times(BitWorld.STEP_PER_SEC);
			resolution.body.velocity.add(velocityAdjustment);
			if (BitWorld.gravity.dot(resolution.resolution) < 0) {
				resolution.body.grounded = true;
			}
		}
		resolution.body.lastResolution.add(resolution.resolution);
		resolution.body.resolutionLocked = resolution.lockingResolution;
	}

	/**
	 * A simple method that sees if there is a mid-scope overlap and
	 * possibly adds new collisions or contacts if needed.
	 *
	 * @param body
	 * @param against
	 */
	private void checkForNewEvent(BitBody body, BitBody against, boolean doContacts) {
		if (SATUtilities.getCollision(body.aabb, against.aabb) != null) {
			if (doContacts) {
				maybeFlagNewContact(body, against);
			}
			maybeCollide(body, against);
		}
	}

	private void maybeFlagNewContact(BitBody body, BitBody against) {
		if (!ongoingContacts.get(body).contains(against)) {
			if (!newContacts.containsKey(body)) {
				newContacts.put(body, new HashSet<>());
			}
			newContacts.get(body).add(against);
		}
	}

	private void maybeCollide(BitBody body, BitBody against) {
		if (!body.props.collides || !against.props.collides) {
			return;
		} else if (body.resolutionLocked) {
			return;
		} else if (BodyType.DYNAMIC.equals(body.bodyType) ^ BodyType.DYNAMIC.equals(against.bodyType)) {
			// all we have to do is take out the xor if and dynamic bodies will collide. There are still bugs, however
			if (!pendingResolutions.containsKey(body)) {
				pendingResolutions.put(body, new SATStrategy(body));
			}
			SATStrategy resolution = pendingResolutions.get(body);
			for (BitCollision collision : resolution.collisions) {
				if (collision.against == against) {
					return;
				}
			}
			resolution.collisions.add(new BitCollision(body, against));
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
		clearOutCurrentGrid();
		gridObjects = new BitBody[grid.length][grid[0].length];
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (grid[x][y] != null) {
					BitBody tileBody = grid[x][y].buildBody();
					gridObjects[x][y] = tileBody;
					addToContacts(tileBody);
				}
			}
		}
	}

	private void clearOutCurrentGrid() {
		for (int x = 0; x < gridObjects.length; x++) {
			for (int y = 0; y < gridObjects[0].length; y++) {
				removeFromContacts(gridObjects[x][y]);
			}
		}
	}

	public BitBody[][] getGrid() {
		return gridObjects;
	}

	public int getTileSize() {
		return tileSize;
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
