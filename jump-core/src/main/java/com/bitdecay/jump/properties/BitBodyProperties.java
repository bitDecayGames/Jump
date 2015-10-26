package com.bitdecay.jump.properties;

import com.bitdecay.jump.geom.BitPoint;

/**
 * Created by Monday on 10/25/2015.
 */
public class BitBodyProperties {
    /**
     * The acceleration of the body
     */
    public BitPoint acceleration = new BitPoint(0, 0);

    /**
     * The max speed of the body
     */
    public BitPoint maxSpeed = new BitPoint(150, 0);

    /**
     * A flag for whether or not gravity should affect this body
     */
    public boolean gravitational = true;
}
