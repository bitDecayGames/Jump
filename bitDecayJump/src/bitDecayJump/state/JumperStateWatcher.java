package bitDecayJump.state;

import bitDecayJump.BitBody;

public class JumperStateWatcher implements BitBodyStateWatcher {

	public JumperState state;
	private BitBody body;

	public JumperStateWatcher(BitBody body) {
		this.body = body;
	}

	@Override
	public void update() {
		JumperState newState = state;
		if (body.grounded) {
			if (Math.abs(body.velocity.x) < .5) {
				if (body.lastResolution.x != 0) {
					newState = JumperState.GROUNDED_AGAINST_WALL;
				} else {
					newState = JumperState.STANDING;
				}
			} else {
				newState = JumperState.RUNNING;
			}
		} else {
			if (body.lastResolution.x != 0) {
				newState = JumperState.AIR_AGAINST_WALL;
			} else if (body.velocity.y > 50) {
				newState = JumperState.JUMPING;
			} else if (body.velocity.y < 50) {
				newState = JumperState.FALLING;
			} else {
				newState = JumperState.APEX;
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
