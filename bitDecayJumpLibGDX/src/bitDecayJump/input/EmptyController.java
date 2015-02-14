package bitDecayJump.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

public class EmptyController implements Controller {

	@Override
	public void addListener(ControllerListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector3 getAccelerometer(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAxis(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getButton(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PovDirection getPov(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getSliderX(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getSliderY(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ControllerListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAccelerometerSensitivity(float arg0) {
		// TODO Auto-generated method stub

	}

}
