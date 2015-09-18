package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.geom.BitPath;
import com.bitdecay.jump.geom.BitRectangle;

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
		body.bodyType = BodyType.KINETIC;
		if (direction == Direction.UP) {
			body.velocity.y = speed;
		} else if (direction == Direction.DOWN) {
			body.velocity.y = -speed;
		} else if (direction == Direction.LEFT) {
			body.velocity.x = -speed;
		} else if (direction == Direction.RIGHT) {
			body.velocity.x = speed;
		}
		return body;
	}
}
