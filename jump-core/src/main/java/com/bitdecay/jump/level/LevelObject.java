package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.annotation.CantInspect;
import com.bitdecay.jump.geom.BitRectangle;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class LevelObject {
	@CantInspect
	public BitRectangle rect;

	@CantInspect
	public final String uuid = UUID.randomUUID().toString();

	@CantInspect
	public List<String> objectsTriggeredByThis = new ArrayList<>();

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
