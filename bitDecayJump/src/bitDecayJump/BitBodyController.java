package bitDecayJump;

public class BitBodyController {

	private BitBody body;

	// state stuff
	boolean requestJump = false;

	boolean jumping = false;

	float jumpVariableHeightWindow = 0;
	float jumpGracePeriod = 0;
	float jumpPreRequestWindow = .1f;
	float jumpPreRequestTimer = 0;

	int jumpsRemaining = 0;

	// end state stuff

	public BitBodyController(BitBody body) {
		this.body = body;
	}

	public void update(float delta) {
		if (!body.props.grounded) {
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
			jumpsRemaining = body.props.jumpCount;
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
			if (jumpsRemaining > 0 || requestValid && (body.props.grounded || jumpGracePeriod <= body.props.jumpGraceWindow)) {
				jumpsRemaining--;
				jumping = true;
				jumpVariableHeightWindow = 0;
				requestJump = false;
			} else {
			}
		}

		if (jumping && jumpVariableHeightWindow <= body.props.variableJumpWindow) {
			body.velocity.y = body.props.jumpStrength;
		} else {
			jumping = false;
		}
	}

	public void goLeft() {
		body.velocity.x = -body.props.maxSpeedX;
	}

	public void goRight() {
		body.velocity.x = body.props.maxSpeedX;

	}

	public void startJump() {
		requestJump = true;
	}

	public void stopJump() {
		jumping = false;
	}
}
