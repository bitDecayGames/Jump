package com.bitdecay.engine.animation;

import java.util.*;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.*;

public class AnimationState<T> {
	private static final TextureRegion EMPTY_REGION = new TextureRegion(new Texture(new Pixmap(10, 10, Format.RGBA8888)));
	private Map<T, Animation> animations = new HashMap<T, Animation>();
	private T currentState;
	private float elapsed;

	public AnimationState() {
	}

	public void addState(T state, Animation animation) {
		animations.put(state, animation);
	}

	public void setState(T state) {
		if (!animations.containsKey(state)) {
			currentState = null;
		} else if (!state.equals(currentState)) {
			currentState = state;
			elapsed = 0;
		}
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
}
