package bitDecayJump.collision;

import bitDecayJump.geom.BitPoint;

/**
 * Created by Monday on 9/6/2015.
 */
public class SATResolution {
    public BitPoint axis;
    public float distance;

    public SATResolution(BitPoint axis, float distance) {
        this.axis = axis;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return axis.toString() + " * " + distance;
    }
}
