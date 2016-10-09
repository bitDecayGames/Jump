package com.bitdecay.jump.leveleditor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Launcher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        float scale = 1;
        config.width = (int) (1600f * scale);
        config.height = (int) (900f * scale);
        config.title = "Jump Level Editor";
        new LwjglApplication(new EditorApp(), config);
    }
}
