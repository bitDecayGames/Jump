package com.bitdecay.jump.leveleditor.utils;

import com.bitdecay.jump.level.FileUtils;
import com.bitdecay.jump.level.Level;
import com.bitdecay.jump.level.builder.LevelBuilder;

import javax.swing.*;
import java.io.File;

public class LevelUtilities {
	public static Level loadLevel() {
		return FileUtils.loadFileAs(Level.class);
	}

	public static Level loadLevel(String fileName) {
		return FileUtils.loadFileAs(Level.class, new File(fileName));
	}

	public static Level loadLevel(File file) {

		return null;
	}

	public static Level saveLevel(LevelBuilder builder, boolean canCancel) {
		if(canCancel) {
			UIManager.put("FileChooser.cancelButtonText", "Cancel");
		} else {
			UIManager.put("FileChooser.cancelButtonText", "Don't Save");
		}
		Level level = builder.optimizeLevel();
		String savedContent = FileUtils.saveToFile(level);
		if (savedContent != null) {
			return level;
		} else {
			return null;
		}
	}
}
