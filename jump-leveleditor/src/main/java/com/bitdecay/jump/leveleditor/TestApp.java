package com.bitdecay.jump.leveleditor;

import com.badlogic.gdx.Game;
import com.bitdecay.jump.leveleditor.render.LevelEditor;

public class TestApp extends Game {

    @Override
    public void create() {
        setScreen(new LevelEditor());
    }

}