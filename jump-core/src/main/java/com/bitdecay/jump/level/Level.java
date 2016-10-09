package com.bitdecay.jump.level;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Level {
	public int tileSize = 16;
	public LevelLayers layers;
	public List<TriggerObject> triggers;
	/**
	 * This offset is to compensate from how far from the origin the 2d array
	 * sits. This is needed due to the array being 'shrink wrapped' to the grid
	 * objects.
	 */
//	public BitPointInt gridOffset;

	@JsonIgnore
	public DebugSpawnObject debugSpawn;
	public int theme;

	public Level() {
		// Here for JSON persistence/parsing
	}

	public Level(int unitSize) {
		this.tileSize = unitSize;
		layers = new LevelLayers(unitSize);
	}

	public Level(Level level) {
		tileSize = level.tileSize;
		layers = new LevelLayers(tileSize);
		debugSpawn = level.debugSpawn;
		theme = level.theme;
	}
}
