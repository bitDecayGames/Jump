package com.bitdecay.engine.animation;

import java.util.*;

import com.badlogic.gdx.graphics.g2d.*;

public class AnimationState {
	private Map<String, Animation> animations = new HashMap<String, Animation>();
	private String currentState;
	private float elapsed;

	public AnimationState() {
	}

	public void addState(String state, Animation animation) {
		animations.put(state, animation);
	}

	public void setState(String state) {
		if (!state.equals(currentState) && animations.containsKey(state)) {
			currentState = state;
			elapsed = 0;
		}
	}

	public TextureRegion getNextKeyFrame(float delta) {
		elapsed += delta;
		return animations.get(currentState).getKeyFrame(elapsed);
	}
}
