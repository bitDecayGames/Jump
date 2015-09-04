package bitDecayJump.controller;

import bitDecayJump.*;

public class JumperController extends BasicBodyController {
	// state stuff
	public boolean requestJump = false;

	public boolean jumping = false;

	public float jumpVariableHeightWindow = 0;
	public float jumpGracePeriod = 0;
	public float jumpPreRequestWindow = .1f;
	public float jumpPreRequestTimer = 0;

	public int jumpsPerformed = 0;
	public int jumpsRemaining = 0;

	// end state stuff

	@Override
	public void update(float delta, BitBody body) {
		super.update(delta, body);
		if (body.props instanceof JumperProps) {
			JumperProps props = (JumperProps) body.props;
			if (!body.grounded) {
				if (jumping) {
					jumpVariableHeightWindow += delta;
				} else if (jumpsPerformed == 0) {
					jumpGracePeriod += delta;
				}

				if (jumpsRemaining == 0 && requestJump) {
					/*
					 * Trying to jump with no jumps remaining. Time for grace
					 * period where player can 'pre-jump' and jump as they hit
					 * the ground
					 */
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
				boolean validJumpRequest = false;
				if (body.grounded && jumpPreRequestTimer <= jumpPreRequestWindow) {
					// grounded, and requesting jump within a reasonable time of touching the ground
					validJumpRequest = true;
				} else if (!body.grounded && jumpsPerformed == 0 && jumpGracePeriod <= props.jumpGraceWindow) {
					// not grounded, but within the jump grace period
					validJumpRequest = true;
				} else if (jumpsPerformed > 0 && jumpsRemaining > 0) {
					// still have jumps left
					validJumpRequest = true;
				} else {
					// all possible jump criteria failed, stop the request
					requestJump = false;
				}
				if (validJumpRequest) {
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
