package com.bitdecay.jump.leveleditor.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.gdx.engine.animation.AnimationState;
import com.bitdecay.jump.gdx.engine.utilities.ImageUtilities;
import com.bitdecay.jump.gdx.render.Renderable;
import com.bitdecay.jump.geom.BitRectangle;
import com.bitdecay.jump.level.FileUtils;
import com.bitdecay.jump.level.Updatable;
import com.bitdecay.jump.leveleditor.input.ControlMap;
import com.bitdecay.jump.leveleditor.input.PlayerInputHandler;
import com.bitdecay.jump.state.JumperState;
import com.bitdecay.jump.state.JumperStateWatcher;

public class Player implements Updatable, Renderable {

    public BitBody playerBody;
    public PlayerInputHandler inputHandler;
    public AnimationState animations = new AnimationState();

    public Player(BitWorld world) {
        buildBody(world);
        buildAnimations();
    }

    private void buildBody(BitWorld world) {
        playerBody = new BitBody(FileUtils.loadFileAs(JumperBody.class, Gdx.files.internal("props/graceProps").readString()));
        playerBody.aabb = new BitRectangle(50, 50, 46, 46);
        playerBody.stateWatcher = new JumperStateWatcher();
        playerBody.stateWatcher.addListener(animations);
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
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(animations.getKeyFrame(), playerBody.aabb.xy.x, playerBody.aabb.xy.y);
    }
}
