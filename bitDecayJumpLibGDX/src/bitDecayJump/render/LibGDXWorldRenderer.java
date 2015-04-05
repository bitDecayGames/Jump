package bitDecayJump.render;

import bitDecayJump.*;
import bitDecayJump.geom.*;
import bitDecayJump.level.*;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class LibGDXWorldRenderer implements BitWorldRenderer {
	private BitWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer renderer;

	public LibGDXWorldRenderer(BitWorld world, OrthographicCamera cam) {
		this.world = world;
		this.cam = cam;
		renderer = new ShapeRenderer();
	}

	@Override
	public void render() {
		BitRectangle view = new BitRectangle(cam.position.x, cam.position.y, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);
		view.translate(-view.width / 2, -view.height / 2);
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.WHITE);
		TileObject[][] gridObjects = world.getGrid();
		for (int x = 0; x < gridObjects.length; x++) {
			TileObject[] column = gridObjects[x];
			for (int y = 0; y < column.length; y++) {
				if (column[y] != null) {
					TileObject levelObject = column[y];
					if (GeomUtils.intersection(view, levelObject.rect) == null) {
						// don't even attempt to draw if not on camera
						continue;
					}
					float leftX = levelObject.rect.xy.x;
					float rightX = levelObject.rect.xy.x + levelObject.rect.width;
					float bottomY = levelObject.rect.xy.y;
					float topY = levelObject.rect.xy.y + levelObject.rect.height;
					if ((levelObject.nValue & Neighbor.UP) == 0) {
						renderer.setColor(Color.WHITE);
						renderer.line(leftX, topY, rightX, topY);
					}
					if ((levelObject.nValue & Neighbor.DOWN) == 0) {
						renderer.setColor(Color.WHITE);
						renderer.line(leftX, bottomY, rightX, bottomY);
					}
					if ((levelObject.nValue & Neighbor.LEFT) == 0) {
						renderer.setColor(Color.WHITE);
						renderer.line(leftX, bottomY, leftX, topY);
					}
					if ((levelObject.nValue & Neighbor.RIGHT) == 0) {
						renderer.setColor(Color.WHITE);
						renderer.line(rightX, bottomY, rightX, topY);
					}
				}
			}
		}
		for (BitBody body : world.getBodies()) {
			switch (body.props.bodyType) {
			case DYNAMIC:
				renderer.setColor(Color.CYAN);
				break;
			case KINETIC:
				renderer.setColor(Color.BLUE);
				break;
			case STATIC:
				renderer.setColor(Color.TEAL);
				break;
			default:
				renderer.setColor(Color.WHITE);
				break;
			}
			renderer.rect(body.aabb.xy.x, body.aabb.xy.y, body.aabb.width, body.aabb.height);
		}

		renderer.setColor(Color.RED);
		for (BitRectangle col : world.collisions) {
			renderer.rect(col.xy.x, col.xy.y, col.width, col.height);
		}
		renderer.end();
	}
}
