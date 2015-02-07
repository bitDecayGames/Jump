package bitDecayJump;

import java.util.*;
import java.util.stream.Collectors;

import bitDecayJump.geom.BitRectangle;

/**
 * A Pseudo-Physics simulation world. Will step according to all body's
 * properties, but properties are publicly accessible to allow for total
 * control.
 * 
 * @author Monday
 *
 */
public class BitWorld {
	private int tileSize;
	private List<BitBody> bodies;

	private List<BitBody> pendingAdds;
	private List<BitBody> pendingRemoves;

	public BitWorld() {
		bodies = new ArrayList<BitBody>();
		pendingAdds = new ArrayList<BitBody>();
		pendingRemoves = new ArrayList<BitBody>();
	}

	public void addBody(BitBody body) {
		pendingAdds.add(body);
	}

	public void step(float delta) {
		// make sure world contains everything it should
		bodies.addAll(pendingAdds);
		pendingAdds.clear();
		bodies.removeAll(pendingRemoves);
		pendingRemoves.clear();

		// move all our bodies
		bodies.parallelStream().filter(body -> BodyType.STATIC != body.props.bodyType).forEach(body -> body.aabb.translate(body.velocity.getScaled(delta)));

		// resolve collisions against static bodies
		List<BitBody> staticBodies = bodies.stream().filter(body -> BodyType.STATIC == body.props.bodyType).collect(Collectors.toList());
		bodies.stream().filter(body -> BodyType.DYNAMIC == body.props.bodyType).forEach(body -> resolveCollisions(body, staticBodies));
	}

	private void resolveCollisions(BitBody dynamicBody, List<BitBody> staticBodies) {
		// TODO: resolve collisions in a more sensical way. Grid style -- check
		// top/bottom/left/right first, resolve, then do diagonals or some shit.
		for (BitBody staticBody : staticBodies) {
			System.out.println("resolving " + dynamicBody + " against" + staticBody);
		}
	}

	public BitBody createBody(int x, int y, int width, int height, BitBodyProps props) {
		BitBody body = new BitBody();
		body.aabb = new BitRectangle(x, y, width, height);
		body.props = new BitBodyProps(props);
		bodies.add(body);
		return body;
	}

	public List<BitBody> getBodies() {
		return Collections.unmodifiableList(bodies);
	}
}
