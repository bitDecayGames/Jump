package com.bitdecay.jump.properties;

/**
 * Created by Monday on 10/25/2015.
 */
public class JumperProperties {
    /**
     * The number of jumps a body can make before needing to touch the ground again
     */
    public int jumpCount = 2;

    /**
     * The jump strength of the primary jump
     */
    public int jumpStrength = 300;

    /**
     * The jump strength of any jump after the first
     */
    public int jumpDoubleJumpStrength = 150;

    /**
     * The window, in seconds, that holding the jump button can increase jump height
     */
    public float jumpVariableHeightWindow = .2f;

    /**
     * Grace period, in seconds, for allowing a player to either:
     *  - jump early before actually becoming grounded
     *  - jump after running off a ledge
     */
    public float jumpGraceWindow = .2f;

    /**
     * If true, hitting a platform on the way up during a jump will cause the jump
     * to immediately end.
     */
    public boolean jumpHittingHeadStopsJump = true;

    /**
     * If true, a body can wall slide when falling past a wall
     */
    public boolean wallSlideEnabled;

    /**
     * If true, a body can wall slide when falling past a wall
     */
    public boolean wallJumpEnabled;

    /**
     * How hard a body jumps off the wall when performing a wall jump
     */
    public int wallJumpLaunchPower;

    /**
     * The maximum speed at which a a body can slide down a wall
     */
    public int wallMaxSlideSpeed;
}
