package bitDecayJump.render;

import bitDecayJump.geom.BitPointInt;
import bitDecayJump.level.LevelBuilder;

public class DeleteMouseMode extends BaseMouseMode {

	public DeleteMouseMode(LevelBuilder builder) {
		super(builder);
	}

	@Override
	protected void mouseUpLogic(BitPointInt point) {
		if (startPoint.equals(endPoint)) {
			// only delete if the user didn't move the mouse. UX nicety.
			builder.selectObject(point, false);
			builder.deleteSelected();
		}
	}

}
