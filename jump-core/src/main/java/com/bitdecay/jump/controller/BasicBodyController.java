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
				body.props.velocity.x = body.props.accelX == 0 ? -body.props.maxSpeedX : Math.max(-body.props.maxSpeedX, body.props.velocity.x
						- body.props.accelX * (body.props.velocity.x > 0 ? 2 : 1) * delta);
			} else {
				body.props.velocity.x = body.props.accelX == 0 ? body.props.maxSpeedX : Math.min(body.props.maxSpeedX, body.props.velocity.x
						+ body.props.accelX * (body.props.velocity.x < 0 ? 2 : 1) * delta);
			}
		} else {
			if (body.props.accelX == 0) {
				body.props.velocity.x = 0;
			} else {
				if (body.props.velocity.x > 0) {
					body.props.velocity.x = Math.max(0, body.props.velocity.x - body.props.accelX * delta);
				}
				if (body.props.velocity.x < 0) {
					body.props.velocity.x = Math.min(0, body.props.velocity.x + body.props.accelX * delta);
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
