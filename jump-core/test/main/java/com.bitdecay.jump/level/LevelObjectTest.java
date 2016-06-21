package com.bitdecay.jump.level;

import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Monday on 6/19/2016.
 */
public class LevelObjectTest {

    @Test
    public void testSelectsPoint() {
        LevelObject obj = new LevelObject(new BitRectangle(0, 0, 10, 10)) {
            @Override
            public BitBody buildBody() {
                return new BitBody();
            }

            @Override
            public String name() {
                return null;
            }
        };

        // Should select
        assertTrue(obj.selects(new BitPointInt(0, 0)));
        assertTrue(obj.selects(new BitPointInt(0, 5)));
        assertTrue(obj.selects(new BitPointInt(5, 5)));
        assertTrue(obj.selects(new BitPointInt(10, 5)));
        assertTrue(obj.selects(new BitPointInt(10, 10)));

        // Should not select
        assertFalse(obj.selects(new BitPointInt(0, 11)));
        assertFalse(obj.selects(new BitPointInt(-1, 5)));
        assertFalse(obj.selects(new BitPointInt(11, 5)));
        assertFalse(obj.selects(new BitPointInt(5, 11)));
        assertFalse(obj.selects(new BitPointInt(5, -1)));
    }

    @Test
    public void testSelectsRect() {
        LevelObject obj = new LevelObject(new BitRectangle(0, 0, 10, 10)) {
            @Override
            public BitBody buildBody() {
                return new BitBody();
            }

            @Override
            public String name() {
                return null;
            }
        };

        // Should select
        assertTrue(obj.selects(new BitRectangle(0, 0, 10, 10)));
        assertTrue(obj.selects(new BitRectangle(-1, -1, 100, 100)));


        // Should not select
        assertFalse(obj.selects(new BitRectangle(1, 1, 8, 8)));
        assertFalse(obj.selects(new BitRectangle(4, 4, 1, 1)));
    }
}
