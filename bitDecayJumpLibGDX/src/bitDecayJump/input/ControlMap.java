package bitDecayJump.input;

import java.util.*;

import bitDecayJump.input.ControllerType.Button;

public class ControlMap {
	Map<Button, Integer> map = new HashMap<Button, Integer>();

	public int get(Button btn) {
		return map.get(btn);
	}

	public void set(Button btn, int key) {
		map.put(btn, key);
	}
}
