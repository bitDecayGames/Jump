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

	private List<BitBody> bodies;

	private Level level;
	private Map<LevelObject, BitBody> levelBodies;

	private BitPoint gravity = new BitPoint(0, 0);

	private List<BitBody> pendingAdds;
	private List<BitBody> pendingRemoves;

	public BitWorld() {
		bodies = new ArrayList<BitBody>();
		levelBodies = new HashMap<LevelObject, BitBody>();
		pendingAdds = new ArrayList<BitBody>();
		pendingRemoves = new ArrayList<BitBody>();
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

	public void step(float delta) {
		// make sure world contains everything it should
		bodies.addAll(pendingAdds);
		pendingAdds.clear();
		bodies.removeAll(pendingRemoves);
		pendingRemoves.clear();

		// apply gravity to DYNAMIC bodies
		bodies.parallelStream().filter(body -> BodyType.DYNAMIC == body.props.bodyType).forEach(body -> body.velocity.add(gravity));

		// move all of our bodies
		bodies.parallelStream().filter(body -> BodyType.STATIC != body.props.bodyType).forEach(body -> body.aabb.translate(body.velocity.getScaled(delta)));

		// resolve collisions for DYNAMIC bodies against Level bodies
		bodies.stream().filter(body -> BodyType.DYNAMIC == body.props.bodyType).forEach(body -> resolveLevelCollisions(body));
	}

	private void resolveCollisions(BitBody dynamicBody, List<BitBody> staticBodies) {
		// TODO: resolve collisions in a more sensical way. Grid style -- check
		// top/bottom/left/right first, resolve, then do diagonals or some shit.
		for (BitBody staticBody : staticBodies) {
			System.out.println("resolving " + dynamicBody + " against" + staticBody);
		}
	}

	private void resolveLevelCollisions(BitBody body) {
		boolean collisionsDetected = false;

		// 1. determine tile that x,y lives in
		BitPointInt startCell = body.aabb.xy.divideBy(level.tileSize, level.tileSize).minus(level.gridOffset);

		// 2. determine width/height in tiles
		int endX = startCell.x + (int) Math.ceil(1.0 * body.aabb.width / level.tileSize);
		int endY = startCell.y + (int) Math.ceil(1.0 * body.aabb.height / level.tileSize);

		// 3. loop over those all occupied tiles
		// - find all tiles that the body occupies full width or height
		// - add up resolution via:
		// if (width of interstRect > height) -> resolve up/down else resolve
		// left/right
		// - move body the cumulative resolution amount
		List<BitRectangle> fullOverlaps = new ArrayList<BitRectangle>();
		List<BitRectangle> partialOverlaps = new ArrayList<BitRectangle>();
		for (int x = startCell.x; x <= endX; x++) {
			for (int y = startCell.y; y <= endY; y++) {
				if (ArrayUtilities.onGrid(level.objects, x, y) && level.objects[x][y] != null) {
					BitRectangle insec = GeomUtils.intersection(body.aabb, level.objects[x][y].rect);
					if (insec != null) {
						collisionsDetected = true;
						if (insec.width == level.tileSize || insec.height == level.tileSize) {
							fullOverlaps.add(insec);
						} else {
							partialOverlaps.add(insec);
						}
					}
				}
			}
		}

		if (collisionsDetected) {

			List<BitRectangle> recsToResolve;
			BitPointInt resolution = new BitPointInt(0, 0);
			if (fullOverlaps.size() > 0) {
				// if we have full width/height overlaps, only resolve those
				recsToResolve = fullOverlaps;
			} else {
				// otherwise resolve the partial overlaps
				recsToResolve = partialOverlaps;
				// TODO: in the case of partial overlaps, we need to figure out
				// how to narrow down what we resolve... if we resolve all of
				// them in one loop, the body can get resolved strangely.
			}

			for (BitRectangle fullColl : recsToResolve) {
				if (fullColl.xy.x == body.aabb.xy.x && fullColl.width < fullColl.height) {
					// left side
					resolution.add(fullColl.width, 0);
				} else if (fullColl.xy.x + fullColl.width == body.aabb.xy.x + body.aabb.width && fullColl.width < fullColl.height) {
					// right side
					resolution.add(-fullColl.width, 0);
				} else if (fullColl.xy.y == body.aabb.xy.y && fullColl.height < fullColl.width) {
					// bottom side
					resolution.add(0, fullColl.height);
				} else if (fullColl.xy.y + fullColl.height == body.aabb.xy.y + body.aabb.height && fullColl.height < fullColl.width) {
					// top side
					resolution.add(0, -fullColl.height);
				}
			}

			body.aabb.xy.add(resolution.x, resolution.y);
			// NOTE: need to keep track of grounded objects in some fashion.
		}
	}

	private BitBody createBody(BitRectangle rect, BitBodyProps props) {
		return createBody(rect.xy.x, rect.xy.y, rect.width, rect.height, props);
	}

	public BitBody createBody(int x, int y, int width, int height, BitBodyProps props) {
		BitBody body = new BitBody();
		body.aabb = new BitRectangle(x, y, width, height);
		body.props = new BitBodyProps(props);
		addBody(body);
		return body;
	}

	public List<BitBody> getBodies() {
		return Collections.unmodifiableList(bodies);
	}

	public void setLevel(Level level) {
		this.level = level;
		bodies.removeAll(levelBodies.values());
		levelBodies.clear();

		BitBodyProps props = new BitBodyProps();
		props.bodyType = BodyType.STATIC;
		for (LevelObject object : level.getObjects()) {
			createBody(object.rect, props);
		}
		bodies.addAll(pendingAdds);
		pendingAdds.clear();
	}
}
