package bitDecayJump.render;

import bitDecayJump.*;
import bitDecayJump.geom.BitRectangle;
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
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.WHITE);
		LevelObject[][] objects = world.getObjects();
		for (int x = 0; x < objects.length; x++) {
			LevelObject[] column = objects[x];
			for (int y = 0; y < column.length; y++) {
				if (column[y] != null) {
					LevelObject levelObject = column[y];
					int leftX = levelObject.rect.xy.x;
					int rightX = levelObject.rect.xy.x + levelObject.rect.width;
					int bottomY = levelObject.rect.xy.y;
					int topY = levelObject.rect.xy.y + levelObject.rect.height;
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
					//					renderer.rect((x + world.getBodyOffset().x) * world.getTileSize(), (y + world.getBodyOffset().y) * world.getTileSize(),
					//							world.getTileSize(), world.getTileSize());
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
