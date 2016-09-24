package com.bitdecay.jump.leveleditor.render.mouse;

import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.builder.ILevelBuilder;

public class DeleteMouseMode extends BaseMouseMode {

    public DeleteMouseMode(ILevelBuilder builder) {
        super(builder);
    }

    @Override
    protected void mouseUpLogic(BitPointInt point, MouseButton button) {
        if (startPoint.equals(endPoint)) {
            // only delete if the user didn't move the mouse. UX nicety.
            builder.selectObject(point, false);
            builder.deleteSelected();
        }
    }

    @Override
    public String getToolTip() {
        return "Delete";
    }

}
