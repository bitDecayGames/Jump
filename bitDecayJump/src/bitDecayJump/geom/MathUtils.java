package bitDecayJump.geom;

public class MathUtils {
	public BitPoint normalize(BitPoint xy) {
		return xy.dividedBy((float) Math.sqrt(Math.pow(xy.x, 2) + Math.pow(xy.y, 2)));
	}
}
