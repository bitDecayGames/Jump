package bitDecayJump.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;

/**
 * Tracks controller state.
 * 
 * @author Monday
 * 
 */
public abstract class UpdatableController extends ControllerAdapter {

	private static final float THRESHOLD = .4f;
	public final Map<Integer, Float> axisState = new HashMap<Integer, Float>();
	public PovDirection povDirection = PovDirection.center;

	public final Map<Integer, Boolean> controllerState = new HashMap<Integer, Boolean>();
	protected final Map<Integer, Boolean> buttonsPressed = new HashMap<Integer, Boolean>();

	public ControllerType type;

	public abstract void update(float delta);

	/**
	 * To listen for if this controller is disconnected. What should we do with this?
	 * 
	 * @param controller
	 */
	@Override
	public void disconnected(Controller controller) {
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		controllerState.put(buttonIndex, true);
		buttonsPressed.put(buttonIndex, true);
		return true;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		controllerState.put(buttonIndex, false);
		return true;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		if (value > THRESHOLD) {
			axisState.put(axisIndex, (value - THRESHOLD) / (1-THRESHOLD));
		} else if (value < -THRESHOLD) {
			axisState.put(axisIndex, (value + THRESHOLD) / (1-THRESHOLD));
		} else {
			axisState.put(axisIndex, 0f);
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		povDirection = value;
		return false;
	}

	public boolean isButtonDown(int buttonIndex) {
		if (controllerState.containsKey(buttonIndex)) {
			return controllerState.get(buttonIndex);
		} else {
			return false;
		}
	}

	public PovDirection getPov() {
		return povDirection;
	}

	public float getAxis(int code) {
		if (axisState.containsKey(code)) {
			return axisState.get(code);
		} else {
			return 0;
		}
	}

	public boolean isButtonPressed(int buttonIndex) {
		Boolean pressed = false;
		if (buttonsPressed.containsKey(buttonIndex)) {
			pressed = buttonsPressed.get(buttonIndex);
		}
		buttonsPressed.put(buttonIndex, false);
		return pressed;
	}

	public void setType(ControllerType type) {
		this.type = type;
	}
}
