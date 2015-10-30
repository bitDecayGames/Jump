package com.bitdecay.jump.gdx.level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Simple class allowing a game to provide an id and a thumbnail for an object that doesn't necessarily have a body
 * Created by Monday on 10/22/2015.
 */
public class EditorIdentifierObject {
    public int id;
    public String displayName;
    public TextureRegion texture;

    public EditorIdentifierObject(int id, String name, TextureRegion texture) {
        this.id = id;
        this.displayName = name;
        this.texture = texture;
    }
}
