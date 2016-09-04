package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.leveleditor.utils.EditorKeys;

/**
 * Created by Monday on 10/19/2015.
 */
public class DropSizedObjectMode extends BaseMouseMode{
    private Class<? extends RenderableLevelObject> objectClass;
    private RenderableLevelObject reference;

    private LevelEditor editor;

    public DropSizedObjectMode(LevelBuilder builder, LevelEditor editor) {
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
    public void mouseMoved(BitPointInt point) {
        startPoint = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        endPoint = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    public void mouseDown(BitPointInt point, MouseButton button) {
        startPoint = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (reference != null) {
            reference.rect = new BitRectangle(startPoint, GeomUtils.snap(point, builder.tileSize));
            builder.createObject(reference);
            startPoint = null;
            endPoint = null;
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
