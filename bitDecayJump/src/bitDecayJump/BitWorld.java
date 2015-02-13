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
		while (delta > 1 / 120f) {
			internalStep(1 / 120f);
			delta -= 1 / 120f;
		}
		internalStep(delta);
	}

	private void internalStep(float delta) {
		if (delta <= 0) {
			return;
		}
		// make sure world contains everything it should
		bodies.addAll(pendingAdds);
		pendingAdds.clear();
		bodies.removeAll(pendingRemoves);
		pendingRemoves.clear();

		// apply gravity to DYNAMIC bodies
		bodies.stream().filter(body -> BodyType.DYNAMIC == body.props.bodyType).forEach(body -> {
			if (body.props.gravity) {
				body.velocity.add(gravity.getScaled(delta));
			}
			if (body.props.grounded) {
				// move all grounded bodies down one unit to force the grounded flag to be consistent
				body.aabb.xy.add(0, -1);
			}
		});

		// move all of our bodies
		bodies.stream().filter(body -> BodyType.STATIC != body.props.bodyType).forEach(body -> body.aabb.translate(body.velocity.getScaled(delta)));

		// resolve collisions for DYNAMIC bodies against Level bodies
		bodies.stream().filter(body -> BodyType.DYNAMIC == body.props.bodyType).forEach(body -> resolveLevelCollisions(body));
	}

	private void resolveLevelCollisions(BitBody body) {
		// we will always assume a body is not grounded unless it collides
		// against something on a per-step basis
		boolean grounded = false;

		// 1. determine tile that x,y lives in
		BitPointInt startCell = body.aabb.xy.divideBy(level.tileSize, level.tileSize).minus(level.gridOffset);

		// 2. determine width/height in tiles
		int endX = startCell.x + (int) Math.ceil(1.0 * body.aabb.width / level.tileSize);
		int endY = startCell.y + (int) Math.ceil(1.0 * body.aabb.height / level.tileSize);

		// 3. loop over those all occupied tiles
		BitPointInt resolution = new BitPointInt(0, 0);
		Boolean haltX = false;
		Boolean haltY = false;
		for (int x = startCell.x; x <= endX; x++) {
			for (int y = startCell.y; y <= endY; y++) {
				// ensure valid cell
				if (ArrayUtilities.onGrid(level.objects, x, y) && level.objects[x][y] != null) {
					LevelObject checkObj = level.objects[x][y];
					BitRectangle insec = GeomUtils.intersection(body.aabb, level.objects[x][y].rect);
					// first we check that we actually intersected with something
					if (insec != null) {
						final boolean xSpeedDominant = Math.abs(body.velocity.x) > Math.abs(body.velocity.y);
						boolean validLeftCollision = validBodyLeftCollision(body, checkObj, insec);
						boolean validRightCollision = validBodyRightCollision(body, checkObj, insec);
						boolean validTopCollision = validBodyTopCollisionLoose(body, checkObj, insec);
						boolean validBottomCollision = validBodyBottomCollisionLoose(body, checkObj, insec);
						if (insec.equals(body.aabb)) {
							// check if the body covers the object entirely
							resolveBodyInside(resolution, body, checkObj, insec, xSpeedDominant);
							if (xSpeedDominant) {
								haltX = true;
							} else {
								haltY = true;
							}
						} else if (insec.equals(checkObj.rect)) {
							// handle where another object is entirely inside the body
							resolveBodySurround();
							if (xSpeedDominant) {
								haltX = true;
							} else {
								haltY = true;
							}
						} else if (validBottomCollision && body.velocity.y <= 0 && body.props.grounded) {
							// body collided on its top side
							resolution.y = Math.max(resolution.y, insec.height);
							haltY = true;
						} else if (validLeftCollision) {
							// body collided on its left side
							resolution.x = Math.max(resolution.x, insec.width);
							haltX = true;
						} else if (validRightCollision) {
							System.out.println();
							// body collided on its right side
							resolution.x = Math.min(resolution.x, -insec.width);
							haltX = true;
						} else if (validTopCollision) {
							// body collided on its top side
							resolution.y = Math.min(resolution.y, -insec.height);
							haltY = true;
						} else if (validBottomCollision) {
							// body collided on its bottom side
							resolution.y = Math.max(resolution.y, insec.height);
							haltY = true;
						} else {
							// handle where the body is partially inside a box that was not resolvable with the basic checks
							if (insec.height == body.aabb.height) {
								if (body.velocity.y <= 0) {
									resolveUp(resolution, checkObj, insec);
								} else {
									resolveDown(resolution, checkObj, insec);
								}
								haltY = true;
							} else if (insec.width == body.aabb.width) {
								if (body.velocity.x <= 0) {
									resolveRight(resolution, checkObj, insec);
								} else {
									resolveLeft(resolution, checkObj, insec);
								}
								haltX = true;
							}
						}
					}
				}
			}
		}

		if (resolution.x != 0 || resolution.y != 0) {
			body.aabb.translate(resolution, true);
			if (resolution.y > 0) {
				// we resolved upward, so the feet must have hit something
				grounded = true;
			}
		}
		if (haltX) {
			body.velocity.x = 0;
		}
		if (haltY) {
			body.velocity.y = 0;
		}

		body.props.grounded = grounded;
	}

	// top/bottom collisions are slightly more lenient on velocity restrictions (velocity.y can = 0 comipared to velocity.x != 0 for left/right)
	private boolean validBodyBottomCollisionLoose(BitBody body, LevelObject checkObj, BitRectangle insec) {
		return (checkObj.nValue & Neighbor.UP) == 0 && insec.height != body.aabb.height && insec.xy.y == body.aabb.xy.y && body.velocity.y <= 0;
	}

	private boolean validBodyTopCollisionLoose(BitBody body, LevelObject checkObj, BitRectangle insec) {
		return (checkObj.nValue & Neighbor.DOWN) == 0 && insec.height != body.aabb.height && insec.xy.y + insec.height == body.aabb.xy.y + body.aabb.height
				&& body.velocity.y >= 0;
	}

	private boolean validBodyRightCollision(BitBody body, LevelObject checkObj, BitRectangle insec) {
		return (checkObj.nValue & Neighbor.LEFT) == 0 && insec.width != body.aabb.width && insec.xy.x + insec.width == body.aabb.xy.x + body.aabb.width
				&& (insec.width < insec.height || noUpDownSpace(checkObj)) && body.velocity.x > 0;
	}

	private boolean validBodyLeftCollision(BitBody body, LevelObject checkObj, BitRectangle insec) {
		return (checkObj.nValue & Neighbor.RIGHT) == 0 && insec.width != body.aabb.width && insec.xy.x == body.aabb.xy.x
				&& (insec.width < insec.height || noUpDownSpace(checkObj)) && body.velocity.x < 0;
	}

	private boolean noSideSpace(LevelObject checkObj) {
		return (checkObj.nValue & Neighbor.SIDES) == Neighbor.SIDES;
	}

	private boolean noUpDownSpace(LevelObject checkObj) {
		return (checkObj.nValue & Neighbor.UPDOWN) == Neighbor.UPDOWN;
	}

	private void resolveBodySurround() {
		// TODO Auto-generated method stub

	}

	private void resolveBodyInside(BitPointInt resolution, BitBody body, LevelObject checkObj, BitRectangle insec, boolean xSpeedDominant) {
		// handle where a body is entirely inside another object
		// check speed and move the character straight out based on the dominant vector axis
		if (xSpeedDominant && body.velocity.x <= 0 && (checkObj.nValue & Neighbor.RIGHT) == 0) {
			//push them out to the right
			resolveRight(resolution, checkObj, insec);
		} else if (xSpeedDominant && body.velocity.x > 0 && (checkObj.nValue & Neighbor.LEFT) == 0) {
			// push them out to the left
			resolveLeft(resolution, checkObj, insec);
		} else if (!xSpeedDominant && body.velocity.y <= 0 && (checkObj.nValue & Neighbor.UP) == 0) {
			// push them out to the top
			resolveUp(resolution, checkObj, insec);
		} else if (!xSpeedDominant && body.velocity.y > 0 && (checkObj.nValue & Neighbor.DOWN) == 0) {
			// push them out to the bottom
			resolveDown(resolution, checkObj, insec);
		}
	}

	//	private boolean validRightCollision() {
	//		(checkObj.nValue & Neighbor.RIGHT) == 0 && body.velocity.x < 0 && insec.width != body.aabb.width
	//				&& insec.xy.x == body.aabb.xy.x && (insec.width <= insec.height || noUpDownSpace(checkObj))
	//	}

	private void resolveLeft(BitPointInt resolution, LevelObject checkObj, BitRectangle insec) {
		resolution.x = Math.min(resolution.x, checkObj.rect.xy.x - (insec.xy.x + insec.width));
	}

	private void resolveDown(BitPointInt resolution, LevelObject checkObj, BitRectangle insec) {
		resolution.y = Math.min(resolution.y, checkObj.rect.xy.y - (insec.xy.y + insec.height));
	}

	private void resolveUp(BitPointInt resolution, LevelObject checkObj, BitRectangle insec) {
		resolution.y = Math.max(resolution.y, checkObj.rect.xy.y + checkObj.rect.height - insec.xy.y);
	}

	private void resolveRight(BitPointInt resolution, LevelObject checkObj, BitRectangle insec) {
		resolution.x = Math.max(resolution.x, checkObj.rect.xy.x + checkObj.rect.width - insec.xy.x);
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
