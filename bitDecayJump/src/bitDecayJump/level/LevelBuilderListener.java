package bitDecayJump.level;

import bitDecayJump.geom.BitPointInt;

/**
 * A simple listener. Only needs to listen for level changed because
 * {@link LevelBuilder} recreates the level a change occurs.
 * 
 * @author Monday
 *
 */
public interface LevelBuilderListener {
	public void levelChanged(Level level);

	//
	//	public void objectsAdded(Collection<LevelObject> objects);
	//
	//	public void objectsRemoved(Collection<LevelObject> objects);

	public void updateGrid(BitPointInt gridOffset, LevelObject[][] grid);
}
