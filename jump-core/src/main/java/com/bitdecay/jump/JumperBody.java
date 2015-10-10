package com.bitdecay.jump;

public class JumperBody extends BitBody {
	public int jumpCount = 2;

	public int jumpStrength = 300;
	public int jumpDoubleJumpStrength = 150;

	public float jumpVariableHeightWindow = .2f;
	public float jumpGraceWindow = .2f;

	public JumperBody() {
		super();
	}
}
