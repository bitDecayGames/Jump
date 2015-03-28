package com.bitdecay.engine.utilities;

import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.bitdecay.engine.GameFactory;

/**
 * Will look for a <b>texture.properties</b> file in <b>assets/props</b>. The
 * props file should be formatted as ${atlasName}=${directory} where atlasName
 * is the atlas grouping reference to be used in code. <br>
 * <br>
 * Ex.<br>
 * sprites=/images/mySprites<br>
 * ImageUtilities.getTextureRegion("sprites", "logo")<br>
 * <br>
 * <b>NOTE:</b> Automatically packs all images into atlases when run from the
 * desktop. This is known to potentially take a while. This aspect should be
 * removed from release version of the software
 * 
 * @author Monday
 * @version 0.1 (March 2014)
 */
public class ImageUtilities {
	private static String atlasLocation = "images/textures/images.atlas";

	public static void initialize(String location) {
		atlasLocation = location;
		loadTextures();
	}

	private static void loadTextures() {
		GameFactory.getAssetManager().load(atlasLocation, TextureAtlas.class);
	}

	/**
	 * @param folder
	 *            the folder the image lives in in the repository
	 * @param image
	 *            the name of the image to get
	 * @return The TextureRegion that corresponds to the image, pulled from the
	 *         image atlas
	 */
	public static TextureRegion getTextureRegion(String folder, String image) {
		AtlasRegion region = getAtlas().findRegion(folder + "." + image);
		if (region == null) {
			throw new RuntimeException("Attempted to load image '" + folder + " - " + image + "' but it does not exist");
		}
		return region;
	}

	/**
	 * 
	 * @param folder
	 *            the folder the image lives in in the repository
	 * @param image
	 *            the name of the image to get
	 * @param row
	 *            the row of the image to get (useful for getting a specific
	 *            animation)
	 * @return a TextureRegion[] of {@link Cubicle#SPRITE_SIZE} tiles from the
	 *         corresponding row of the image
	 */
	public static TextureRegion[] getImageRow(String folder, String image, int row) {
		return getImageRow(folder, image, row, -1);
	}

	/**
	 * 
	 * @param folder
	 *            the folder the image lives in in the repository
	 * @param image
	 *            the name of the image to get
	 * @param row
	 *            the row of the image to get (useful for getting a specific
	 *            animation)
	 * @param frames
	 *            the number of frames from that row to fetch
	 * @return a TextureRegion[] with length <b>frames</b> of
	 *         {@link Cubicle#SPRITE_SIZE} tiles from the corresponding row of
	 *         the image
	 */
	public static TextureRegion[] getImageRow(String folder, String image, int row, int frames) {
		if (frames <= 1) {
			return getTextureRegion(folder, image).split(32, 32)[row];
		} else {
			return Arrays.copyOf(getTextureRegion(folder, image).split(32, 32)[row], frames);
		}
	}

	public static TextureAtlas getAtlas() {
		return GameFactory.getAssetManager().get(atlasLocation, TextureAtlas.class);
	}
}
