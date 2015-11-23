package com.bitdecay.jump.leveleditor.example.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.ContactAdapter;
import com.bitdecay.jump.level.builder.LevelObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Monday on 11/22/2015.
 */
public class GravityField extends GameObject {
    @Override
    public List<BitBody> build(LevelObject template) {
        BitBody body = template.buildBody();
        body.addContactListener(new ContactAdapter() {
            @Override
            public void contactStarted(BitBody other) {
                other.props.gravityModifier = -1;
            }

            @Override
            public void contactEnded(BitBody other) {
                other.props.gravityModifier = 1;
            }
        });
        return Arrays.asList(body);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
