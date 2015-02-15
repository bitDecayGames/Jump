package bitDecayJump.input;

import bitDecayJump.*;

import com.badlogic.gdx.Gdx;

public class PlayerController {
	private BitBody body;
	private ControlMap controls;
	private JumperController bodyController;

	public void setBody(BitBody body, ControlMap controls) {
		this.body = body;
		this.controls = controls;
		bodyController = new JumperController(body);
		body.controller = bodyController;
	}

	public void update() {
		if (body != null) {
			// make sure we only send one jump request through per push of the button.
			if (Gdx.input.isKeyJustPressed(controls.get(PlayerAction.JUMP))) {
				bodyController.startJump();
			} else if (!Gdx.input.isKeyPressed(controls.get(PlayerAction.JUMP))) {
				bodyController.stopJump();
			}
			if (Gdx.input.isKeyPressed(controls.get(PlayerAction.LEFT))) {
				bodyController.goLeft();
			} else if (Gdx.input.isKeyPressed(controls.get(PlayerAction.RIGHT))) {
				bodyController.goRight();
			} else {
				body.velocity.x = 0;
			}
		}
	}
}
