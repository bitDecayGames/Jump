package bitDecayJump.render.mouse;

import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * an empty mouse mode
 * 
 * @author Monday
 *
 */
public class SelectMouseMode extends BaseMouseMode {
	public SelectMouseMode(LevelBuilder builder) {
		super(builder);
	}

	@Override
	public void mouseUpLogic(BitPointInt point) {
		boolean shift = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Keys.SHIFT_RIGHT);
		if (startPoint.equals(endPoint)) {
			builder.selectObject(startPoint, shift);
		} else {
			BitRectangle rect = GeomUtils.makeRect(startPoint, endPoint);
			builder.selectObjects(rect, shift);
		}
	}

	@Override
	public void render(ShapeRenderer shaper) {
		if (startPoint != null && endPoint != null) {
			shaper.setColor(Color.WHITE);
			shaper.begin(ShapeType.Line);
			shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
			shaper.end();
		}
	}

}
