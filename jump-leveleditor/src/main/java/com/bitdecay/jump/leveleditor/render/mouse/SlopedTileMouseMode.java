package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.ILevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;

/**
 * Created by Monday on 10/18/2016.
 */
public class SlopedTileMouseMode extends CreateMouseMode {

    int renderHeight = 0;

    public SlopedTileMouseMode(ILevelBuilder builder) {
        super(builder);
    }

    @Override
    public String getToolTip() {
        return "Single Tile Mouse Mode";
    }

    @Override
    public void mouseMoved(BitPointInt point) {
        super.mouseMoved(point);
        currentLocation = GeomUtils.snap(point.x - builder.getCellSize()/2, point.y - builder.getCellSize()/2, builder.getCellSize(), 0, 0);
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        super.mouseDragged(point);
        currentLocation = GeomUtils.snap(point.x - builder.getCellSize()/2, point.y - builder.getCellSize()/2, builder.getCellSize(), 0, 0);
    }

    @Override
    public void mouseUpLogic(BitPointInt point, MouseButton button) {
        endPoint = GeomUtils.snap(currentLocation.plus(builder.getCellSize(), builder.getCellSize()), builder.getCellSize());
        builder.createLevelObject(currentLocation, endPoint, false, material);
    }

    @Override
    public void scrolled(int amount) {
        amount = amount > 0 ? -1 : 1;
        renderHeight += amount;
        if (renderHeight < 0) {
            renderHeight += builder.getCellSize();
        }
        renderHeight %= builder.getCellSize();
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (currentLocation != null) {
            shaper.setColor(BitColors.GAME_OBJECT);
            shaper.rect(currentLocation.x, currentLocation.y, builder.getCellSize(), builder.getCellSize());
            shaper.line(currentLocation.x, currentLocation.y + Math.abs(renderHeight), currentLocation.x + builder.getCellSize(), currentLocation.y + Math.abs(renderHeight));
        }
    }
}
