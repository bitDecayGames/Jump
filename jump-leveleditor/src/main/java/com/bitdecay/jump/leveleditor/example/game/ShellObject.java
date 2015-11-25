package com.bitdecay.jump.leveleditor.example.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.BodyType;
import com.bitdecay.jump.collision.ContactAdapter;
import com.bitdecay.jump.level.LevelObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Monday on 11/12/2015.
 */
public class ShellObject extends GameObject {
    ShellController controller = new ShellController(250);

    @Override
    public List<BitBody> build(LevelObject template) {
        BitBody body = template.buildBody();
        body.controller = controller;
        body.addContactListener(new ContactAdapter() {
            @Override
            public void contactStarted(BitBody other) {
                if (BodyType.DYNAMIC.equals(other.bodyType)) {
                    if (other.aabb.xy.y > body.aabb.xy.y + body.aabb.height/2) {
                        if (other.velocity.y > 0) {
                            other.velocity.y += 200;
                        } else {
                            other.velocity.y = 200;
                        }
                        controller.moving = !controller.moving;
                        controller.left = other.aabb.center().x >= body.aabb.center().x;
                    }
                }
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
