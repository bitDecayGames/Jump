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
        controls.set(PlayerAction.JUMP, new ControllerButtonState(controller, 1));
        controls.set(PlayerAction.LEFT, new ControllerButtonState(controller, 2));
        assertTrue(controls.isJustPressed(PlayerAction.JUMP));
        assertFalse(controls.isJustPressed(PlayerAction.LEFT));
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
