package bitDecayJump;

import java.lang.reflect.Field;

public class BitBodyProps {
	public BodyType bodyType;
	public int accelX = 0;
	public int accelY = 0;
	public int maxSpeedX = 0;
	public int maxSpeedY = 0;
	public boolean grounded = false;
	public boolean gravity = true;
	public int jumpStrength = 0;
	public float variableJumpWindow = 1;

	public boolean doubleJump = true;
	public int doubleJumpStrength = 0;
	public float jumpGraceWindow = .2f;
	public int jumpCount = 1;

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

	public void set(String prop, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		this.getClass().getDeclaredField(prop).set(this, value);
	}
}
