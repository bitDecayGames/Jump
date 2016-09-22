package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.builder.ILevelBuilder;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.ui.PropModUI;
import com.bitdecay.jump.leveleditor.ui.PropModUICallback;

/**
 * Created by Monday on 10/27/2015.
 */
public class PropertyInspectMode extends BaseMouseMode implements PropModUICallback {
    private PropModUI ui;
    private Object selectedObject;

    private LevelEditor editor;

    public PropertyInspectMode(ILevelBuilder builder, LevelEditor editor) {
        super(builder);
        this.editor = editor;
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        builder.selectObject(startPoint, false);
        if (builder.getSelection().size() > 0) {
            if (ui != null) {
                ui.close();
            }
            selectedObject = builder.getSelection().get(0);
            ui = new PropModUI(this, builder.getSelection().get(0));
            ui.setVisible(true);
        }
    }

    @Override
    public String getToolTip() {
        return "Inspect Object";
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        super.render(shaper, spriteBatch);
    }

    @Override
    public void propertyChanged(String prop, Object value) {
        editor.queueReload();
    }
}
