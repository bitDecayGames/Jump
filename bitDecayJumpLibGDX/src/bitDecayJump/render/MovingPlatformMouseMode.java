package bitDecayJump.render;

import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;
import bitDecayJump.render.mouse.BaseMouseMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MovingPlatformMouseMode extends BaseMouseMode {
	private LevelBuilder builder;

	public MovingPlatformMouseMode(LevelBuilder builder) {
		super(builder);
		this.builder = builder;
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
		endPoint = GeomUtils.snap(point, builder.tileSize);
		builder.createKineticObject(startPoint, endPoint);
	}

	@Override
	public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
		if (startPoint != null && endPoint != null) {
			shaper.setColor(Color.RED);
			shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
		}
	}

	@Override
	public String getToolTip() {
		return "Create a kinetic object that follows a path";
	}
}
