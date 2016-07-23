package com.bitdecay.jump.gdx.engine.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.bitdecay.jump.gdx.engine.GameFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Sound Utility for easy access to all sound files in the system. All sound files should be in mp3 format. <br>
 * <br>
 * Sound Clips will be retrieved by name from assets/sounds <br>
 * <br>
 * Music will be retrieved by name from assets/sounds/music <br>
 * <br>
 * Currently this class does NOT use {@link AssetManager} because we load sounds on the fly the first time they are needed. Consider changing this at some
 * point.
 *
 * @author Monday
 * @version 0.1 (March 2014)
 */
public class SoundUtilities {

    public static Map<String, SoundFXKnob> soundFXMap;
    public static Map<String, MusicKnob> musicMap;

    static Map<String, String> nameToFileMap;

    public static Music currentMusic;

    public static void initialize() {
        soundFXMap = new HashMap<>();
        musicMap = new HashMap<>();

        nameToFileMap = new HashMap<>();
    }

    public static void preLoadSoundFX(String name, String fileName, float volume) {
        if (nameToFileMap.containsKey(name)) {
            throw new RuntimeException("Audio file with name '" + name + "' has already been preloaded");
        }

        if (nameToFileMap.containsValue(fileName)) {
            throw new RuntimeException("Audio file '" + fileName + "' has already been loaded under a different name");
        }

        GameFactory.getAssetManager().load(fileName, Sound.class);
        soundFXMap.put(name, new SoundFXKnob(volume));
    }

    public static void preLoadMusic(String name, String fileName, float volume) {
        if (nameToFileMap.containsKey(name)) {
            throw new RuntimeException("Audio file with name '" + name + "' has already been preloaded");
        }

        if (nameToFileMap.containsValue(fileName)) {
            throw new RuntimeException("Audio file '" + fileName + "' has already been loaded under a different name");
        }

        GameFactory.getAssetManager().load(fileName, Music.class);
        musicMap.put(name, new MusicKnob(volume));
    }

    public static long playSoundFX(String name) {
        if (soundFXMap.containsKey(name)) {
            SoundFXKnob soundEffect = soundFXMap.get(name);
            if (soundEffect.sound == null) {
                String file = nameToFileMap.get(name);
                if (!GameFactory.getAssetManager().isLoaded(file)) {
                    throw new RuntimeException("Asset '" + name + "' with file '" + file + "' being used before it is fully loaded");
                }
                soundEffect.sound = GameFactory.getAssetManager().get(file);
            }
            return soundEffect.play();
        } else {
            throw new RuntimeException("Attempting to play sound effect '" + name + "' but no such sound has been pre loaded yet");
        }
    }

    public static void stopSoundFX(String name) {
        stopSoundFX(name, Long.MIN_VALUE);
    }

    public static void stopSoundFX(String name, long id) {
        if (soundFXMap.containsKey(name)) {
            SoundFXKnob soundEffect = soundFXMap.get(name);
            if (soundEffect.sound == null) {
                String file = nameToFileMap.get(name);
                if (!GameFactory.getAssetManager().isLoaded(file)) {
                    throw new RuntimeException("Asset '" + name + "' with file '" + file + "' being used before it is fully loaded");
                }
                soundEffect.sound = GameFactory.getAssetManager().get(file);
            }
            if (id == Long.MIN_VALUE) {
                soundEffect.stopAll();
            } else {
                soundEffect.stop(id);
            }
        } else {
            throw new RuntimeException("Attempting to play sound effect '" + name + "' but no such sound has been pre loaded yet");
        }
    }

    public static void playMusic(String name) {
        getMusic(name).play();
    }

    public static void loopMusic(String name) {
        getMusic(name).stop();
    }

    public static void stopMusic(String name) {
        getMusic(name).stop();
    }

    public static void stopAllMusic() {
        for (MusicKnob musicKnob : musicMap.values()) {
            if (musicKnob.isPlaying()) {
                musicKnob.stop();
            }
        }
    }

    private static MusicKnob getMusic(String name) {
        if (musicMap.containsKey(name)) {
            MusicKnob musicKnob = musicMap.get(name);
            if (musicKnob.music == null) {
                String file = nameToFileMap.get(name);
                if (!GameFactory.getAssetManager().isLoaded(file)) {
                    throw new RuntimeException("Asset '" + name + "' with file '" + file + "' being used before it is fully loaded");
                }
                musicKnob.music = GameFactory.getAssetManager().get(file);
            }
            return musicKnob;
        } else {
            throw new RuntimeException("Attempting to play sound effect '" + name + "' but no such sound has been pre loaded yet");
        }
    }
}
