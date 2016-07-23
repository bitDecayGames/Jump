package com.bitdecay.jump.gdx.engine.utilities;

import com.badlogic.gdx.audio.Sound;

/**
 * Created by MondayHopscotch on 7/23/2016.
 */
public class SoundFXKnob {
    Sound sound;
    float volume;

    public SoundFXKnob(float volume) {
        this.volume = volume;
    }

    public long play() {
        return sound.play(volume);
    }

    public void stop(long instance) {
        sound.stop(instance);
    }

    public void stopAll() {
        sound.stop();
    }
}
