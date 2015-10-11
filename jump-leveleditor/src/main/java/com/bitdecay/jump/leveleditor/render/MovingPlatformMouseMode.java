package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.LevelBuilder;
import com.bitdecay.jump.leveleditor.render.mouse.BaseMouseMode;
import com.bitdecay.jump.leveleditor.tools.BitColors;

import java.util.ArrayList;
import java.util.List;

public class MovingPlatformMouseMode extends BaseMouseMode {
    private LevelBuilder builder;
    public static int direction = Direction.UP;

    private BitPointInt currentPoint;
    private BitRectangle platform;
    private List<BitPoint> pathPoints;

    public MovingPlatformMouseMode(LevelBuilder builder) {
        super(builder);
        this.builder = builder;
    }

    @Override
    public void mouseMoved(BitPointInt point) {
        if (platform != null) {
            currentPoint = GeomUtils.snap(point.x, point.y, builder.tileSize, (int) (platform.xy.x + platform.width / 2), (int) (platform.xy.y + platform.height / 2));
        }
    }

    @Override
    public void mouseDown(BitPointInt point) {
        startPoint = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        super.mouseDragged(point);
        endPoint = GeomUtils.snap(point, builder.tileSize);
    }

    @Override
    public void mouseUpLogic(BitPointInt point) {
        if (platform == null) {
            endPoint = GeomUtils.snap(point, builder.tileSize);
            if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
                platform = new BitRectangle(startPoint, endPoint);
                pathPoints = new ArrayList<>();
                pathPoints.add(platform.xy.plus(platform.width/2, platform.height/2));
                currentPoint = endPoint;
            }
        } else {
            endPoint = GeomUtils.snap(point.x, point.y, builder.tileSize, (int)(platform.xy.x + platform.width/2), (int)(platform.xy.y + platform.height/2));
            BitPoint latestPoint = new BitPoint(endPoint.x, endPoint.y);
            for (BitPoint pathPoint : pathPoints) {
                if (latestPoint.equals(pathPoint)) {
                    boolean pendulum = true;
                    if (latestPoint.equals(pathPoints.get(0))) {
                        pendulum = false;
                    }
                    // if we clicked a previous point, end our pathing
                    builder.createKineticObject(platform, pathPoints, 20, pendulum);
                    platform = null;
                    return;
                }
            }
            // if we didn't click a previous point, just add it and continue
            pathPoints.add(latestPoint);
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (platform == null) {
            if (startPoint != null && endPoint != null) {
                shaper.setColor(Color.RED);
                shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
            }
        } else {
            shaper.setColor(Color.BLUE);
            shaper.rect(platform.xy.x, platform.xy.y, platform.width, platform.height);
            shaper.setColor(Color.FIREBRICK);
            BitPoint lastPoint = null;
            for (BitPoint point : pathPoints) {
                if (lastPoint == null) {
                    lastPoint = point;
                } else {
                    shaper.setColor(BitColors.DARK_NAVY);
                    shaper.rect(point.x - platform.width / 2, point.y - platform.height / 2, platform.width, platform.height);
                    shaper.setColor(Color.FIREBRICK);
                    shaper.line(lastPoint.x, lastPoint.y, point.x, point.y);
                    shaper.circle(point.x, point.y, 4);
                    lastPoint = point;
                }
            }
            if (currentPoint != null) {
                shaper.setColor(Color.GREEN);
                shaper.rect(currentPoint.x - platform.width / 2, currentPoint.y - platform.height / 2, platform.width, platform.height);
                shaper.setColor(Color.FIREBRICK);
                shaper.line(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
            }
        }
    }

    @Override
    public String getToolTip() {
        return "Create a kinetic object that follows a path";
    }
}
