package com.bitdecay.jump.render;

import com.bitdecay.jump.common.RenderState;
import com.fasterxml.jackson.annotation.*;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JumperRenderState implements RenderState {
	RIGHT_STANDING,
	LEFT_STANDING,
	RIGHT_RUNNING,
	LEFT_RUNNING,
	RIGHT_JUMPING,
	LEFT_JUMPING,
	RIGHT_APEX,
	LEFT_APEX,
	RIGHT_FALLING,
	LEFT_FALLING,
	RIGHT_AIR_AGAINST_WALL,
	LEFT_AIR_AGAINST_WALL,
	RIGHT_GROUNDED_AGAINST_WALL,
	LEFT_GROUNDED_AGAINST_WALL,
	RIGHT_PUSHED,
	LEFT_PUSHED;

	@JsonCreator
	public static JumperRenderState forValue(String value) {
		return RIGHT_RUNNING;
	}

	@JsonValue
	public String toValue() {
		return toString();
	}
}
