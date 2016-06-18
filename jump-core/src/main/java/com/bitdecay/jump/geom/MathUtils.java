package com.bitdecay.jump.geom;

public class MathUtils {
	public static final float FLOAT_PRECISION = .0001f;
	public static final float PI_OVER_TWO = (float) (Math.PI / 2);

	public static boolean close(float f1, float f2) {
		return within(f1, f2, FLOAT_PRECISION);
	}

	public static boolean within(float f1, float f2, float precision) {
		return Math.abs(f1 - f2) <= precision;
	}

	// Currently this method treats zero as positive.
	public static boolean sameSign(float num1, float num2) {
		return ((num1<0) == (num2<0));
	}

	public static boolean opposing(float num1, float num2) {
		if (num1 > 0) {
			return num2 < 0;
		} else if (num1 < 0) {
			return num2 > 0;
		} else {
			return false;
		}
	}
}
