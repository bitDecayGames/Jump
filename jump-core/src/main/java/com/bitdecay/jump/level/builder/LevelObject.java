package com.bitdecay.jump.level.builder;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitRectangle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public abstract class LevelObject {
	public BitRectangle rect;

	public LevelObject() {
		// Here for JSON
	}

	public LevelObject(BitRectangle rect) {
		this.rect = rect;
	}

	@JsonIgnore
	public abstract BitBody buildBody();

	public abstract String name();
}
