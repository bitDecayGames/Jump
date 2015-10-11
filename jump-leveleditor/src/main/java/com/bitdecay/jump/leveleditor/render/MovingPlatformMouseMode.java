package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.*;
import com.bitdecay.jump.level.LevelBuilder;
import com.bitdecay.jump.leveleditor.render.mouse.BaseMouseMode;
import com.bitdecay.jump.leveleditor.tools.BitColors;

import java.util.ArrayList;
import java.util.List;

public class MovingPlatformMouseMode extends BaseMouseMode {
    private LevelBuilder builder;

    private BitPointInt currentPoint;
    private BitPointInt pausePoint;

    private BitRectangle platform;
    private List<PathPoint> pathPoints;
    private int pauseSnapSize;
    private float platformPause;

    public MovingPlatformMouseMode(LevelBuilder builder) {
        super(builder);
        this.builder = builder;
        pauseSnapSize = builder.tileSize / 4;
    }

    @Override
    public void mouseMoved(BitPointInt point) {
        if (platform != null) {
            int xOffset = platform.width % 2 == 0 ? 0: 4;
            int yOffset = platform.height % 2 == 0 ? 0 : -4;
            currentPoint = GeomUtils.snap(point.x + xOffset, point.y + yOffset, builder.tileSize, (int) (platform.xy.x + platform.width / 2), (int) (platform.xy.y + platform.height / 2));
            pausePoint = null;
        }
    }

    @Override
    public void mouseDown(BitPointInt point) {
        if (platform == null) {
            startPoint = GeomUtils.snap(point, builder.tileSize);
        } else {
            startPoint = currentPoint;
        }
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        if (platform == null) {
            endPoint = GeomUtils.snap(point, builder.tileSize);
        } else {
            pausePoint = GeomUtils.snap(point, pauseSnapSize);
            BitPoint lineSegment = new BitPoint(pausePoint.x - startPoint.x, pausePoint.y - startPoint.y);
            platformPause = (int)(lineSegment.len()) / pauseSnapSize * .1f;
        }
    }

    @Override
    public void mouseUpLogic(BitPointInt point) {
        if (platform == null) {
            endPoint = GeomUtils.snap(point, builder.tileSize);
            if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
                platform = new BitRectangle(startPoint, endPoint);
                pathPoints = new ArrayList<>();
                pathPoints.add(new PathPoint(platform.xy.plus(platform.width/2, platform.height/2), 0));
                currentPoint = endPoint;
            }
        } else {
            endPoint = currentPoint;
            BitPoint latestPoint = new BitPoint(endPoint.x, endPoint.y);
            for (PathPoint pathPoint : pathPoints) {
                if (latestPoint.equals(pathPoint.destination)) {
                    // if we clicked a previous point, potentially end our path
                    boolean pendulum = true;
                    if (latestPoint.equals(pathPoints.get(0).destination)) {
                        pathPoints.get(0).stayTime = platformPause;
                        pendulum = false;
                    }
                    if (pathPoints.size() > 1) {
                        // only end the path if we have more than one node -- otherwise let the user adjust the initial
                        // wait time for the beginning node
                        builder.createKineticObject(platform, pathPoints, 20, pendulum);
                        platform = null;
                        return;
                    }
                }
            }
            // if we didn't click a previous point, just add it and continue
            pathPoints.add(new PathPoint(latestPoint, platformPause));
            platformPause = 0;
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
            if (startPoint != null && pausePoint != null) {
                shaper.setColor(Color.WHITE);
                shaper.line(startPoint.x, startPoint.y, pausePoint.x, pausePoint.y);
                LevelEditor.addStringForRender(String.format("%.1f seconds", platformPause), new BitPoint(pausePoint.x, pausePoint.y + 10));
            }
            shaper.setColor(Color.BLUE);
            shaper.rect(platform.xy.x, platform.xy.y, platform.width, platform.height);
            shaper.setColor(Color.FIREBRICK);
            PathPoint lastPoint = null;
            for (PathPoint point : pathPoints) {
                if (lastPoint == null) {
                    lastPoint = point;
                } else {
                    shaper.setColor(BitColors.DARK_NAVY);
                    shaper.rect(point.destination.x - platform.width / 2, point.destination.y - platform.height / 2, platform.width, platform.height);
                    shaper.setColor(Color.FIREBRICK);
                    shaper.line(lastPoint.destination.x, lastPoint.destination.y, point.destination.x, point.destination.y);
                    shaper.circle(point.destination.x, point.destination.y, 4);
                    lastPoint = point;
                }
            }
            if (currentPoint != null) {
                shaper.setColor(Color.GREEN);
                shaper.rect(currentPoint.x - platform.width / 2, currentPoint.y - platform.height / 2, platform.width, platform.height);
                shaper.setColor(Color.FIREBRICK);
                shaper.line(lastPoint.destination.x, lastPoint.destination.y, currentPoint.x, currentPoint.y);
            }
        }
    }

    @Override
    public String getToolTip() {
        return "Create a kinetic object that follows a path";
    }
}
