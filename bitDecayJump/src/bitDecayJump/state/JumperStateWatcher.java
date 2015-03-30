package bitDecayJump.state;

import bitDecayJump.*;

public class JumperStateWatcher implements BitBodyStateWatcher {

	public JumperState state;
	private BitBody body;

	public JumperStateWatcher(BitBody body) {
		this.body = body;
	}

	@Override
	public void update() {
		boolean facingLeft = Facing.LEFT.equals(body.facing);
		JumperState newState = state;
		if (body.grounded) {
			if (Math.abs(body.velocity.x) < .5) {
				if (body.lastResolution.x != 0) {
					newState = facingLeft ? JumperState.LEFT_GROUNDED_AGAINST_WALL : JumperState.RIGHT_GROUNDED_AGAINST_WALL;
				} else {
					newState = facingLeft ? JumperState.LEFT_STANDING : JumperState.RIGHT_STANDING;
				}
			} else {
				newState = facingLeft ? JumperState.LEFT_RUNNING : JumperState.RIGHT_RUNNING;
			}
		} else {
			if (body.lastResolution.x != 0) {
				newState = facingLeft ? JumperState.LEFT_AIR_AGAINST_WALL : JumperState.RIGHT_AIR_AGAINST_WALL;
			} else if (body.velocity.y > 50) {
				newState = facingLeft ? JumperState.LEFT_JUMPING : JumperState.RIGHT_JUMPING;
			} else if (body.velocity.y < 50) {
				newState = facingLeft ? JumperState.LEFT_FALLING : JumperState.RIGHT_FALLING;
			} else {
				newState = facingLeft ? JumperState.LEFT_APEX : JumperState.RIGHT_APEX;
			}
		}
		if (!newState.equals(state)) {
			System.out.println(newState);
			state = newState;
		}
	}

	@Override
	public Object getState() {
		return state;
	}
}
