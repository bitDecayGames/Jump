package bitDecayJump.input;

import java.util.*;

import com.badlogic.gdx.controllers.*;

public class ControllerManager {
	public static final ControllerManager instance;
	static {
		instance = new ControllerManager();
	}

	public static final ControllerType XBOX_CONTROLLER_TYPE = new Xbox360();
	//	private static final List<ControllerType> controllerTypes = Arrays.asList(new PS3(), new Xbox360());

	private HashMap<UpdatableController, Controller> playerMap = new HashMap<UpdatableController, Controller>();
	private HashMap<Controller, UpdatableController> controllerMap = new HashMap<Controller, UpdatableController>();

	public List<Controller> availableControllers = new ArrayList<Controller>();
	public List<Controller> allControllers = new ArrayList<Controller>();

	private ControllerAdapter connectionListener = new ControllerAdapter() {
		@Override
		public void connected(Controller controller) {
			//			detectType(controller);
			availableControllers.add(controller);
		}

		@Override
		public boolean buttonDown(Controller controller, int buttonIndex) {
			return true;
		};

		@Override
		public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
			return true;
		};

		@Override
		public void disconnected(Controller controller) {
			// Individual controllers will detect and handle their own disconnect (maybe)
			// Gdx.app.debug(DevConstants.getLogTag(), "Controller " + controller.getName() + " disconnected");
			// if (controllerMap.containsKey(controller)) {
			// // TODO: pause the game and ask player to reconnect
			//
			// // remove entries from both maps
			// playerMap.remove(controllerMap.remove(controller));
			// }
		};
	};

	private ControllerManager() {
		for (Controller controller : Controllers.getControllers()) {
			//			detectType(controller);
			availableControllers.add(controller);
			allControllers.add(controller);
		}
		Controllers.addListener(connectionListener);
	}

	//	private ControllerType detectType(Controller controller) {
	//		for (ControllerType type : controllerTypes) {
	//			if (type.isSupported(controller)) {
	//				return type;
	//			}
	//		}
	//		return null;
	//	}

	/**
	 * Maps a controllable entity to a controller if one is available
	 * 
	 * @param controllable
	 *            Player to be mapped
	 * @return true if player was mapped to a controller
	 */
	public int addControllable(UpdatableController controllable) {
		Controller controller;
		if (availableControllers.size() > 0) {
			controller = availableControllers.remove(0);
		} else {
			controller = new EmptyController();
			allControllers.add(controller);
		}

		controller.addListener(controllable);
		playerMap.put(controllable, controller);
		controllerMap.put(playerMap.get(controllable), controllable);
		controllable.setType(XBOX_CONTROLLER_TYPE);

		return allControllers.indexOf(controller);
	}

	public int removeControllable(UpdatableController controllable) {
		if (!playerMap.containsKey(controllable)) {
			return -1;
		}

		Controller controller = playerMap.get(controllable);
		controller.removeListener(controllable);
		controllerMap.remove(playerMap.get(controllable));
		playerMap.remove(controllable);

		availableControllers.add(controller);

		return allControllers.indexOf(controller);
	}
}
