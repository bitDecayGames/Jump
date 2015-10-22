package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.leveleditor.ui.OptionsMode;

/**
 * Created by Monday on 10/19/2015.
 */
public class DropObjectMode extends BaseMouseMode{
    private Class objectClass;
    private RenderableLevelObject reference;

    private LevelEditor editor;

    public DropObjectMode(LevelBuilder builder, LevelEditor editor) {
        super(builder);
        this.editor = editor;
    }

    public void setObject(Class<? extends RenderableLevelObject> objectClass) {
        this.objectClass = objectClass;
        try {
            reference = objectClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseMoved(BitPointInt point) {
        super.mouseMoved(point);
        currentLocation = GeomUtils.snap(point.x - builder.tileSize/2, point.y - builder.tileSize/2, builder.tileSize, 0, 0);
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        super.mouseDragged(point);
        currentLocation = GeomUtils.snap(point.x - builder.tileSize/2, point.y - builder.tileSize/2, builder.tileSize, 0, 0);
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        builder.createObject(objectClass, currentLocation);
        editor.setMode(OptionsMode.SELECT);
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
        return "Delete";
    }
}
