package com.bitdecay.jump.gdx.engine.utilities;

import com.badlogic.gdx.audio.Music;

/**
 * Created by MondayHopscotch on 7/23/2016.
 */
public class MusicKnob {
    Music music;
    float volume;

    public MusicKnob(float volume) {
        this.volume = volume;
    }

    public void play() {
        music.setLooping(false);
        music.setVolume(volume);
        music.play();
    }

    public void loop() {
        music.setLooping(true);
        music.setVolume(volume);
        music.play();
    }

    public void stop() {
        music.stop();
    }

    public boolean isPlaying() {
        return music != null && music.isPlaying();
    }
}
