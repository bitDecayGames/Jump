package com.bitdecay.jump.gdx.engine.utilities;

import com.badlogic.gdx.math.Vector2;

public class MathUtilities {

    public static Vector2 normalizeVector(Vector2 vector) {
        double vectorLength = Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));

        vector.x /= vectorLength;
        vector.y /= vectorLength;

        return vector;
    }

    public static float roundTo(float number, int base) {
        return Math.round(number * 10 * base) / (10f * base);
    }

    /**
     * Truncates two floats down to x.xx precision and compares them
     *
     * @param a float
     * @param b float
     * @return True if they are the same
     */
    public static boolean compareFloats(float a, float b) {
        if (Math.round(a * 100.0) / 100.0 == Math.round(b * 100.0) / 100.0) {
            return true;
        }
        return false;
    }

}
