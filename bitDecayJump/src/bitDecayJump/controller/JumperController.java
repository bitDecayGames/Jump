package bitDecayJump.controller;

import bitDecayJump.*;

public class JumperController extends BasicBodyController {
	// state stuff
	boolean requestJump = false;

	boolean jumping = false;

	float jumpVariableHeightWindow = 0;
	float jumpGracePeriod = 0;
	float jumpPreRequestWindow = .1f;
	float jumpPreRequestTimer = 0;

	int jumpsPerformed = 0;
	int jumpsRemaining = 0;

	// end state stuff

	public JumperController(BitBody body) {
		super(body);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (body.props instanceof JumperProps) {
			JumperProps props = (JumperProps) body.props;
			if (!body.grounded) {
				if (jumping) {
					jumpVariableHeightWindow += delta;
				} else {
					jumpGracePeriod += delta;
				}

				if (requestJump) {
					jumpPreRequestTimer += delta;
				} else {
					jumpPreRequestTimer = 0;
				}
			} else {
				jumpsPerformed = 0;
				jumpsRemaining = props.jumpCount;
				jumpGracePeriod = 0;
				jumpVariableHeightWindow = 0;
				jumpPreRequestTimer = 0;
			}

			if (requestJump && !jumping) {
				boolean requestValid = false;
				if (jumpPreRequestTimer <= jumpPreRequestWindow) {
					// if they pushed it too early, cancel request. No bunny hop mods here
					requestValid = true;
				} else {
					requestJump = false;
				}
				if (jumpsPerformed > 0 && jumpsRemaining > 0 || requestValid && (body.grounded || jumpGracePeriod <= props.jumpGraceWindow)) {
					jumpsPerformed++;
					jumpsRemaining--;
					jumping = true;
					jumpVariableHeightWindow = 0;
					requestJump = false;
				} else if (jumpsPerformed == 0 && jumpsRemaining > 1 && jumpGracePeriod > props.jumpGraceWindow) {
					/*
					 * This case handles when a player misses the jump window,
					 * but still has extra jumps they can use. (They lose their
					 * first jump for missing the window, but can use any
					 * remaining jumps)
					 */
					jumpsPerformed++;
					jumpsRemaining--;
				}
			}

			if (jumping && jumpVariableHeightWindow <= props.variableJumpWindow) {
				body.props.velocity.y = props.jumpStrength;
			} else {
				jumping = false;
			}
		}
	}

	public void startJump() {
		requestJump = true;
	}

	public void stopJump() {
		jumping = false;
	}
}
