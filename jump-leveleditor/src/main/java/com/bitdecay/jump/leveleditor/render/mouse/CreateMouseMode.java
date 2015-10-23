package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;

public class CreateMouseMode extends BaseMouseMode {
    protected int material = 0;

    public CreateMouseMode(LevelBuilder builder) {
        super(builder);
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    @Override
    public void mouseDown(BitPointInt point, MouseButton button) {
        startPoint = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        super.mouseDragged(point);
        endPoint = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    public void mouseUpLogic(BitPointInt point, MouseButton button) {
        endPoint = GeomUtils.snap(point, builder.tileSize);
        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
            builder.createLevelObject(startPoint, endPoint, false, material);
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (startPoint != null && endPoint != null) {
            shaper.setColor(BitColors.NEW);
            shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        }
    }

    @Override
    public String getToolTip() {
        return "Create";
    }
}
