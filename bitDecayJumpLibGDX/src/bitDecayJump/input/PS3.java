package bitDecayJump.input;

import com.badlogic.gdx.controllers.*;

public class PS3 implements ControllerType {
	public static final int BUTTON_X = 2;
	public static final int BUTTON_O = 1;
	public static final int BUTTON_S = 3;
	public static final int BUTTON_T = 0;
	public static final int BUTTON_SELECT = 8;
	public static final int BUTTON_START = 11;
	public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
	public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
	public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
	public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
	public static final int BUTTON_L1 = 4;
	public static final int BUTTON_L3 = 9;
	public static final int BUTTON_R1 = 5;
	public static final int BUTTON_R3 = 10;
	// unsure if joystick mappings
	public static final int AXIS_LEFT_X = 1; // -1 is left | +1 is right
	public static final int AXIS_LEFT_Y = 0; // -1 is up | +1 is down
	public static final int AXIS_L2 = 6; // value 0 to 1f
	// public static final int AXIS_RIGHT_X = 3; // -1 is left | +1 is right
	// public static final int AXIS_RIGHT_Y = 2; // -1 is up | +1 is down
	public static final int AXIS_R2 = 7; // value 0 to -1f

	@Override
	public boolean isSupported(Controller controller) {
		return controller.getName().toUpperCase().contains("PS3") || controller.getName().toUpperCase().contains("MOTIONINJOY");
	}

	@Override
	public String getName() {
		return "PlayStation 3";
	}

	@Override
	public int getButtonCode(Button button) {
		switch (button) {
		case JUMP:
			return BUTTON_X;
		default:
			return -1;
		}
	}

	@Override
	public int getAxis(Axis axis) {
		switch (axis) {
		case LEFTRIGHT:
			return AXIS_LEFT_X;
		case UPDOWN:
			return AXIS_LEFT_Y;
		default:
			return -1;
		}
	}
}
