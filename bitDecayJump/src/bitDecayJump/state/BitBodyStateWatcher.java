package bitDecayJump.state;

import com.bitdecay.common.StateListener;

public interface BitBodyStateWatcher {
	public void update();

	public Object getState();

	void addListener(StateListener listener);

	void removeListener(StateListener listener);
}
