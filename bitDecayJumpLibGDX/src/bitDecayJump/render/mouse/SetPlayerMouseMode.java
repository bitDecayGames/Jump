package bitDecayJump.render.mouse;

import bitDecayJump.*;
import bitDecayJump.geom.*;
import bitDecayJump.level.LevelBuilder;
import bitDecayJump.render.PlayerController;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SetPlayerMouseMode extends BaseMouseMode {

	private BitWorld world;
	private BitBodyProps props;

	public BitBody lastPlayer;
	private PlayerController playerController;

	public SetPlayerMouseMode(LevelBuilder builder, BitWorld world, PlayerController playerController, BitBodyProps props) {
		super(builder);
		this.world = world;
		this.playerController = playerController;
		this.props = props;
	}

	@Override
	protected void mouseUpLogic(BitPointInt point) {
		if (lastPlayer != null) {
			world.removeBody(lastPlayer);
		}
		lastPlayer = world.createBody(GeomUtils.makeRect(startPoint, endPoint), props);
		playerController.setBody(lastPlayer);
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
