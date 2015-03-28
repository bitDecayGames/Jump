package bitDecay.jump.game;

import com.badlogic.gdx.Game;
import com.bitdecay.engine.GameFactory;
import com.bitdecay.engine.utilities.DevConstants;
import com.bitdecay.engine.utilities.DevConstants.Constants;
import com.bitdecay.engine.utilities.ImageUtilities;

public class Kenney extends Game {

	@Override
	public void create() {
		ImageUtilities.initialize();
		setScreen(new GameScreen());
	}

}
