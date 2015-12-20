package com.bitdecay.jump.gdx.integration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import java.io.File;
import java.io.FileFilter;

public class BitTexturePacker {

    FileFilter directoryFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };
    private File inputDir;
    private File outputDir;

    public BitTexturePacker(File inputDir, File outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }

    public void pack() {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth *= 4;
        settings.maxHeight *= 4;
        settings.combineSubdirectories = true;
        settings.duplicatePadding = true;
        settings.fast = true;
        settings.filterMin = Texture.TextureFilter.Nearest;
        settings.filterMag = Texture.TextureFilter.Nearest;

        System.out.println("Start packing textures");
        int count = 0;
        long startTime = System.currentTimeMillis();
        for (File subDir : inputDir.listFiles(directoryFilter)) {
            count++;
            System.out.println("\n****Packing atlas '" + subDir.getName() + "'");
            TexturePacker.process(settings, subDir.getAbsolutePath(), outputDir.getAbsolutePath(), subDir.getName());
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Finished packing textures");
        System.out.println("Atlases Packed: " + count);
        System.out.println(String.format("Total time: %.2f seconds", duration / 1000f));

        TexturePacker.process(settings, "ui", "skins", "ui");
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: <topLevelInputDirectory> <topLevelOutputDirectory>");
            System.exit(-1);
        }

        File inputDir = new File(args[0]);
        if (!inputDir.isDirectory()) {
            System.err.println(args[0] + " must be a directory");
        }

        File outputDir = new File(args[1]);
        if (!outputDir.isDirectory()) {
            System.err.println(args[1] + " must be a directory");
        }

        BitTexturePacker packer = new BitTexturePacker(inputDir, outputDir);
        packer.pack();
    }
}
