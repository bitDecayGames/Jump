package com.bitdecay.jump.render;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.Facing;
import com.bitdecay.jump.common.RenderState;

public class JumperRenderStateWatcher extends AbstractRenderStateWatcher {

	public JumperRenderStateWatcher() {
	}

	@Override
	public void update(BitBody body) {
		boolean facingLeft = Facing.LEFT.equals(body.facing);
		RenderState newState = state;
		if (body.grounded) {
			if (Math.abs(body.velocity.x) < .01f) {
				if (body.lastResolution.x != 0) {
					newState = facingLeft ? JumperRenderState.LEFT_GROUNDED_AGAINST_WALL : JumperRenderState.RIGHT_GROUNDED_AGAINST_WALL;
				} else {
					newState = facingLeft ? JumperRenderState.LEFT_STANDING : JumperRenderState.RIGHT_STANDING;
				}
			} else {
				if (body.lastResolution.x == 0 ) {
					newState = facingLeft ? JumperRenderState.LEFT_RUNNING : JumperRenderState.RIGHT_RUNNING;
				} else if (body.lastResolution.x < 0) {
					newState = facingLeft ? JumperRenderState.LEFT_RUNNING : JumperRenderState.LEFT_PUSHED;
				} else {
					newState = facingLeft ? JumperRenderState.RIGHT_PUSHED : JumperRenderState.RIGHT_RUNNING;
				}
			}
		} else {
			if (body.lastResolution.x != 0) {
				newState = facingLeft ? JumperRenderState.LEFT_AIR_AGAINST_WALL : JumperRenderState.RIGHT_AIR_AGAINST_WALL;
			} else if (body.velocity.y > 50) {
				newState = facingLeft ? JumperRenderState.LEFT_JUMPING : JumperRenderState.RIGHT_JUMPING;
			} else if (body.velocity.y < -50) {
				newState = facingLeft ? JumperRenderState.LEFT_FALLING : JumperRenderState.RIGHT_FALLING;
			} else {
				newState = facingLeft ? JumperRenderState.LEFT_APEX : JumperRenderState.RIGHT_APEX;
			}
		}
		if (!newState.equals(state)) {
			state = newState;
			fireListeners(state);
		}
	}
}
