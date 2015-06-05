package bitDecayJump.state;

import bitDecayJump.BitBody;

import com.bitdecay.common.StateListener;

public interface BitBodyStateWatcher {
	public void update(BitBody body);

	public Object getState();

	void addListener(StateListener listener);

	void removeListener(StateListener listener);
}
