package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.geom.*;
import com.bitdecay.jump.level.builder.LevelBuilder;
import com.bitdecay.jump.leveleditor.render.mouse.BaseMouseMode;
import com.bitdecay.jump.leveleditor.render.mouse.MouseButton;
import com.bitdecay.jump.leveleditor.tools.BitColors;

import java.util.ArrayList;
import java.util.List;

public class MovingPlatformMouseMode extends BaseMouseMode {
    private LevelBuilder builder;

    private BitPointInt currentPoint;

    private BitPointInt pausePoint;
    private int pauseSnapSize;
    private float platformPause;

    private BitPointInt speedPoint;
    private float platformSpeed = 30;

    private BitRectangle platform;
    private List<PathPoint> pathPoints;

    private MouseButton mouseButtonDown = null;

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
    public void mouseDown(BitPointInt point, MouseButton button) {
        if (mouseButtonDown == null) {
            mouseButtonDown = button;
        } else {
            return;
        }

        if (MouseButton.LEFT.equals(mouseButtonDown)) {
            if (platform == null) {
                startPoint = GeomUtils.snap(point, builder.tileSize);
            } else {
                startPoint = currentPoint;
            }
        } else if (MouseButton.RIGHT.equals(mouseButtonDown)) {
            startPoint = point;
        }
    }

    @Override
    public void mouseDragged(BitPointInt point) {
        if (MouseButton.RIGHT.equals(mouseButtonDown)) {
            speedPoint = point;
            BitPoint lineSegment = new BitPoint(speedPoint.x - startPoint.x, speedPoint.y - startPoint.y);
            platformSpeed = (int) (lineSegment.len());
        } else {
            if (platform == null) {
                endPoint = GeomUtils.snap(point, builder.tileSize);
            } else {
                pausePoint = GeomUtils.snap(point, pauseSnapSize);
                BitPoint lineSegment = new BitPoint(pausePoint.x - startPoint.x, pausePoint.y - startPoint.y);
                platformPause = (int) (lineSegment.len()) / pauseSnapSize * .1f;
            }
        }
    }

    @Override
    public void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (MouseButton.RIGHT.equals(mouseButtonDown)) {
            mouseButtonDown = null;
            // speed is set by the mouse drag. just return
            return;
        } else {
            mouseButtonDown = null;
            if (platform == null) {
                endPoint = GeomUtils.snap(point, builder.tileSize);
                if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
                    platform = new BitRectangle(startPoint, endPoint);
                    pathPoints = new ArrayList<>();
                    pathPoints.add(new PathPoint(platform.xy.plus(platform.width / 2, platform.height / 2), platformSpeed, 0));
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
                            pendulum = false;
                            if (pathPoints.size() == 1) {
                                //this was our only node. update it instead of placing 2 on top of each other
                                pathPoints.get(0).stayTime = platformPause;
                                pathPoints.get(0).speed = platformSpeed;
                                platformPause = 0;
                                return;
                            }
                        }
                        if (pathPoints.size() > 1) {
                            // only end the path if we have more than one node -- otherwise let the user adjust the initial
                            // wait time for the beginning node
                            builder.createKineticObject(platform, pathPoints, pendulum);
                            platform = null;
                            platformPause = 0;
                            return;
                        }
                    }
                }
                // if we didn't click a previous point, just add it and continue
                pathPoints.add(new PathPoint(latestPoint, platformSpeed, platformPause));
                platformPause = 0;
                speedPoint = null;
            }
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (MouseButton.RIGHT.equals(mouseButtonDown)) {
            // render speed line
            if (startPoint != null && speedPoint != null) {
                shaper.setColor(Color.GOLD);
                shaper.line(startPoint.x, startPoint.y, speedPoint.x, speedPoint.y);
                LevelEditor.addStringForRender(String.format("%.0f speed", platformSpeed), new BitPoint(speedPoint.x, speedPoint.y + 10));
            }
        }
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
            if (currentPoint != null && !MouseButton.RIGHT.equals(mouseButtonDown)) {
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
