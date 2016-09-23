package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.ILevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.leveleditor.utils.EditorKeys;

/**
 * Created by Monday on 10/19/2015.
 */
public class DropSizedObjectMode extends BaseMouseMode{
    private RenderableLevelObject modelInstance;
    private RenderableLevelObject instanceToDrop;

    public DropSizedObjectMode(ILevelBuilder builder) {
        super(builder);
    }

    public void setObject(RenderableLevelObject modelInstance) {
        this.modelInstance = modelInstance;
        getNewInstanceToDrop();
    }

    private void getNewInstanceToDrop() {
        try {
            instanceToDrop = modelInstance.getNewCopy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseMoved(BitPointInt point) {
        startPoint = GeomUtils.snap(point, builder.getCellSize());
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        endPoint = GeomUtils.snap(point, builder.getCellSize());
    }

    @Override
    public void mouseDown(BitPointInt point, MouseButton button) {
        startPoint = GeomUtils.snap(point, builder.getCellSize());
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (instanceToDrop != null) {
            instanceToDrop.rect = new BitRectangle(startPoint, GeomUtils.snap(point, builder.getCellSize()));
            builder.createObject(instanceToDrop);
            startPoint = null;
            endPoint = null;
            if (EditorKeys.DROP_MULTI.isPressed()) {
                getNewInstanceToDrop();
            } else {
                instanceToDrop = null;
            }
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (currentLocation != null && instanceToDrop != null) {
            spriteBatch.draw(instanceToDrop.texture(), currentLocation.x, currentLocation.y, instanceToDrop.rect.width, instanceToDrop.rect.height);
            shaper.setColor(BitColors.GAME_OBJECT);
            shaper.rect(currentLocation.x, currentLocation.y, instanceToDrop.rect.width, instanceToDrop.rect.height);
        }

        if (startPoint != null && endPoint != null) {
            shaper.setColor(BitColors.NEW);
            shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        }
    }

    @Override
    public String getToolTip() {
        return "Drop Custom Sized Object";
    }
}
