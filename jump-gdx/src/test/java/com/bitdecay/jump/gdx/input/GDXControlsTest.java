package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.bitdecay.jump.control.PlayerAction;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MondayHopscotch on 7/20/2016.
 */
public class GDXControlsTest {

    @Test
    public void testControllerButtonReplacement() {
        MockController controller = new MockController();

        GDXControls controls = new GDXControls();
        ControllerButtonState jumpButton = new ControllerButtonState(controller, 1);
        controls.set(PlayerAction.JUMP, jumpButton);

        ControllerButtonState leftButton = new ControllerButtonState(controller, 2);
        controls.set(PlayerAction.LEFT, leftButton);

        jumpButton.buttonDown(controller, 1);
        leftButton.buttonUp(controller, 2);

        assertTrue(controls.isJustPressed(PlayerAction.JUMP));
        assertFalse(controls.isJustPressed(PlayerAction.LEFT));
    }

    @Test
    public void testJustPressed() {
        MockController controller = new MockController();

        GDXControls controls = new GDXControls();
        ControllerButtonState jumpButtonState = new ControllerButtonState(controller, 1);
        controls.set(PlayerAction.JUMP, jumpButtonState);
        controls.set(PlayerAction.LEFT, new ControllerButtonState(controller, 2));

        jumpButtonState.buttonDown(controller, 1);

        assertTrue(controls.isPressed(PlayerAction.JUMP));
        assertFalse(controls.isJustPressed(PlayerAction.JUMP));
    }

    @Test
    public void testPOVButtons() {
        MockController controller = new MockController();

        GDXControls controls = new GDXControls();
        ControllerPOVState leftButtonState = new ControllerPOVState(controller, PovDirection.west);
        controls.set(PlayerAction.LEFT, leftButtonState);

        leftButtonState.povMoved(controller, 1, PovDirection.west);

        assertTrue(controls.isPressed(PlayerAction.LEFT));

        leftButtonState.povMoved(controller, 1, PovDirection.center);

        assertFalse(controls.isPressed(PlayerAction.LEFT));

    }

    @Test
    public void testControllersNotConfigured() {
        GDXControls controls = new GDXControls();

        controls.isPressed(PlayerAction.LEFT);
        controls.isJustPressed(PlayerAction.DOWN);
    }

    @Test
    public void testEnabled() {
        MockController controller = new MockController();
        GDXControls controls = new GDXControls();
        ControllerButtonState jumpButtonState = new ControllerButtonState(controller, 1);
        controls.set(PlayerAction.JUMP, jumpButtonState);

        assertTrue("Controls enabled by default", controls.enabled);

        jumpButtonState.buttonDown(controller, 1);

        assertTrue("Controls enabled returns correctly", controls.isPressed(PlayerAction.JUMP));

        controls.disable();

        assertFalse(controls.enabled);

        assertFalse("Controls disabled does not return input", controls.isPressed(PlayerAction.JUMP));
    }

    private static class MockController implements Controller {

        @Override
        public boolean getButton(int i) {
            return i == 1;
        }

        @Override
        public float getAxis(int i) {
            return 0;
        }

        @Override
        public PovDirection getPov(int i) {
            return null;
        }

        @Override
        public boolean getSliderX(int i) {
            return false;
        }

        @Override
        public boolean getSliderY(int i) {
            return false;
        }

        @Override
        public Vector3 getAccelerometer(int i) {
            return null;
        }

        @Override
        public void setAccelerometerSensitivity(float v) {

        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public void addListener(ControllerListener controllerListener) {

        }

        @Override
        public void removeListener(ControllerListener controllerListener) {

        }
    }
}
