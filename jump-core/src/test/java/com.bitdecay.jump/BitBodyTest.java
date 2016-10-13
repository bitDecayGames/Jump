package com.bitdecay.jump;

import com.bitdecay.jump.exception.BitBodySerializeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Monday on 6/8/2016.
 */
public class BitBodyTest {

    @Test
    public void testSet() {
        BitBody testBody = new BitBody();
        assertTrue("Body type starts as static", BodyType.STATIC.equals(testBody.bodyType));

        try {
            testBody.set("bodyType", BodyType.DYNAMIC);
        } catch (BitBodySerializeException e) {
            fail("Unable to set body type to dynamic");
        }
        assertTrue("Body type change to dynamic", BodyType.DYNAMIC.equals(testBody.bodyType));
    }
}
