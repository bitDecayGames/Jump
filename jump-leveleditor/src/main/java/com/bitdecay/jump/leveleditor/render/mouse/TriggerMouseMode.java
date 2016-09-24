package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.DebugSpawnObject;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.jump.level.TriggerObject;
import com.bitdecay.jump.level.builder.ILevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;

/**
 * Created by Monday on 11/22/2015.
 */
public class TriggerMouseMode extends BaseMouseMode {
    private LevelObject triggerer;
    private LevelObject triggeree;

    public TriggerMouseMode(ILevelBuilder builder) {
        super(builder);
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (MouseButton.LEFT.equals(button)) {
            builder.selectObject(point, false, false);
            if (builder.getSelection().size() > 0) {
                if (builder.getSelection().get(0) instanceof DebugSpawnObject ||
                        builder.getSelection().get(0) instanceof TriggerObject) {
                    // these are not valid trigger objects
                    return;
                }
                if (triggerer == null) {
                    triggerer = builder.getSelection().get(0);
                } else {
                    // create our trigger
                    triggeree = builder.getSelection().get(0);
                    builder.createObject(new TriggerObject(triggerer, triggeree));
                    triggerer = null;
                    triggeree = null;
                }
            }
            builder.getSelection().clear();
        } else if (MouseButton.RIGHT.equals(button)) {
            triggerer = null;
            triggeree = null;
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (triggerer != null) {
            shaper.setColor(BitColors.NEW);
            shaper.rect(triggerer.rect.xy.x, triggerer.rect.xy.y, triggerer.rect.width, triggerer.rect.height);
            shaper.line(triggerer.rect.xy.x + triggerer.rect.width/2, triggerer.rect.xy.y + triggerer.rect.height/2, currentLocation.x, currentLocation.y);
        }
    }

    @Override
    public String getToolTip() {
        return "Trigger";
    }
}
