package bitDecayJump.level;

import bitDecayJump.*;
import bitDecayJump.geom.*;

public class MovingObject extends LevelObject implements Updatable {

	private BitPath path;
	private float speed;
	private int direction;

	public MovingObject(BitRectangle rect, BitPath path, int direction, float speed) {
		super(rect);
		this.path = path;
		this.direction = direction;
		this.speed = speed;
	}

	@Override
	public void update(float delta) {
		//		rect.xy = path.update(delta, speed);
	}

	@Override
	public BitBody getBody() {
		BitBody body = new BitBody();
		body.aabb = rect;
		body.props = new BitBodyProps();
		body.props.bodyType = BodyType.KINETIC;
		if (direction == Direction.UP) {
			body.props.velocity.y = speed;
		} else if (direction == Direction.DOWN) {
			body.props.velocity.y = -speed;
		} else if (direction == Direction.LEFT) {
			body.props.velocity.x = -speed;
		} else if (direction == Direction.RIGHT) {
			body.props.velocity.x = speed;
		}
		return body;
	}
}
