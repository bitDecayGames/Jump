package bitDecayJump.controller;

import bitDecayJump.BitBody;

public class BasicBodyController extends BitBodyController {

	protected boolean requestLeft = false;
	protected boolean requestRight = false;

	public BasicBodyController(BitBody body) {
		super(body);
	}

	@Override
	public void update(float delta) {
		if (requestLeft || requestRight) {
			if (requestLeft) {
				body.velocity.x = Math.max(-body.props.maxSpeedX, body.velocity.x - body.props.accelX * delta);
			} else {
				body.velocity.x = Math.min(body.props.maxSpeedX, body.velocity.x + body.props.accelX * delta);
			}
		} else {
			if (body.velocity.x > 0) {
				body.velocity.x = Math.max(0, body.velocity.x - body.props.accelX * delta);
			} else if (body.velocity.x < 0) {
				body.velocity.x = Math.min(0, body.velocity.x + body.props.accelX * delta);
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
