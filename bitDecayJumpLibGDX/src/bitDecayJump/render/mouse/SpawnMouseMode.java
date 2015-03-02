package bitDecayJump.render.mouse;

import bitDecayJump.geom.BitPointInt;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SpawnMouseMode extends BaseMouseMode {

	public SpawnMouseMode(LevelBuilder builder) {
		super(builder);
	}

	@Override
	protected void mouseUpLogic(BitPointInt point) {
		builder.setSpawn(point);
	}

	@Override
	public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
		if (endPoint != null) {
			shaper.setColor(Color.YELLOW);
			shaper.circle(endPoint.x, endPoint.y, 7);
			shaper.setColor(Color.RED);
			shaper.circle(endPoint.x, endPoint.y, 4);
		}
	}

}
