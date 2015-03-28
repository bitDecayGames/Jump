package com.bitdecay.engine;

import com.badlogic.gdx.assets.AssetManager;

/**
 * A singleton instance holder. Holds things such as the {@link AssetManager} so
 * it can be accessed from custom built tools. This class should never be needed
 * outside of engine code.
 * 
 * @author Monday
 * @version 0.1 (March 2014)
 */
public class GameFactory {
	private static AssetManager assetManager;

	public static AssetManager getAssetManager() {
		if (assetManager == null) {
			assetManager = new AssetManager();
		}
		return assetManager;
	}
}
