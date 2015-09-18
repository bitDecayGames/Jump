package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitRectangle;

public abstract class LevelObject {
	public BitRectangle rect;

	public LevelObject() {
		// Here for JSON
	}

	public LevelObject(BitRectangle rect) {
		this.rect = rect;
	}

	public abstract BitBody getBody();

	// will also need notion of material / path / style
}
