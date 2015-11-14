package com.bitdecay.jump.leveleditor.example.game;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.control.BitBodyController;

/**
 * Created by Monday on 11/12/2015.
 */
public class ShellController implements BitBodyController {
    boolean moving = false;
    boolean left = false;

    int speed;

    public ShellController(int speed) {
        this.speed = speed;
    }

    @Override
    public void update(float delta, BitBody body) {
        if (moving) {
            if (left) {
                if (body.lastResolution.x > 0) {
                    left = false;
                    return;
                } else {
                    body.velocity.x = -speed;
                }
            } else {
                if (body.lastResolution.x < 0) {
                    left = true;
                } else {
                    body.velocity.x = speed;
                }
            }
        } else {
            body.velocity.x = 0;
        }
    }

    @Override
    public String getStatus() {
        return moving ? "Moving" : "Stopped";
    }
}
