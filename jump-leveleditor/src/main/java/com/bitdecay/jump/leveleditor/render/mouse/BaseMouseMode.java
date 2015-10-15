package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.tools.ToolAction;

public abstract class BaseMouseMode implements MouseMode, ToolAction {

    public LevelBuilder builder;
    public BitPointInt startPoint;
    public BitPointInt endPoint;

    public BaseMouseMode(LevelBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void mouseDown(BitPointInt point, MouseButton button) {
        startPoint = point;
    }

    @Override
    public void mouseDragged(BitPointInt point) {
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

    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
    }

    @Override
    public TextureRegion getIcon() {
        return new TextureRegion(new Texture(Gdx.files.internal("editorAssets/fill.png")));
    }
}
