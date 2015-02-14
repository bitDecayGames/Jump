package bitDecayJump.input;

import com.badlogic.gdx.controllers.*;

/**
 * 360 controller mapping, this is not tested so the button codes may be wrong.
 * 
 * @author Monday
 * 
 */
public class Xbox360 implements ControllerType {
	/*
	 * Example possible controller names:
	 * 
	 * Controller (Gamepad for Xbox 360) Controller (XBOX 360 For Windows)
	 * Controller (Xbox 360 Wireless Receiver for Windows) Controller (Xbox
	 * wireless receiver for windows) XBOX 360 For Windows (Controller) Xbox 360
	 * Wireless Receiver Xbox Receiver for Windows (Wireless Controller) Xbox
	 * wireless receiver for windows (Controller)
	 */

	public static final int BUTTON_X = 2;
	public static final int BUTTON_Y = 3;
	public static final int BUTTON_A = 0;
	public static final int BUTTON_B = 1;
	public static final int BUTTON_BACK = 6;
	public static final int BUTTON_START = 7;
	public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
	public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
	public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
	public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
	public static final int BUTTON_LB = 4;
	public static final int BUTTON_L3 = 8;
	public static final int BUTTON_RB = 5;
	public static final int BUTTON_R3 = 9;
	public static final int AXIS_LEFT_X = 1; // -1 is left | +1 is right
	public static final int AXIS_LEFT_Y = 0; // -1 is up | +1 is down
	public static final int AXIS_LEFT_TRIGGER = 4; // value 0 to 1f
	public static final int AXIS_RIGHT_X = 3; // -1 is left | +1 is right
	public static final int AXIS_RIGHT_Y = 2; // -1 is up | +1 is down
	public static final int AXIS_RIGHT_TRIGGER = 4; // value 0 to -1f

	@Override
	public boolean isSupported(Controller controller) {
		return controller.getName().toUpperCase().contains("XBOX") && controller.getName().contains("360");
	}

	@Override
	public String getName() {
		return "XBOX 360";
	}

	@Override
	public int getButtonCode(Button button) {
		switch (button) {
		case JUMP:
			return BUTTON_A;
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
