package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.control.PathedBodyController;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.TileBody;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;

import java.util.List;

public class LibGDXLevelRenderer {
    private ShapeRenderer renderer;

    public LibGDXLevelRenderer() {
        renderer = new ShapeRenderer();
    }

    public void render(LevelBuilder builder, OrthographicCamera cam) {
        BitRectangle view = new BitRectangle(cam.position.x, cam.position.y, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
        view.translate(-view.width / 2, -view.height / 2);
        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeType.Line);
        renderer.setColor(BitColors.INACTIVE_OBJECT);
        builder.otherObjects.forEach(object -> {
            renderer.rect(object.rect.xy.x, object.rect.xy.y, object.rect.width, object.rect.height);
            LevelEditor.addStringForRender(object.name(), new BitPoint(object.rect.xy), RenderLayer.LEVEL);
        });
        renderer.end();
    }
}
