package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitRectangle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.PROPERTY, property="objectType")
@JsonSubTypes({

		@JsonSubTypes.Type(value=MovingObject.class),
		@JsonSubTypes.Type(value=PathedLevelObject.class),
		@JsonSubTypes.Type(value=TileObject.class)
})
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
}
