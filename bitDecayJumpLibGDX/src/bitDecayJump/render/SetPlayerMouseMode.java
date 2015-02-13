package bitDecayJump.render;

import bitDecayJump.*;
import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SetPlayerMouseMode extends BaseMouseMode {

	private BitWorld world;
	private BitBodyProps props;

	public BitBody lastPlayer;

	public SetPlayerMouseMode(LevelBuilder builder, BitWorld world) {
		super(builder);
		this.world = world;
		props = new BitBodyProps();
		props.bodyType = BodyType.DYNAMIC;
	}

	@Override
	protected void mouseUpLogic(BitPointInt point) {
		if (lastPlayer != null) {
			world.removeBody(lastPlayer);
		}
		lastPlayer = world.createBody(GeomUtils.makeRect(startPoint, endPoint), props);
	}

	@Override
	public void render(ShapeRenderer shaper) {
		if (startPoint != null && endPoint != null) {
			shaper.setColor(Color.ORANGE);
			shaper.begin(ShapeType.Line);
			shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
			shaper.end();
		}
	}
}
