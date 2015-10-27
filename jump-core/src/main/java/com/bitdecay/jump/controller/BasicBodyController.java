package com.bitdecay.jump.controller;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.geom.BitPoint;

public class BasicBodyController implements BitBodyController {

	protected boolean requestLeft = false;
	protected boolean requestRight = false;

	// Here to prevent excessive instantiations
	BitPoint accel;
	BitPoint decel;

	@Override
	public void update(float delta, BitBody body) {
		accel = body.grounded ? body.props.acceleration : body.props.airAcceleration;
		decel = body.grounded ? body.props.deceleration : body.props.airDeceleration;
		if (requestLeft || requestRight) {
			body.facing = requestLeft ? Facing.LEFT : Facing.RIGHT;
			if (requestLeft) {
				if (body.velocity.x > 0) {
					if (decel.x == 0) {
						body.velocity.x = 0;
					}
					body.velocity.x = accel.x == 0 ? -body.props.maxSpeed.x : Math.max(-body.props.maxSpeed.x, body.velocity.x
							- (accel.x + decel.x) * delta);
				} else {
					body.velocity.x = accel.x == 0 ? -body.props.maxSpeed.x : Math.max(-body.props.maxSpeed.x, body.velocity.x
							- accel.x * delta);
				}
			} else {
				if (body.velocity.x > 0) {
					body.velocity.x = accel.x == 0 ? body.props.maxSpeed.x : Math.min(body.props.maxSpeed.x, body.velocity.x
							+ accel.x * delta);
				} else {
					if (decel.x == 0) {
						body.velocity.x = 0;
					}
					body.velocity.x = accel.x == 0 ? body.props.maxSpeed.x : Math.min(body.props.maxSpeed.x, body.velocity.x
							+ (accel.x + decel.x) * delta);
				}
			}
		} else {
			if (decel.x == 0) {
				body.velocity.x = 0;
			} else {
				if (body.velocity.x > 0) {
					body.velocity.x = Math.max(0, body.velocity.x - decel.x * delta);
				}
				if (body.velocity.x < 0) {
					body.velocity.x = Math.min(0, body.velocity.x + decel.x * delta);
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
