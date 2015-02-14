package bitDecayJump.input;

import com.badlogic.gdx.controllers.Controller;

public interface ControllerType {

	public enum Button {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		JUMP;
	}

	public enum Axis {
		LEFTRIGHT,
		UPDOWN;
	}

	/**
	 * @param controller
	 * @return true if the controller is supported by this controller type
	 */
	public boolean isSupported(Controller controller);

	public String getName();

	public abstract int getButtonCode(Button button);

	public abstract int getAxis(Axis axis);
}
