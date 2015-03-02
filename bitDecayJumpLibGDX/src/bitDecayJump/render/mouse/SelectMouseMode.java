package bitDecayJump.render.mouse;

import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;
import bitDecayJump.render.LevelEditor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * an empty mouse mode
 * 
 * @author Monday
 *
 */
public class SelectMouseMode extends BaseMouseMode {
	private static final Color LINE_COLOR = new Color(0, 0, 255, 1f);
	private Texture fillTexture;

	public SelectMouseMode(LevelBuilder builder) {
		super(builder);
		fillTexture = new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/fill.png"));
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
	public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
		if (startPoint != null && endPoint != null) {
			spriteBatch.draw(fillTexture, startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);

			shaper.setColor(LINE_COLOR);
			shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
		}
	}

}
