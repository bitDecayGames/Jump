package bitDecayJump.render.mouse;

import bitDecayJump.geom.BitPointInt;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class BaseMouseMode implements MouseMode {

	public LevelBuilder builder;
	public BitPointInt startPoint;
	public BitPointInt endPoint;

	public BaseMouseMode(LevelBuilder builder) {
		this.builder = builder;
	}

	@Override
	public void mouseDown(BitPointInt point) {
		startPoint = point;
	}

	@Override
	public void mouseDragged(BitPointInt point) {
		endPoint = point;
	}

	@Override
	public void mouseUp(BitPointInt point) {
		if (startPoint == null) {
			return;
		}
		endPoint = point;
		mouseUpLogic(point);
		startPoint = null;
		endPoint = null;
	}

	protected abstract void mouseUpLogic(BitPointInt point);

	@Override
	public void mouseMoved(BitPointInt point) {

	}

	@Override
	public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
	}

}
