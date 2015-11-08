package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;

public class SpawnMouseMode extends BaseMouseMode {

    public SpawnMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
         builder.setDebugSpawn(point);
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (endPoint != null) {
            shaper.setColor(BitColors.SPAWN_OUTER);
            shaper.circle(endPoint.x, endPoint.y, 7);
            shaper.setColor(BitColors.SPAWN);
            shaper.circle(endPoint.x, endPoint.y, 4);
        }
    }

    @Override
    public String getToolTip() {
        return "Set the debugSpawn point for the level";
    }

}
