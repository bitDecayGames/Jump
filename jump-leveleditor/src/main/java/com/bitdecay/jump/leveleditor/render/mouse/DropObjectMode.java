package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.leveleditor.tools.BitColors;

/**
 * Created by Monday on 10/19/2015.
 */
public class DropObjectMode extends BaseMouseMode{
    private Class objectClass;
    private LevelObject reference;

    public DropObjectMode(LevelBuilder builder) {
        super(builder);
    }

    public void setObject(Class<? extends LevelObject> objectClass) {
        this.objectClass = objectClass;
        try {
            reference = objectClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        super.mouseDragged(point);
        currentLocation = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        builder.createObject(objectClass, currentLocation);
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (currentLocation != null && reference != null) {
            shaper.setColor(BitColors.GAME_OBJECT);
            shaper.rect(currentLocation.x, currentLocation.y, reference.rect.width, reference.rect.height);
        }
    }

    @Override
    public String getToolTip() {
        return "Delete";
    }
}
