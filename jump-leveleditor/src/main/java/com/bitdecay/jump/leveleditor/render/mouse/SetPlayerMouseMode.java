package com.bitdecay.jump.leveleditor.render.mouse;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bitdecay.jump.BitBody;
import com.bitdecay.jump.JumperBody;
import com.bitdecay.jump.collision.BitWorld;
import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.geom.GeomUtils;
import com.bitdecay.jump.leveleditor.input.ControlMap;
import com.bitdecay.jump.leveleditor.input.PlayerInputHandler;
import com.bitdecay.jump.leveleditor.tools.BitColors;
import com.bitdecay.jump.state.JumperStateWatcher;

/**
 * Currently this mode only gets the world via the constructor -- so if a level changes the world out from under us, we
 * will be adding a body to a world that nothing is looking at.
 */
public class SetPlayerMouseMode extends BaseMouseMode {

    private BitWorld world;
    private JumperBody body = new JumperBody();

    public BitBody lastPlayer;
    private PlayerInputHandler playerController;
    private ControlMap controls;

    public SetPlayerMouseMode(BitWorld world, PlayerInputHandler playerController) {
        super(null);
        this.world = world;
        this.playerController = playerController;
        this.controls = ControlMap.defaultMapping;
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {
            if (lastPlayer != null) {
                world.removeBody(lastPlayer);
            }
            body.aabb = GeomUtils.makeRect(startPoint, endPoint);
            world.addBody(body);
            lastPlayer = body;
            lastPlayer.stateWatcher = new JumperStateWatcher();
            playerController.setBody(lastPlayer, controls);
        }
    }

    @Override
    public void render(ShapeRenderer shaper, SpriteBatch spriteBatch) {
        if (startPoint != null && endPoint != null) {
            shaper.setColor(BitColors.SPAWN);
            shaper.rect(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        }
    }

    @Override
    public String getToolTip() {
        return "Drop a player object into the level";
    }
}
