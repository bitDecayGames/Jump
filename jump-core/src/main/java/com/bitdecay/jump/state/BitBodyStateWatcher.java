package com.bitdecay.jump.state;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.common.State;
import com.bitdecay.jump.common.StateListener;

public interface BitBodyStateWatcher {
	void update(BitBody body);

	State getState();

	void addListener(StateListener listener);

	void removeListener(StateListener listener);
}
