package com.bitdecay.engine.animation;

import java.util.*;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.*;
import com.bitdecay.common.*;

public class AnimationState implements StateListener {
	private static final TextureRegion EMPTY_REGION = new TextureRegion(new Texture(new Pixmap(10, 10, Format.RGBA8888)));
	private Map<State, Animation> animations = new HashMap<State, Animation>();
	private State currentState;
	private float elapsed;

	public AnimationState() {
	}

	public void addState(State state, Animation animation) {
		animations.put(state, animation);
	}

	public TextureRegion getKeyFrame() {
		if (currentState == null) {
			return EMPTY_REGION;
		} else {
			return animations.get(currentState).getKeyFrame(elapsed);
		}
	}

	public void update(float delta) {
		elapsed += delta;
	}

	@Override
	public void stateChanged(State state) {
		if (!animations.containsKey(state)) {
			currentState = null;
		} else if (!state.equals(currentState)) {
			currentState = state;
			elapsed = 0;
		}
	}
}
