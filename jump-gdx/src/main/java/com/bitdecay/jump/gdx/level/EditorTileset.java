package com.bitdecay.jump.gdx.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Simple class allowing a game to provide a id for a tileset and a thumbnail as an example for the uesr
 * Created by Monday on 10/22/2015.
 */
public class EditorTileset {
    public int id;
    public String displayName;
    public TextureRegion texture;

    public EditorTileset(int id, String name, TextureRegion texture) {
        this.id = id;
        this.displayName = name;
        this.texture = texture;
    }
}
