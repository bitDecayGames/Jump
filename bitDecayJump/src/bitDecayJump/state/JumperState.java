package bitDecayJump.state;

import com.bitdecay.common.State;
import com.fasterxml.jackson.annotation.*;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JumperState implements State {
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
	LEFT_GROUNDED_AGAINST_WALL;

	@JsonCreator
	public static JumperState forValue(String value) {
		return RIGHT_RUNNING;
	}

	@JsonValue
	public String toValue() {
		return toString();
	}
}
