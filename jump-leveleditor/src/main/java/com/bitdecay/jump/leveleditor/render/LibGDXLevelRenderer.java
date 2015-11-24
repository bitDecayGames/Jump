package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.LevelObject;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.tools.BitColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibGDXLevelRenderer {
    private SpriteBatch batch;
    private ShapeRenderer renderer;

    private List<BitPoint> arrow = Arrays.asList(new BitPoint(-10, -5), new BitPoint(0, 0), new BitPoint(-10, 5));

    public LibGDXLevelRenderer() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
    }

    public void render(LevelBuilder builder, OrthographicCamera cam) {
        BitRectangle view = new BitRectangle(cam.position.x, cam.position.y, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
        view.translate(-view.width / 2, -view.height / 2);
        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeType.Line);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.setColor(1, 1, 1, .3f);
        builder.otherObjects.values().forEach(object -> {
            if (object instanceof RenderableLevelObject) {
                batch.draw(((RenderableLevelObject) object).texture(), object.rect.xy.x, object.rect.xy.y, object.rect.width, object.rect.height);
            }
            if (RenderLayer.TRIGGERS.enabled) {
                renderer.setColor(BitColors.SELECTABLE);
                LevelObject other;
                for (String uuid : object.objectsTriggeredByThis) {
                    other = builder.otherObjects.get(uuid);

                    BitPoint middleOfLine = new BitPoint((object.rect.center().x + other.rect.center().x)/2,(object.rect.center().y + other.rect.center().y)/2);
                    float angle = GeomUtils.angle(object.rect.center(), other.rect.center());
                    renderer.polyline(GeomUtils.pointsToFloats(GeomUtils.translatePoints(GeomUtils.rotatePoints(arrow, angle), middleOfLine)));
                    renderer.line(object.rect.center().x, object.rect.center().y, other.rect.center().x, other.rect.center().y);
                }
            }
            renderer.setColor(BitColors.INACTIVE_OBJECT);
            renderer.rect(object.rect.xy.x, object.rect.xy.y, object.rect.width, object.rect.height);
            LevelEditor.addStringForRender(object.name(), new BitPoint(object.rect.xy), RenderLayer.LEVEL_OBJECTS);
        });
        batch.end();
        renderer.end();
    }
}
