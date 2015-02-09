package bitDecayJump.render;

import bitDecayJump.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
		for (BitBody body : world.getBodies()) {
			renderer.rect(body.aabb.xy.x, body.aabb.xy.y, body.aabb.width, body.aabb.height);
		}
		renderer.end();
	}
}
