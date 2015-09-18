package com.bitdecay.jump.leveleditor.input;

import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter class for InputProcessor. Contains some useful fields for use by
 * children. Has No-Op overrides of InputProcessor methods so children can only
 * override those that concern them. Handles and maintains keyboard state.
 *
 * @author MondayHopscotch
 */
public abstract class KeyboardState implements InputProcessor {

    // HAX
    protected boolean changed = false;

    protected final Map<Integer, Boolean> keyboardState = new HashMap<Integer, Boolean>();
    protected final Map<Integer, Boolean> keyPressed = new HashMap<Integer, Boolean>();

    public KeyboardState() {
    }

    private boolean focused = false;

    /**
     * The business end of the controller. This will be called every game loop.
     * Override to implement various control schemes. Call this from the
     * overridden if you wish to check special commands such as quit/debug
     *
     * @param delta how much time as passed since last being called
     */
    public void update(float delta) {

    }

    /**
     * Clears any input
     */
    public void clearInput() {
        keyboardState.clear();
    }

    /* InputProcessor methods */
    @Override
    public boolean keyDown(int keycode) {
        setKey(keycode, true);
        setPressed(keycode, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        setKey(keycode, false);
        setPressed(keycode, false);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    protected Boolean getKey(int key) {
        return getValue(keyboardState, key);
    }

    protected void setKey(int key, Boolean value) {
        keyboardState.put(key, value);
    }

    protected Boolean getPressed(int key) {
        Boolean value = keyPressed.remove(key);
        return value == null ? false : value;
    }

    protected void setPressed(int key, Boolean value) {
        keyPressed.put(key, value);
    }

    /**
     * Safe getter that returns false if the map value was null
     *
     * @param map
     * @param key
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected Boolean getValue(Map map, int key) {
        Boolean value = (Boolean) map.get(key);
        return value != null ? value : false;
    }

    /**
     * Called whenever this controller gains control. Override to do any special
     * handling.
     */
    public void gainedControl() {
    }

    /**
     * Called whenever this controller loses control. Override to do any special
     * handling.
     */
    public void lostControl() {
    }
}
