package bitDecayJump.render;

import bitDecayJump.BitBody;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.*;

public class PlayerController extends InputAdapter {
	private BitBody body;

	public void setBody(BitBody body) {
		this.body = body;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (body != null) {
			if (Keys.W == keycode) {
				if (body.props.grounded) {
					body.velocity.y = body.props.jumpStrength;
				}
			}
			if (Keys.A == keycode) {
				body.velocity.x = -body.props.maxSpeedX;
			} else if (Keys.D == keycode) {
				body.velocity.x = body.props.maxSpeedX;
			} else {
				body.velocity.x = 0;
			}
		}
		return true;
	}
}
