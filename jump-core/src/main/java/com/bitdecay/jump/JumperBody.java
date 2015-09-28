package com.bitdecay.jump;

public class JumperBody extends BitBody {
	public int jumpStrength = 0;
	public float variableJumpWindow = 1;

	public int doubleJumpStrength = 0;
	public float jumpGraceWindow = .2f;
	public int jumpCount = 1;

	public JumperBody() {
		super();
	}

	public JumperBody(JumperBody other){
		super(other);
        this.jumpStrength = other.jumpStrength;
        this.variableJumpWindow = other.variableJumpWindow;
        this.doubleJumpStrength = other.doubleJumpStrength;
        this.jumpGraceWindow = other.jumpGraceWindow;
        this.jumpCount = other.jumpCount;
	}
}
