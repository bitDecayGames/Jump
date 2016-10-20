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
    public void mouseDown(BitPointInt point, MouseButton button) {
        if (MouseButton.RIGHT.equals(button)) {
            startPoint = null;
            endPoint = null;
        } else {
            super.mouseDown(point, button);
            endPoint = snapToSlopeSizes(point);
        }
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        if (startPoint != null) {
            super.mouseDragged(point);
            endPoint = snapToSlopeSizes(point);
        }
    }

    private BitPointInt snapToSlopeSizes(BitPointInt point) {
        /*
         * permitted multiples:
         * rise:run
         * 1:1
         * 1:2
         * 1:3
         * 1:4
         */
        BitPointInt clip = GeomUtils.snap(point, builder.getCellSize());

        int rise = clip.y - startPoint.y;
        int run = clip.x - startPoint.x;

        if (rise < 0) {
            clip.y = startPoint.y - builder.getCellSize();
        } else {
            clip.y = startPoint.y + builder.getCellSize();
        }

        int maxRunDistance = 4;
        if (run < 0) {
            clip.x = Math.max(startPoint.x - builder.getCellSize() * maxRunDistance, clip.x);
        } else if (run > 0) {
            clip.x = Math.min(startPoint.x + builder.getCellSize() * maxRunDistance, clip.x);
        } else {
            clip.x = startPoint.x + builder.getCellSize();
        }

        return clip;
    }

    @Override
    public void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (MouseButton.LEFT.equals(button) && startPoint != null && endPoint != null) {
            endPoint = snapToSlopeSizes(point);
            boolean isFloor = startPoint.y < endPoint.y;
            builder.createSlopedLevelObject(startPoint, endPoint, isFloor, false, material);
        }
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
        if (startPoint != null && endPoint != null) {
            shaper.setColor(BitColors.GAME_OBJECT);
            shaper.circle(startPoint.x, startPoint.y, 1);
            shaper.circle(endPoint.x, endPoint.y, 1);
            shaper.polygon(new float[] {startPoint.x, startPoint.y, endPoint.x, endPoint.y, endPoint.x, startPoint.y });
        }
    }
}
