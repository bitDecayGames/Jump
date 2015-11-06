package com.bitdecay.jump.render;

import com.bitdecay.jump.common.RenderState;
import com.bitdecay.jump.common.StateListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRenderStateWatcher implements BitBodyStateWatcher {
	private List<StateListener> listeners = new ArrayList<StateListener>();
	protected RenderState state = JumperRenderState.RIGHT_STANDING;

	@Override
	public void addListener(StateListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(StateListener listener) {
		listeners.remove(listener);
	}

	protected void fireListeners(RenderState state) {
		for (StateListener l : listeners) {
			l.stateChanged(state);
		}
	}

	@Override
	public RenderState getState() {
		return state;
	}
}
