package com.bitdecay.jump.state;

import com.bitdecay.jump.common.StateListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStateWatcher implements BitBodyStateWatcher {
	private List<StateListener> listeners = new ArrayList<StateListener>();
	protected JumperState state;

	@Override
	public void addListener(StateListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(StateListener listener) {
		listeners.remove(listener);
	}

	protected void fireListeners(JumperState state) {
		for (StateListener l : listeners) {
			l.stateChanged(state);
		}
	}

	@Override
	public Object getState() {
		return state;
	}
}
