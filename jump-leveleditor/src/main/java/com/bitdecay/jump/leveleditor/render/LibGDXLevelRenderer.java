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
            renderer.setColor(BitColors.INACTIVE_OBJECT);
            renderer.rect(object.rect.xy.x, object.rect.xy.y, object.rect.width, object.rect.height);
            LevelEditor.addStringForRender(object.name(), new BitPoint(object.rect.xy), RenderLayer.LEVEL_OBJECTS);
        });
        if (RenderLayer.TRIGGERS.enabled) {
            builder.triggers.values().forEach(trigger -> {
                renderer.setColor(BitColors.SELECTABLE);
                LevelObject actor = builder.otherObjects.get(trigger.triggerer.uuid);
                LevelObject victim = builder.otherObjects.get(trigger.triggeree.uuid);

                BitPoint middleOfLine = new BitPoint((actor.rect.center().x + victim.rect.center().x) / 2, (actor.rect.center().y + victim.rect.center().y) / 2);
                float angle = GeomUtils.angle(actor.rect.center(), victim.rect.center());
                renderer.polyline(GeomUtils.pointsToFloats(GeomUtils.translatePoints(GeomUtils.rotatePoints(arrow, angle), middleOfLine)));
                renderer.line(actor.rect.center().x, actor.rect.center().y, victim.rect.center().x, victim.rect.center().y);
            });
        }
        batch.end();
        renderer.end();
    }
}
