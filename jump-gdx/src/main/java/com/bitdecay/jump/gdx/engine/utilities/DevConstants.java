package com.bitdecay.jump.gdx.engine.utilities;

/**
 * A class that is used in various places to get project specific settings and
 * constants.
 *
 * @author Monday
 * @version 0.1 (March 2014)
 * @see {@link Constants}
 */
public class DevConstants {
    private static Constants instance = new Constants("android", "/assets/images/raw");

    public static boolean skipSplashScreen = false;
    public static boolean forceNewGame = true;
    public static boolean debugRender = true;

    public static void setDevConstants(Constants constants) {
        instance = constants;
    }

    public static String getAssetProjectName() {
        return instance.assetProjectName;
    }

    public static String getRawImageDir() {
        return instance.rawImageDir;
    }

    /**
     * Contains getters for various game constants used by the engine such as
     * file paths. Subclass {@link Constants} and call
     * {@link DevConstants#setDevConstants}
     *
     * @author Monday
     * @version 0.1 (March 2014)
     */
    public static class Constants {
        public String assetProjectName;
        private String rawImageDir;

        public Constants(String assetProjectName, String rawImageDir) {
            this.assetProjectName = assetProjectName;
            this.rawImageDir = rawImageDir;
        }
    }
}
