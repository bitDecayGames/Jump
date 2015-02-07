package bitDecayJump.render;

import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CreateMouseMode extends BaseMouseMode {

	public CreateMouseMode(LevelBuilder builder) {
		super(builder);
	}

	@Override
	public void mouseDown(BitPointInt point) {
		startPoint = GeomUtils.snap(point, builder.level.tileSize);
	}

	@Override
	public void mouseDragged(BitPointInt point) {
		super.mouseDragged(point);
		endPoint = GeomUtils.snap(point, builder.level.tileSize);
	}

	@Override
	public void mouseUpLogic(BitPointInt point) {
		endPoint = GeomUtils.snap(point, builder.level.tileSize);
		builder.createObject(startPoint, endPoint);
	}

	@Override
	public void render(ShapeRenderer shaper) {
		if (startPoint != null && endPoint != null) {
			shaper.setColor(Color.RED);
			shaper.begin(ShapeType.Line);
			shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
			shaper.end();
		}
	}

}
