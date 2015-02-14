package bitDecayJump.input;

import java.util.*;

import com.badlogic.gdx.InputProcessor;

/**
 * Simple class to allow multiple {@link InputProcessor} objects to get events.
 * 
 * @author Monday
 *
 */
public class InputDistributer implements InputProcessor {
	List<InputProcessor> handlers;

	public InputDistributer() {
		handlers = new ArrayList<InputProcessor>();
	}

	public void addHandler(InputProcessor handler) {
		handlers.add(handler);
	}

	public void removeHandler(InputProcessor handler) {
		handlers.remove(handler);
	}

	@Override
	public boolean keyDown(int arg0) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.keyDown(arg0);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char arg0) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.keyTyped(arg0);
		}
		return true;
	}

	@Override
	public boolean keyUp(int arg0) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.keyUp(arg0);
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.mouseMoved(arg0, arg1);
		}
		return true;
	}

	@Override
	public boolean scrolled(int arg0) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.scrolled(arg0);
		}
		return true;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.touchDown(arg0, arg1, arg2, arg3);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.touchDragged(arg0, arg1, arg2);
		}
		return true;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		for (InputProcessor inputProcessor : handlers) {
			inputProcessor.touchUp(arg0, arg1, arg2, arg3);
		}
		return true;
	}
}
