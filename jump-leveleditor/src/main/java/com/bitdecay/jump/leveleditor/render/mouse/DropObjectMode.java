package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.ILevelBuilder;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.leveleditor.utils.EditorKeys;

/**
 * Created by Monday on 10/19/2015.
 */
public class DropObjectMode extends BaseMouseMode{
    private RenderableLevelObject modelInstance;
    private RenderableLevelObject instanceToDrop;

    private LevelEditor editor;

    public DropObjectMode(ILevelBuilder builder, LevelEditor editor) {
        super(builder);
        this.editor = editor;
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
    public void mouseDown(BitPointInt point, MouseButton button) {
        super.mouseDown(point, button);
        if (MouseButton.RIGHT.equals(button)) {
            // end our object dropping
            instanceToDrop = null;
        }
    }

    @Override
    public void mouseMoved(BitPointInt point) {
        super.mouseMoved(point);
        if (EditorKeys.DISABLE_SNAP.isPressed()) {
            currentLocation = new BitPointInt(point.x, point.y);
        } else {
            currentLocation = GeomUtils.snap(point.x - builder.getCellSize()/2, point.y - builder.getCellSize()/2, builder.getCellSize(), 0, 0);
        }
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        super.mouseDragged(point);
        if (EditorKeys.DISABLE_SNAP.isPressed()) {
            currentLocation = new BitPointInt(point.x, point.y);
        } else {
            currentLocation = GeomUtils.snap(point.x - builder.getCellSize() / 2, point.y - builder.getCellSize() / 2, builder.getCellSize(), 0, 0);
        }
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (instanceToDrop != null) {
            instanceToDrop.rect.xy.set(currentLocation.x, currentLocation.y);
            builder.createObject(instanceToDrop);
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
    }

    @Override
    public String getToolTip() {
        return "Drop Custom Object";
    }
}
