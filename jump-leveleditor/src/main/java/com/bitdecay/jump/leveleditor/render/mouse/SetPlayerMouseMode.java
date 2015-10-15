package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.*;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.level.LevelBuilder;
import com.bitdecay.jump.leveleditor.input.ControlMap;
import com.bitdecay.jump.leveleditor.input.PlayerInputHandler;
import com.bitdecay.jump.state.JumperStateWatcher;

public class SetPlayerMouseMode extends BaseMouseMode {

    private BitWorld world;
    private JumperBody body;

    public BitBody lastPlayer;
    private PlayerInputHandler playerController;
    private ControlMap controls;

    public SetPlayerMouseMode(LevelBuilder builder, BitWorld world, PlayerInputHandler playerController, JumperBody body) {
        super(builder);
        this.world = world;
        this.playerController = playerController;
        this.body = body;
        this.controls = ControlMap.defaultMapping;
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
            if (lastPlayer != null) {
                world.removeBody(lastPlayer);
            }
            body.aabb = GeomUtils.makeRect(startPoint, endPoint);
            // TODO: figure out why this isn't bringing over the jumperprops values
            world.addBody(body);
            lastPlayer = body;
            lastPlayer.stateWatcher = new JumperStateWatcher();
            playerController.setBody(lastPlayer, controls);
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (startPoint != null && endPoint != null) {
            shaper.setColor(Color.ORANGE);
            shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        }
    }

    @Override
    public String getToolTip() {
        return "Drop a player object into the level";
    }
}
