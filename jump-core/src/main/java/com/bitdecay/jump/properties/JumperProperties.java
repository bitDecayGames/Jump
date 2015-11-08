package com.bitdecay.jump.properties;

import com.bitdecay.jump.annotation.ValueRange;

/**
 * Created by Monday on 10/25/2015.
 */
public class JumperProperties {
    /**
     * The number of jumps a body can make before needing to touch the ground again
     */
    @ValueRange(min = 0, max = 10)
    public int jumpCount = 2;

    /**
     * The jump strength of the primary jump
     */
    @ValueRange(min = 0, max = 1000)
    public int jumpStrength = 300;

    /**
     * The jump strength of any jump after the first
     */
    @ValueRange(min = 0, max = 1000)
    public int jumpDoubleJumpStrength = 150;

    /**
     * The window, in seconds, that holding the jump button can increase jump height
     */
    @ValueRange(min = 0, max = 2)
    public float jumpVariableHeightWindow = .2f;

    /**
     * Grace period, in seconds, for allowing a player to either:
     *  - jump early before actually becoming grounded
     *  - jump after running off a ledge
     */
    @ValueRange(min = 0, max = 1)
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
    @ValueRange(min = 0, max = 1000)
    public int wallJumpLaunchPower;

    /**
     * The maximum speed at which a a body can slide down a wall
     */
    @ValueRange(min = 0, max = 500)
    public int wallMaxSlideSpeed;
}
