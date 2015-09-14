package com.bitdecay.jump.gdx.desktop;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.bitdecay.jump.gdx.engine.utilities.DevConstants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileUtilities {
    private static boolean generated = false;

    public static void generateImageAtlases() {
        if (!generated) {
            generated = true;
        } else {
            throw new RuntimeException("Cannot generate atlases more than once per run");
        }

        long timer = System.currentTimeMillis();
        Settings settings = new Settings();
        settings.filterMag = TextureFilter.Nearest;
        settings.filterMin = TextureFilter.Nearest;
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        File tempDir = new File("tempImages_" + System.currentTimeMillis());
        File imageDir = new File("../" + DevConstants.getAssetProjectName() + DevConstants.getRawImageDir());
        if (!imageDir.exists()) {
            System.err.println(imageDir.getAbsolutePath() + " does not exist. Could not build image atlases");
            System.exit(5);
        }
        for (File dir : imageDir.listFiles()) {
            if (dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    try {
                        FileUtils.copyFile(file, FileUtils.getFile(tempDir, dir.getName() + "." + file.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Generate packed images and .atlas file.
        TexturePacker.process(settings, tempDir.getAbsolutePath(), "../" + DevConstants.getAssetProjectName() + "/assets/images/textures", "images");

        try {
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Atlases took " + (System.currentTimeMillis() - timer) / 1000.0 + " seconds to generate");
    }
}
