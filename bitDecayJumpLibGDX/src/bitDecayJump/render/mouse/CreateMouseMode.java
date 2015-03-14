package bitDecayJump.render.mouse;

import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CreateMouseMode extends BaseMouseMode {

	public CreateMouseMode(LevelBuilder builder) {
		super(builder);
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
		builder.createLevelObject(startPoint, endPoint);
	}

	@Override
	public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
		if (startPoint != null && endPoint != null) {
			shaper.setColor(Color.RED);
			shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
		}
	}

}
