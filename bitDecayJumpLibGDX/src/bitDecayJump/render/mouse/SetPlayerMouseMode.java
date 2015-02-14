package bitDecayJump.render.mouse;

import bitDecayJump.*;
import bitDecayJump.geom.*;
import bitDecayJump.input.*;
import bitDecayJump.level.LevelBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SetPlayerMouseMode extends BaseMouseMode {

	private BitWorld world;
	private BitBodyProps props;

	public BitBody lastPlayer;
	private PlayerController playerController;
	private ControlMap controls;

	public SetPlayerMouseMode(LevelBuilder builder, BitWorld world, PlayerController playerController, BitBodyProps props) {
		super(builder);
		this.world = world;
		this.playerController = playerController;
		this.props = props;
		this.controls = ControlMap.defaultMapping;
	}

	@Override
	protected void mouseUpLogic(BitPointInt point) {
		if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
			if (lastPlayer != null) {
				world.removeBody(lastPlayer);
			}
			lastPlayer = world.createBody(GeomUtils.makeRect(startPoint, endPoint), props);
			playerController.setBody(lastPlayer, controls);
		}
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
