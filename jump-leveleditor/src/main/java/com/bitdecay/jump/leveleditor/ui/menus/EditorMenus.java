package com.bitdecay.jump.leveleditor.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.ui.OptionsMode;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a separate class from LevelEditor to try to make things easier to read.
 * Created by Monday on 10/10/2015.
 */
public class EditorMenus {

    private final LevelEditor levelEditor;

    private final Stage stage;
    private final Skin skin;

    private Map<MenuPage, Actor> menus = new HashMap<>();
    private Actor currentMenu;

    public EditorMenus(LevelEditor levelEditor) {
        this.levelEditor = levelEditor;
        TextureAtlas menuAtlas = new TextureAtlas(Gdx.files.internal("skins/ui.atlas"));
        skin = new Skin(Gdx.files.internal("skins/menu-skin.json"), menuAtlas);
        stage = new Stage();
        menus.put(MenuPage.MainMenu, buildMainMenu());
        menus.put(MenuPage.CreateMenu, buildCreateMenu());
        menus.put(MenuPage.PlayerMenu, buildPlayerMenu());
        menuTransition(MenuPage.MainMenu);
    }

    private Actor buildMainMenu() {
        Table menu = new Table();
        menu.setWidth(stage.getWidth());
        menu.align(Align.top);

        TextButton toolsBtn;
        TextButton playerPropsBtn;
        TextButton saveBtn;
        TextButton loadBtn;
        TextButton quitBtn;

        toolsBtn = new TextButton("tools", skin, "button");
        toolsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuTransition(MenuPage.CreateMenu);
            }
        });

        playerPropsBtn = new TextButton("player properties", skin, "button");
        playerPropsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuTransition(MenuPage.PlayerMenu);
            }
        });

        saveBtn = new TextButton("save", skin, "button");
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("TODO: Save level");
                levelEditor.saveLevel();
            }
        });

        loadBtn = new TextButton("load", skin, "button");
        loadBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("TODO: Load level");
                levelEditor.loadLevel();
            }
        });

        quitBtn = new TextButton("exit", skin, "button");
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        menu.add(toolsBtn).height(60);
        menu.add(playerPropsBtn).height(60);
        menu.add(saveBtn).height(60);
        menu.add(loadBtn).height(60);
        menu.add(quitBtn).height(60);
        menu.align(Align.topLeft);
        menu.setFillParent(true);
        menu.setVisible(false);
        stage.addActor(menu);
        return menu;
    }

    public Actor buildCreateMenu() {
        Table menu = new Table();
        menu.setWidth(stage.getWidth());
        menu.align(Align.top);

        for(OptionsMode mode : OptionsMode.values()) {
            if(mode.group == 0) {
                TextButton button = new TextButton(mode.label, skin, "button");
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        levelEditor.setMode(mode);
                    }
                });
                menu.add(button).height(60).padBottom(20);
            }
        }

        TextButton backBtn;
        backBtn = new TextButton("back", skin, "button");
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuTransition(MenuPage.MainMenu);
            }
        });

        addBackButtonAndFinalizeMenu(menu, MenuPage.MainMenu);
        return menu;
    }

    private Actor buildPlayerMenu() {
        Table menu = new Table();
        menu.setWidth(stage.getWidth());
        menu.align(Align.top);

        for(OptionsMode mode : OptionsMode.values()) {
            if(mode.group == 1) {
                TextButton button = new TextButton(mode.label, skin, "button");
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        levelEditor.setMode(mode);
                    }
                });
                menu.add(button).height(60).padBottom(20);
            }
        }

        addBackButtonAndFinalizeMenu(menu, MenuPage.MainMenu);
        return menu;
    }

    private void addBackButtonAndFinalizeMenu(Table menu, MenuPage backTo) {
        TextButton backBtn;
        backBtn = new TextButton("back", skin, "button");
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuTransition(backTo);
                levelEditor.setMode(OptionsMode.SELECT);
            }
        });
        menu.add(backBtn).height(60).padBottom(20);
        menu.align(Align.topLeft);
        menu.setFillParent(true);
        menu.setVisible(false);
        stage.addActor(menu);
    }

    private void menuTransition(MenuPage to) {
        if (currentMenu != null) {
            currentMenu.setVisible(false);
        }
        menus.get(to).setVisible(true);
        currentMenu = menus.get(to);
    }

    public Stage getStage() {
        return stage;
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }
}