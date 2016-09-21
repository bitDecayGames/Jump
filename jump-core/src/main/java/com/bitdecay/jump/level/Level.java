package com.bitdecay.jump.level;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Level {
	public int tileSize = 16;
	public LevelLayers layers;
//	public TileObject[][] gridObjects;
//	public List<LevelObject> otherObjects;
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
//		layers.addLayer(0, new TileObject[10][10]);
//		gridObjects = new TileObject[10][10];
//		otherObjects = new ArrayList<>();
		triggers = new ArrayList<>();

//        gridOffset = new BitPointInt();
	}

	public Level(Level level) {
		tileSize = level.tileSize;
		layers = new LevelLayers(tileSize);
//		gridObjects = level.gridObjects;
//		otherObjects = level.otherObjects;
		triggers = level.triggers;
//		gridOffset = level.gridOffset;
		debugSpawn = level.debugSpawn;
		theme = level.theme;
	}
}
