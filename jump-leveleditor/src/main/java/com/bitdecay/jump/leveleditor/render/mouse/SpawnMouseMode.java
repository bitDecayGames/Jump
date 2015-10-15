package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.LevelBuilder;

public class SpawnMouseMode extends BaseMouseMode {

    public SpawnMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        builder.setSpawn(point);
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (endPoint != null) {
            shaper.setColor(Color.YELLOW);
            shaper.circle(endPoint.x, endPoint.y, 7);
            shaper.setColor(Color.RED);
            shaper.circle(endPoint.x, endPoint.y, 4);
        }
    }

    @Override
    public String getToolTip() {
        return "Set the spawn point for the level";
    }

}
