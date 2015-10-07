package com.bitdecay.jump.collision;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BitWorld;
import com.bitdecay.jump.geom.BitPoint;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Monday on 9/6/2015.
 */
public class SATResolution {
    public BitPoint axis;
    public float distance;

    private List<AxisOverlap> axes = new ArrayList<>();

    @Override
    public String toString() {
        return axis.toString() + " * " + distance;
    }

    public void addAxis(BitPoint axis, float overlap) {
        if (distance < 0) {
            distance *= -1;
            axis.x *= -1;
            axis.y *= -1;
        }
        axes.add(new AxisOverlap(axis, overlap));
    }

    /**
     * This method takes into account the relative speeds of the objects being resolved. A resolution will only be
     * applied to an object that is AGAINST the relative movement between it and what it has collided against.
     *
     * This logic is here to ensure corners feel crisp when a body is landing on the edge of a platform.
     *
     * @param body the body being resolved
     * @param otherBody the other body that participated in the collision
     */
    public void compute(BitBody body, BitBody otherBody) {
        axes.sort((o1, o2) -> Float.compare(Math.abs(o1.overlap), Math.abs(o2.overlap)));
        BitPoint relativeMovement = body.lastAttempt.minus(otherBody.lastAttempt);
        float dotProd;
        for (AxisOverlap axisOver : axes) {
            dotProd = relativeMovement.dot(axisOver.axis);
            if (dotProd != 0 && !sameSign(dotProd, axisOver.overlap)) {
                axis = axisOver.axis;
                distance = axisOver.overlap;
                return;
            }
        }
        /*
         * This is a fallback, I can't think of a scenario where we would get here, but better to do
         * something than to leave ourselves open to an NPE
         */
        axis = axes.get(0).axis;
        distance = axes.get(0).overlap;
    }

    private boolean sameSign(float num1, float num2) {
        return ((num1<0) == (num2<0));
    }

    private class AxisOverlap {
        public BitPoint axis;
        public float overlap;

        public AxisOverlap(BitPoint axis, float overlap) {
            this.axis = axis;
            this.overlap = overlap;
        }
    }
}

