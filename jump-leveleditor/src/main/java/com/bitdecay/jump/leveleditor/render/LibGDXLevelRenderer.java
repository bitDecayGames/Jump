package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;

public class LibGDXLevelRenderer {
    private SpriteBatch batch;
    private ShapeRenderer renderer;

    public LibGDXLevelRenderer() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
    }

    public void render(LevelBuilder builder, OrthographicCamera cam) {
        BitRectangle view = new BitRectangle(cam.position.x, cam.position.y, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
        view.translate(-view.width / 2, -view.height / 2);
        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeType.Line);
        renderer.setColor(BitColors.INACTIVE_OBJECT);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.setColor(1, 1, 1, .3f);
        builder.otherObjects.forEach(object -> {
            if (object instanceof RenderableLevelObject) {
                batch.draw(((RenderableLevelObject) object).texture(), object.rect.xy.x, object.rect.xy.y, object.rect.width, object.rect.height);
            }
            renderer.rect(object.rect.xy.x, object.rect.xy.y, object.rect.width, object.rect.height);
            LevelEditor.addStringForRender(object.name(), new BitPoint(object.rect.xy), RenderLayer.LEVEL_OBJECTS);
        });
        batch.end();
        renderer.end();
    }
}
