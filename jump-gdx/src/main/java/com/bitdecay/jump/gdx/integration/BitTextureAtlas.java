package com.bitdecay.jump.gdx.integration;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 11/12/2015.
 */
public class BitTextureAtlas extends TextureAtlas {
    public BitTextureAtlas(TextureAtlasData data) {
        super(data);
    }

    public Array<AtlasRegion> findRegions(String name) {
        Array<AtlasRegion> regions = super.findRegions(name);
        if (regions.size > 0) {
            return regions;
        } else {
            int i = 1;
            while (true) {
                Array<AtlasRegion> numberedRegions = super.findRegions(name + "/" + i++);
                if (numberedRegions.size == 0) {
                    break;
                } else {
                    regions.addAll(numberedRegions);
                }
            }
            return regions;
        }
    }
}
