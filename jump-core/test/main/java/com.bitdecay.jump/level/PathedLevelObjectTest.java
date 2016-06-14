package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.control.PathedBodyController;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.properties.KineticProperties;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 6/14/2016.
 */
public class PathedLevelObjectTest {

    @Test
    public void testNoArgConstructorPresent() {
        new PathedLevelObject();
    }

    @Test
    public void testBuildBody() {
        BitRectangle bodyShape = new BitRectangle(0, 0, 10, 10);

        List<PathPoint> bodyPath = Arrays.asList(
                new PathPoint(new BitPoint(0, 0), 10, 0),
                new PathPoint(new BitPoint(10, 0), 10, 0));

        boolean pendulum = true;

        PathedLevelObject testObj = new PathedLevelObject(bodyShape, bodyPath, pendulum);

        BitBody bitBody = testObj.buildBody();

        assertTrue("Body is kinetic", BodyType.KINETIC.equals(bitBody.bodyType));
        assertTrue("Body props is correct type", bitBody.props instanceof KineticProperties);

        KineticProperties bodyProps = (KineticProperties) bitBody.props;
        assertTrue("Body is non-sticky by default", bodyProps.sticky == false);

        assertTrue("Body has a bounding box", bitBody.aabb != null);
        assertTrue("Body is at correct location", bitBody.aabb.xy.equals(bodyShape.xy));
        assertTrue("Bounding box has correct width", bitBody.aabb.getWidth() == bodyShape.getWidth());
        assertTrue("Bounding box has correct height", bitBody.aabb.getHeight() == bodyShape.getHeight());

        assertTrue("Controller is correct type", bitBody.controller instanceof PathedBodyController);

        PathedBodyController bodyController = (PathedBodyController) bitBody.controller;
        assertTrue("Controller has correct path points", bodyController.path.equals(bodyPath));
        assertTrue("Controller is set to pendulum", bodyController.pendulum == pendulum);
    }
}
