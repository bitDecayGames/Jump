package com.bitdecay.jump.leveleditor.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class ImagePacker {
    public static void main(String[] arg)
    {
        System.out.println("Start packing textures");
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth *= 8;
        settings.maxHeight *= 8;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.Linear;
        //
        TexturePacker.process(settings, "main/ui", "main/skins", "ui");
        System.out.println("Finished packing textures");
    }
}
