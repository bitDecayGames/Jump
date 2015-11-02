package com.bitdecay.jump.properties;

/**
 * Created by Monday on 11/1/2015.
 */
public class KineticProperties extends BitBodyProperties {
    /**
     * Controls if a platform 'holds on' to a body when it is sitting on top of it. <br>
     * <br>
     * sticky == true will make a better feeling moving platform when it changes direction. <br>
     * <br>
     * sticky == false will make a more realistic physics interaction when a platform changes
     * direction relative to gravity. (Can make vertical launchers)
     */
    public boolean sticky = true;
}
