package bitDecayJump.render.mouse;

import bitDecayJump.geom.BitPointInt;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SpawnMouseMode extends BaseMouseMode {

	public SpawnMouseMode(LevelBuilder builder) {
		super(builder);
	}

	@Override
	protected void mouseUpLogic(BitPointInt point) {
		builder.setSpawn(point);
	}

	@Override
	public void render(ShapeRenderer shaper) {
		super.render(shaper);
		if (endPoint != null) {
			shaper.begin(ShapeType.Filled);
			shaper.setColor(Color.YELLOW);
			shaper.circle(endPoint.x, endPoint.y, 7);
			shaper.setColor(Color.RED);
			shaper.circle(endPoint.x, endPoint.y, 4);
			shaper.end();
		}
	}

}
