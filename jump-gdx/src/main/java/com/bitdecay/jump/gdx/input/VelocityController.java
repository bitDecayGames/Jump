package com.bitdecay.jump.gdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.control.BitBodyController;
import com.bitdecay.jump.geom.BitPoint;

/**
 * Created by Monday on 10/10/2016.
 */
public class VelocityController implements BitBodyController {
    float speed = 100;
    float friction = .93f;

    @Override
    public void update(float delta, BitBody body) {
        BitPoint movement = new BitPoint();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movement.add(0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movement.add(0, -1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) movement.add(1, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) movement.add(-1, 0);
        if (movement.len() > 0) {
            BitPoint vel = new BitPoint(body.velocity);
            vel.add(movement.normalize().scale(speed / 1));
            if (vel.len() > speed) body.velocity = vel.normalize().scale(speed);
            else body.velocity = vel;
        } else body.velocity = body.velocity.scale(friction);
    }

    @Override
    public String getStatus() {
        return "Freestyle";
    }
}
