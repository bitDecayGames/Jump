package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.leveleditor.utils.EditorKeys;

/**
 * Created by Monday on 10/19/2015.
 */
public class DropObjectMode extends BaseMouseMode{
    private Class<? extends RenderableLevelObject> objectClass;
    private RenderableLevelObject reference;

    private LevelEditor editor;

    public DropObjectMode(LevelBuilder builder, LevelEditor editor) {
        super(builder);
        this.editor = editor;
    }

    public void setObject(Class<? extends RenderableLevelObject> objectClass) {
        this.objectClass = objectClass;
        getNewReference();
    }

    private void getNewReference() {
        try {
            reference = objectClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseDown(BitPointInt point, MouseButton button) {
        super.mouseDown(point, button);
        if (MouseButton.RIGHT.equals(button)) {
            // end our object dropping
            reference = null;
        }
    }

    @Override
    public void mouseMoved(BitPointInt point) {
        super.mouseMoved(point);
        if (EditorKeys.DISABLE_SNAP.isPressed()) {
            currentLocation = new BitPointInt(point.x, point.y);
        } else {
            currentLocation = GeomUtils.snap(point.x - builder.tileSize/2, point.y - builder.tileSize/2, builder.tileSize, 0, 0);
        }
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        super.mouseDragged(point);
        if (EditorKeys.DISABLE_SNAP.isPressed()) {
            currentLocation = new BitPointInt(point.x, point.y);
        } else {
            currentLocation = GeomUtils.snap(point.x - builder.tileSize / 2, point.y - builder.tileSize / 2, builder.tileSize, 0, 0);
        }
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (reference != null) {
            reference.rect.xy.set(currentLocation.x, currentLocation.y);
            builder.createObject(reference);
            if (EditorKeys.DROP_MULTI.isPressed()) {
                getNewReference();
            } else {
                reference = null;
            }
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (currentLocation != null && reference != null) {
            spriteBatch.draw(reference.texture(), currentLocation.x, currentLocation.y, reference.rect.width, reference.rect.height);
            shaper.setColor(BitColors.GAME_OBJECT);
            shaper.rect(currentLocation.x, currentLocation.y, reference.rect.width, reference.rect.height);
        }
    }

    @Override
    public String getToolTip() {
        return "Drop Custom Object";
    }
}
