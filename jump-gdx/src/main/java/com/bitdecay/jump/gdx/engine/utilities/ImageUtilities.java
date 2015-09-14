package com.bitdecay.jump.gdx.engine.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitdecay.jump.gdx.engine.GameFactory;

import java.util.ArrayList;
import java.util.List;

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

    private static boolean initialized = false;

    private static final String atlasLocation = "images/textures/images.atlas";
    // TODO: there may be more than one of these files. Do it by prefix and just check 1, 2, 3, etc.
    private static final String atlasFile = "images/textures/images.png";
    private static Pixmap atlasPixmap;

    /**
     * Initializes all image data. Blocks until loaded
     */
    public static void initialize() {
        if (!initialized) {
            loadTextures();
            while (!GameFactory.getAssetManager().update()) {
                // spin. Still loading
            }
            initialized = true;
        } else {
            return;
        }
    }

    private static void loadTextures() {
        GameFactory.getAssetManager().load(atlasLocation, TextureAtlas.class);
        atlasPixmap = new Pixmap(Gdx.files.internal(atlasFile));
    }

    /**
     * @param folder the folder the image lives in in the repository
     * @param image  the name of the image to get
     * @return The TextureRegion that corresponds to the image, pulled from the
     * image atlas
     */
    public static TextureRegion getTextureRegion(String folder, String image) {
        AtlasRegion region = getAtlas().findRegion(folder + "." + image);
        if (region == null) {
            throw new RuntimeException("Attempted to load image '" + folder + " - " + image + "' but it does not exist");
        }
        return region;
    }

    public static Pixmap getPixmap(String folder, String image) {
        AtlasRegion region = getAtlas().findRegion(folder + "." + image);
        Pixmap regionPixmap = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), atlasPixmap.getFormat());
        regionPixmap.drawPixmap(atlasPixmap, 0, 0, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
        return regionPixmap;
    }

    /**
     * @param folder the folder the image lives in in the repository
     * @param image  the name of the image to get
     * @param row    the row of the image to get (useful for getting a specific
     *               animation)
     * @return a TextureRegion[] of {@link Cubicle#SPRITE_SIZE} tiles from the
     * corresponding row of the image
     */
    public static TextureRegion[] getImageRow(String folder, String image, int row) {
        return getImageRow(folder, image, row, -1);
    }

    /**
     * @param folder the folder the image lives in in the repository
     * @param image  the name of the image to get
     * @param row    the row of the image to get (useful for getting a specific
     *               animation)
     * @param frames the number of frames from that row to fetch
     * @return a TextureRegion[] with length <b>frames</b> of
     * {@link Cubicle#SPRITE_SIZE} tiles from the corresponding row of
     * the image
     */
    public static TextureRegion[] getImageRow(String folder, String image, int row, int frames) {
        // TODO: need to make this smarter -- or change how we get certain animations (maybe just make them different files? that'd be easy enough.
        if (frames <= 1) {
            // return getTextureRegion(folder, image).split(Cubicle.SPRITE_SIZE, Cubicle.SPRITE_SIZE)[row];
        } else {
            // return Arrays.copyOf(getTextureRegion(folder, image).split(Cubicle.SPRITE_SIZE, Cubicle.SPRITE_SIZE)[row], frames);
        }
        return null;
    }

    private static TextureAtlas getAtlas() {
        return GameFactory.getAssetManager().get(atlasLocation, TextureAtlas.class);
    }

    public static TextureRegion[] getImagesByPrefix(String folder, String prefix) {
        List<TextureRegion> frames = new ArrayList<TextureRegion>();
        int i = 1;
        try {
            while (true) {
                frames.add(getTextureRegion(folder, prefix + i++));
            }
        } catch (Exception e) {
            //ignore
        }
        if (frames.size() == 0) {
            // check for just one image with no numeric suffix
            frames.add(getTextureRegion(folder, prefix));
        }
        if (frames.size() == 0) {
            throw new RuntimeException("No images found with prefix " + prefix + " in folder " + folder);
        }
        return frames.toArray(new TextureRegion[frames.size()]);
    }
}
