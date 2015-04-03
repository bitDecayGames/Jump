package bitDecayJump.state;

import java.util.*;

import com.bitdecay.common.StateListener;

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
