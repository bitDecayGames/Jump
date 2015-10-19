package com.bitdecay.jump.level.builder;

import java.util.Collection;

import com.bitdecay.jump.geom.BitPointInt;
import com.bitdecay.jump.level.Level;

/**
 * A simple listener. Only needs to listen for level changed because
 * {@link LevelBuilder} recreates the level a change occurs.
 * 
 * @author Monday
 *
 */
public interface LevelBuilderListener {
	void levelChanged(Level level);
}
