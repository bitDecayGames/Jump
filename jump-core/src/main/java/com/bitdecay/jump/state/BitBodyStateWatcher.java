package com.bitdecay.jump.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.common.StateListener;

public interface BitBodyStateWatcher {
	public void update(BitBody body);

	public Object getState();

	void addListener(StateListener listener);

	void removeListener(StateListener listener);
}
