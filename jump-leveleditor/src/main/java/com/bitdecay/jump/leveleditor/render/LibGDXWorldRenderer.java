package com.bitdecay.jump.leveleditor.render;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.controller.PathedBodyController;
import com.bitdecay.jump.geom.BitPoint;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.geom.PathPoint;
import com.bitdecay.jump.level.Direction;
import com.bitdecay.jump.level.TileBody;
import com.bitdecay.jump.leveleditor.tools.BitColors;

import java.util.List;

public class LibGDXWorldRenderer implements BitWorldRenderer {
    private ShapeRenderer renderer;

    public LibGDXWorldRenderer() {
        renderer = new ShapeRenderer();
    }

    @Override
    public void render(BitWorld world, OrthographicCamera cam) {
        BitRectangle view = new BitRectangle(cam.position.x, cam.position.y, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
        view.translate(-view.width / 2, -view.height / 2);
        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeType.Line);
        renderer.setColor(BitColors.STATIC_OBJECT);
        BitBody[][] gridObjects = world.getGrid();
        for (int x = 0; x < gridObjects.length; x++) {
            BitBody[] column = gridObjects[x];
            for (int y = 0; y < column.length; y++) {
                if (column[y] != null) {
                    BitBody levelObject = column[y];
                    if (GeomUtils.intersection(view, levelObject.aabb) == null) {
                        // don't even attempt to draw if not on camera
                        continue;
                    }
                    float leftX = levelObject.aabb.xy.x;
                    float rightX = levelObject.aabb.xy.x + levelObject.aabb.width;
                    float bottomY = levelObject.aabb.xy.y;
                    float topY = levelObject.aabb.xy.y + levelObject.aabb.height;
                    int nValue = 0;
                    if (levelObject instanceof TileBody) {
                        nValue = ((TileBody) levelObject).nValue;
                    }
                    if (((TileBody) levelObject).collisionAxis != null) {
                        // currently we are just assuming it's a one-way platform
                        renderer.setColor(BitColors.COLLISION);
                        renderer.line(leftX, topY, rightX, topY);
                        continue;
                    } else {
                        renderer.setColor(BitColors.STATIC_OBJECT);
                    }


                    if ((nValue & Direction.UP) == 0) {
                        renderer.line(leftX, topY, rightX, topY);
                    }
                    if ((nValue & Direction.DOWN) == 0) {
                        renderer.line(leftX, bottomY, rightX, bottomY);
                    }
                    if ((nValue & Direction.LEFT) == 0) {
                        renderer.line(leftX, bottomY, leftX, topY);
                    }
                    if ((nValue & Direction.RIGHT) == 0) {
                        renderer.line(rightX, bottomY, rightX, topY);
                    }
                }
            }
        }
        renderBodies(renderer, world.getDynamicBodies());
        renderBodies(renderer, world.getKineticBodies());
        renderBodies(renderer, world.getStaticBodies());

        renderer.setColor(BitColors.COLLISION);
        for (BitRectangle col : world.unresolvedCollisions) {
            renderer.rect(col.xy.x, col.xy.y, col.width, col.height);
        }
        renderer.setColor(BitColors.RESOLVED_COLLISION);
        for (BitRectangle col : world.resolvedCollisions) {
            renderer.rect(col.xy.x, col.xy.y, col.width, col.height);
        }
        renderer.end();
    }

    private void renderBodies(ShapeRenderer renderer, List<BitBody> bodies) {
        for (BitBody body : bodies) {
            if (body.stateWatcher != null) {
                LevelEditor.addStringForRender(body.stateWatcher.getState().toString(), new BitPoint(body.aabb.xy.x, body.aabb.xy.y + body.aabb.height + 20));
            }
            if (body.controller != null && body.controller instanceof PathedBodyController) {
                renderer.setColor(BitColors.KINETIC_PATH);
                PathPoint lastPoint = null;
                PathedBodyController controller = (PathedBodyController) body.controller;
                if (controller.path.size() > 1) {
                    for (PathPoint pathNode : controller.path) {
                        if (lastPoint == null) {
                            lastPoint = pathNode;
                            renderer.setColor(BitColors.BACKGROUND_KINETIC);
                            renderer.rect(pathNode.destination.x, pathNode.destination.y, body.aabb.width, body.aabb.height);
                            renderer.setColor(BitColors.SELECTABLE);
                            renderer.circle(pathNode.destination.x + body.aabb.width / 2, pathNode.destination.y + body.aabb.height / 2, 4);
                        } else {
                            renderer.setColor(BitColors.BACKGROUND_KINETIC);
                            renderer.rect(pathNode.destination.x, pathNode.destination.y, body.aabb.width, body.aabb.height);
                            renderer.setColor(BitColors.KINETIC_PATH);
                            renderer.line(lastPoint.destination.x + body.aabb.width/2, lastPoint.destination.y + body.aabb.height/2, pathNode.destination.x + body.aabb.width/2, pathNode.destination.y + body.aabb.height/2);
                            renderer.circle(pathNode.destination.x + body.aabb.width/2, pathNode.destination.y + body.aabb.height/2, 4);
                            lastPoint = pathNode;
                        }
                    }
                    if (!controller.pendulum) {
                        renderer.line(lastPoint.destination.x + body.aabb.width/2, lastPoint.destination.y + body.aabb.height/2, controller.path.get(0).destination.x + body.aabb.width/2, controller.path.get(0).destination.y + body.aabb.height/2);
                    }
                }
            }
            if (!body.active) {
                renderer.setColor(BitColors.INACTIVE_OBJECT);
            } else if (body.parent != null) {
                renderer.setColor(BitColors.CHILD_OBJECT);
            } else if (body.children.size() > 0) {
                renderer.setColor(BitColors.PARENT_OBJECT);
            } else {
                switch (body.bodyType) {
                    case DYNAMIC:
                        renderer.setColor(BitColors.DYNAMIC_OBJECT);
                        break;
                    case KINETIC:
                        renderer.setColor(BitColors.KINETIC_OBJECT);
                        break;
                    case STATIC:
                        renderer.setColor(BitColors.STATIC_OBJECT);
                        break;
                    default:
                        renderer.setColor(BitColors.STATIC_OBJECT);
                        break;
                }
            }

            renderer.rect(body.aabb.xy.x, body.aabb.xy.y, body.aabb.width, body.aabb.height);
            if (body.velocity.x != 0 || body.velocity.y != 0) {
                float x = body.aabb.xy.x + body.aabb.width / 2;
                float y = body.aabb.xy.y + body.aabb.height / 2;
                renderer.setColor(BitColors.SPEED);
                renderer.line(x, y, x + body.velocity.x, y + body.velocity.y);
            }
        }
    }
}
