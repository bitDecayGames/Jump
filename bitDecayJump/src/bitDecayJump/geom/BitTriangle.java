package bitDecayJump.geom;

/**
 * Created by Monday on 9/5/2015.
 */
public class BitTriangle implements Projectable{
    public BitPoint rightAngle;
    public final float height;
    public final float width;

    public final float perpAxis;

    public BitTriangle(float x, float y, float width, float height) {
        this.rightAngle = new BitPoint(x, y);
        this.height = height;
        this.width = width;

        float rise;
        float run;

        if (height > 0 && width > 0) {
            // hypotenuse is top-right
            rise = -height;
        } else if (height > 0 && width < 0) {
            // hypotenuse is in top-left
            rise = height;
        } else if (height < 0 && width > 0) {
            // hypotenuse is in bottom-right
            rise = height;
        } else {
            // hypotenuse is in bottom-left
            rise = - height;
        }
        run = width;
        perpAxis = -run/rise;
    }

    @Override
    public BitPoint[] getProjectionPoints() {
        return new BitPoint[] {rightAngle, rightAngle.plus(width, 0), rightAngle.plus(0, height)};
    }
}
