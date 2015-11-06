package com.bitdecay.jump.render;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.common.RenderState;
import com.bitdecay.jump.common.StateListener;

public interface BitBodyStateWatcher {
	void update(BitBody body);

	RenderState getState();

	void addListener(StateListener listener);

	void removeListener(StateListener listener);
}
