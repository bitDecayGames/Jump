package com.bitdecay.engine.utilities;

import java.util.*;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.*;

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

	public static Map<String, Sound> soundFXMap;
	public static Map<String, Music> musicMap;

	public static Music currentMusic;

	public static void initialize() {
		soundFXMap = new HashMap<String, Sound>();
		musicMap = new HashMap<String, Music>();
	}

	/**
	 * Getter for sound clips (anything but long running sound objects like music)
	 * 
	 * @param type
	 *            the type category
	 * @param name
	 *            the name of the sound
	 * @return reference to shared sound object
	 */
	public static Sound getSoundClip(String name) {
		if (soundFXMap.containsKey(name)) {
			return soundFXMap.get(name);
		} else {
			Sound newSound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + name));
			soundFXMap.put(name, newSound);
			return newSound;
		}
	}

	/**
	 * Music is maintained in a cache which holds only currently playing music. Since music (according to documentation) is a heavyweight object, we need to
	 * clean up after ourselves to make sure we don't have too much in memory at any given time. This cache is used to allow level transitions to not cause a
	 * break/restart of the same song. To dispose of songs, call {@link }
	 * 
	 * @param name
	 *            name of the music track to load
	 * @return a new music object
	 */
	public static Music getMusic(String name) {
		if (musicMap.containsKey(name)) {
			return musicMap.get(name);
		} else {
			Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/" + name + ".mp3"));
			music.setLooping(true);
			musicMap.put(name, music);
			return music;
		}
	}

	public static void disposeMusic(Music music) {
		if (music.isPlaying()) {
			music.stop();
		}

		music.dispose();

		if (musicMap.containsValue(music)) {
			String keyToRemove = null;
			for (Entry<String, Music> entry : musicMap.entrySet()) {
				if (entry.getValue().equals(music)) {
					keyToRemove = entry.getKey();
					break;
				}
			}
			if (keyToRemove != null) {
				musicMap.remove(keyToRemove);
			}
		}
	}

	public static void maybeChangeMusic(String musicName) {
		Music oldMusic = currentMusic;
		currentMusic = getMusic(musicName);
		if (currentMusic != null) {
			if (oldMusic != null && !oldMusic.equals(currentMusic)) {
				SoundUtilities.disposeMusic(oldMusic);
			}
			if (!currentMusic.isPlaying()) {
				currentMusic.play();
			}
		} else if (oldMusic != null) {
			// if new music is null, and something was playing, stop it
			SoundUtilities.disposeMusic(oldMusic);
		}
	}
}
