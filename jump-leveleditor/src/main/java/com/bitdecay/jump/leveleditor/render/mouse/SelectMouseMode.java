package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.builder.ILevelBuilder;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

/**
 * an empty mouse mode
 *
 * @author Monday
 */
public class SelectMouseMode extends BaseMouseMode {
    private static final Color LINE_COLOR = new Color(0, 0, 255, 1f);
    private Texture fillTexture;

    public SelectMouseMode(ILevelBuilder builder) {
        super(builder);
        fillTexture = new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/fill.png"));
    }

    @Override
    public void mouseUpLogic(BitPointInt point, MouseButton button) {
        boolean shift = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Keys.SHIFT_RIGHT);
        if (startPoint.equals(endPoint)) {
            builder.selectObject(startPoint, shift);
        } else {
            BitRectangle rect = new BitRectangle(startPoint, endPoint);
            builder.selectObjects(rect, shift);
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (startPoint != null && endPoint != null) {
            spriteBatch.draw(fillTexture, startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);

            shaper.setColor(LINE_COLOR);
            shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        }
    }

    @Override
    public String getToolTip() {
        return "Select";
    }

}
