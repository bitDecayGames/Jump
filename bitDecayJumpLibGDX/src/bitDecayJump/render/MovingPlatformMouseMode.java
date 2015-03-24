package bitDecayJump.render;

import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;
import bitDecayJump.render.mouse.BaseMouseMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MovingPlatformMouseMode extends BaseMouseMode {
	private EditState state = EditState.DRAW_PLATFORM;
	private LevelBuilder builder;

	public MovingPlatformMouseMode(LevelBuilder builder) {
		super(builder);
		this.builder = builder;
	}

	@Override
	public void mouseDown(BitPointInt point) {
		switch (state) {
		case DRAW_PLATFORM:
			startPoint = GeomUtils.snap(point, builder.tileSize);
			break;
		case FIRST_ANCHOR:
			break;
		case SECOND_ANCHOR:
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseDragged(BitPointInt point) {
		switch (state) {
		case DRAW_PLATFORM:
			endPoint = GeomUtils.snap(point, builder.tileSize);
			break;
		case FIRST_ANCHOR:
			break;
		case SECOND_ANCHOR:
			break;
		default:
			break;
		}
	}

	@Override
	protected void mouseUpLogic(BitPointInt point) {
		switch (state) {
		case DRAW_PLATFORM:
			endPoint = GeomUtils.snap(point, builder.tileSize);
			builder.createKineticObject(startPoint, endPoint);
			break;
		case FIRST_ANCHOR:
			break;
		case SECOND_ANCHOR:
			break;
		default:
			break;
		}
	}

	@Override
	public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
		if (startPoint != null && endPoint != null) {
			shaper.setColor(Color.ORANGE);
			shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
		}
	}

	private enum EditState {
		DRAW_PLATFORM,
		FIRST_ANCHOR,
		SECOND_ANCHOR;
	}

	@Override
	public String getToolTip() {
		return "Create a kinetic object that follows a path";
	}
}
