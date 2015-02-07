package bitDecayJump;

import java.lang.reflect.Field;

public class BitBodyProps {
	public float accelX = 0;
	public float accelY = 0;
	public float jumpStrength = 0;
	public int maxSpeedX = 0;
	public int maxSpeedY = 0;
	public BodyType bodyType;

	public BitBodyProps() {
	}

	public BitBodyProps(BitBodyProps props) {
		try {
			for (Field field : this.getClass().getDeclaredFields()) {
				field.set(this, field.get(props));
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
