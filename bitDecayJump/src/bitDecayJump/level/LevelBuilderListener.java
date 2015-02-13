package bitDecayJump.level;

/**
 * A simple listener. Only needs to listen for level changed because
 * {@link LevelBuilder} recreates the level a change occurs.
 * 
 * @author Monday
 *
 */
public interface LevelBuilderListener {
	public void levelChanged(Level level);
}
