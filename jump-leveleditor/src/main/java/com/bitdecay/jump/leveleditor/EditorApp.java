package com.bitdecay.jump.leveleditor;

import com.badlogic.gdx.Game;
import com.bitdecay.jump.leveleditor.example.ExampleEditorLevel;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

public class EditorApp extends Game {

    @Override
    public void create() {
        ExampleEditorLevel level = new ExampleEditorLevel();
        LevelEditor.setAssetsFolder("jump-leveleditor/assets");
        setScreen(new LevelEditor(level));
    }

}
