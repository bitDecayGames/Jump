package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.builder.ILevelBuilder;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.tools.ToolAction;

public abstract class BaseMouseMode implements MouseMode, ToolAction {

    public ILevelBuilder builder;
    public BitPointInt startPoint;
    public BitPointInt endPoint;
    public BitPointInt currentLocation;

    public BaseMouseMode(ILevelBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void mouseDown(BitPointInt point, MouseButton button) {
        startPoint = point;
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        currentLocation = point;
        endPoint = point;
    }

    @Override
    public void mouseUp(BitPointInt point, MouseButton button) {
        if (startPoint == null) {
            return;
        }
        endPoint = point;
        mouseUpLogic(point, button);
        startPoint = null;
        endPoint = null;
    }

    protected abstract void mouseUpLogic(BitPointInt point, MouseButton button);

    @Override
    public void mouseMoved(BitPointInt point) {
        currentLocation = point;
    }

    @Override
    public void scrolled(int amount) {
        // nothing by default
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
    }

    @Override
    public TextureRegion getIcon() {
        return new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.ASSETS_FOLDER + "editorAssets/fill.png")));
    }
}
