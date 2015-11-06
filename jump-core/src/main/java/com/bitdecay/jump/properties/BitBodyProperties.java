package com.bitdecay.jump.properties;

import com.bitdecay.jump.geom.BitPoint;

/**
 * Property object defining the configuration of a given body.
 * Created by Monday on 10/25/2015.
 */
public class BitBodyProperties {
    /**
     * The acceleration of the body while on the ground
     */
    public BitPoint acceleration = new BitPoint(0, 0);

    /**
     * The Deceleration of the body while in the air
     */
    public BitPoint airAcceleration = new BitPoint(0, 0);

    /**
     * The decelleration of the body on while on the ground
     */
    public BitPoint deceleration = new BitPoint(0, 0);

    /**
     * The Acceleration of the body while in the air
     */
    public BitPoint airDeceleration = new BitPoint(0, 0);

    /**
     * The max speed of the body
     */
    public BitPoint maxSpeed = new BitPoint(300, 0);

    /**
     * A flag for whether or not gravity should affect this body
     */
    public boolean gravitational = true;

    public float gravityModifier = 1.0f;
}
