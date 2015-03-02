package bitDecayJump.render.mouse;

import bitDecayJump.geom.BitPointInt;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface MouseMode {
	public void mouseDown(BitPointInt point);

	/**
	 * mouse moved while button is down
	 * 
	 * @param point
	 */
	public void mouseDragged(BitPointInt point);

	public void mouseUp(BitPointInt point);

	/**
	 * Mouse moved without button down
	 * 
	 * @param point
	 */
	public void mouseMoved(BitPointInt point);

	public void render(ShapeRenderer shaper, SpriteBatch spriteBatch);
}
