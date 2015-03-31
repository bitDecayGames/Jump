package bitDecayJump.entity;

import bitDecayJump.*;
import bitDecayJump.geom.BitRectangle;
import bitDecayJump.input.*;
import bitDecayJump.level.*;
import bitDecayJump.state.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.bitdecay.engine.animation.AnimationState;
import com.bitdecay.engine.utilities.ImageUtilities;
import com.bitdecay.render.Renderable;

public class Player implements Updatable, Renderable {

	public BitBody playerBody;
	public PlayerInputHandler inputHandler;
	public AnimationState<JumperState> animations = new AnimationState<JumperState>();

	public Player(BitWorld world) {
		buildBody(world);
		buildAnimations();
	}

	private void buildBody(BitWorld world) {
		playerBody = new BitBody();
		playerBody.aabb = new BitRectangle(50, 50, 46, 46);
		playerBody.props = FileUtils.loadFileAs(JumperProps.class, Gdx.files.internal("props/graceProps").readString());
		playerBody.stateWatcher = new JumperStateWatcher(playerBody);
		world.addBody(playerBody);

		inputHandler = new PlayerInputHandler();
		inputHandler.setBody(playerBody, ControlMap.defaultMapping);
	}

	private void buildAnimations() {
		Animation runningRight = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "walk"));
		runningRight.setPlayMode(PlayMode.LOOP);
		animations.addState(JumperState.RIGHT_RUNNING, runningRight);

		Animation runningLeft = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "walkLeft"));
		runningLeft.setPlayMode(PlayMode.LOOP);
		animations.addState(JumperState.LEFT_RUNNING, runningLeft);

		Animation floatingRight = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "float"));
		floatingRight.setPlayMode(PlayMode.LOOP);
		animations.addState(JumperState.RIGHT_JUMPING, floatingRight);
		animations.addState(JumperState.RIGHT_APEX, floatingRight);
		animations.addState(JumperState.RIGHT_FALLING, floatingRight);

		Animation floatingLeft = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "floatLeft"));
		floatingLeft.setPlayMode(PlayMode.LOOP);
		animations.addState(JumperState.LEFT_JUMPING, floatingLeft);
		animations.addState(JumperState.LEFT_APEX, floatingLeft);
		animations.addState(JumperState.LEFT_FALLING, floatingLeft);

		Animation standLeft = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "standLeft"));
		standLeft.setPlayMode(PlayMode.LOOP);
		animations.addState(JumperState.LEFT_STANDING, standLeft);

		Animation standRight = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "standRight"));
		standRight.setPlayMode(PlayMode.LOOP);
		animations.addState(JumperState.RIGHT_STANDING, standRight);
	}

	@Override
	public void update(float delta) {
		inputHandler.update();
		animations.update(delta);
		animations.setState((JumperState) playerBody.stateWatcher.getState());
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(animations.getKeyFrame(), playerBody.aabb.xy.x, playerBody.aabb.xy.y);
	}
}
