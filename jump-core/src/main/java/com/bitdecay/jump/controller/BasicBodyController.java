package com.bitdecay.jump.controller;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.Facing;

public class BasicBodyController implements BitBodyController {

	protected boolean requestLeft = false;
	protected boolean requestRight = false;

	@Override
	public void update(float delta, BitBody body) {
		if (requestLeft || requestRight) {
			body.facing = requestLeft ? Facing.LEFT : Facing.RIGHT;
			if (requestLeft) {
				body.velocity.x = body.props.acceleration.x == 0 ? -body.props.maxSpeed.x : Math.max(-body.props.maxSpeed.x, body.velocity.x
						- body.props.acceleration.x * (body.velocity.x > 0 ? 2 : 1) * delta);
			} else {
				body.velocity.x = body.props.acceleration.x == 0 ? body.props.maxSpeed.x : Math.min(body.props.maxSpeed.x, body.velocity.x
						+ body.props.acceleration.x * (body.velocity.x < 0 ? 2 : 1) * delta);
			}
		} else {
			if (body.props.acceleration.x == 0) {
				body.velocity.x = 0;
			} else {
				if (body.velocity.x > 0) {
					body.velocity.x = Math.max(0, body.velocity.x - body.props.acceleration.x * delta);
				}
				if (body.velocity.x < 0) {
					body.velocity.x = Math.min(0, body.velocity.x + body.props.acceleration.x * delta);
				}
			}
		}
	}

	public void goLeft(boolean go) {
		requestLeft = go;
	}

	public void goRight(boolean go) {
		requestRight = go;
	}
}
