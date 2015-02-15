package bitDecayJump;

import java.lang.reflect.Field;

public class JumperProps extends BitBodyProps {
	public int jumpStrength = 0;
	public float variableJumpWindow = 1;

	public boolean doubleJump = true;
	public int doubleJumpStrength = 0;
	public float jumpGraceWindow = .2f;
	public int jumpCount = 1;

	public JumperProps() {
		// TODO Auto-generated constructor stub
	}

	public JumperProps(BitBodyProps props) {
		super(props);
	}

	@Override
	protected void copyProps(BitBodyProps props) {
		super.copyProps(props);
		try {
			for (Field field : JumperProps.class.getDeclaredFields()) {
				field.set(this, field.get(props));
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void set(String prop, Object value) {
		try {
			JumperProps.class.getDeclaredField(prop).set(this, value);
		} catch (Exception e) {
			super.set(prop, value);
		}
	}

	@Override
	public BitBodyProps clone() {
		return new JumperProps(this);
	}
}
