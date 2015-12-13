package com.bitdecay.jump;

import com.bitdecay.jump.properties.JumperProperties;

/**
 * A specialized body that holds state information specific to something platformer bodies
 * Created by Monday on 11/5/2015.
 */
public class JumperBody extends BitBody {
    public JumperProperties jumperProps = new JumperProperties();

    /**
     *
     */
    public int jumpsPerformed = 0;

    /**
     *
     */
    public int jumpsRemaining = 0;

    /**
     * If true, a body will jump immediately upon becoming grounded
     */
    public boolean bunnyHop;
}
