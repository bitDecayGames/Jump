package com.bitdecay.jump.leveleditor.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.jump.level.builder.LevelObject;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.ui.OptionsMode;

import java.util.HashMap;
import java.util.List;
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

    public EditorMenus(LevelEditor levelEditor, EditorHook hooker) {
        this.levelEditor = levelEditor;
        TextureAtlas menuAtlas = new TextureAtlas(Gdx.files.internal("skins/ui.atlas"));
        skin = new Skin(Gdx.files.internal("skins/menu-skin.json"), menuAtlas);
        stage = new Stage();
//        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        menus.put(MenuPage.MainMenu, buildMainMenu());
        menus.put(MenuPage.CreateMenu, buildCreateMenu());
        menus.put(MenuPage.PlayerMenu, buildPlayerMenu());
//        menus.put(MenuPage.LevelObjectMenu, buildObjectMenu(hooker.getCustomObjects()));
        buildObjectMenu(hooker.getCustomObjects());
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
                levelEditor.saveLevel();
            }
        });

        loadBtn = new TextButton("load", skin, "button");
        loadBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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


        menu.add(toolsBtn).height(30);
        menu.add(playerPropsBtn).height(30);
        menu.add(saveBtn).height(30);
        menu.add(loadBtn).height(30);
        menu.add(quitBtn).height(30);
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

        ButtonGroup createGroup = new ButtonGroup();
        createGroup.setUncheckLast(true);
        createGroup.setMaxCheckCount(1);
        createGroup.setMinCheckCount(1);

        for(OptionsMode mode : OptionsMode.values()) {
            if(mode.group == 0) {
                TextButton button = new TextButton(mode.label, skin, "toggle-button");
                createGroup.add(button);
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        levelEditor.setMode(mode);
                    }
                });
                menu.add(button).height(30);
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
                menu.add(button).height(30);
            }
        }

        addBackButtonAndFinalizeMenu(menu, MenuPage.MainMenu);
        return menu;
    }

    private Actor buildObjectMenu(List<LevelObject> objects) {
        Table parentMenu = new Table();
        parentMenu.setVisible(true);
        parentMenu.setFillParent(true);
        parentMenu.setOrigin(Align.topRight);
        parentMenu.align(Align.right);

        Table menu = new Table();
        menu.setVisible(true);
        menu.setFillParent(true);
        menu.setOrigin(Align.topRight);
        menu.align(Align.right);
//        menu.setX(stage.getWidth() - 500);
//        menu.setY(stage.getHeight() - 500);

        menu.setDebug(true);

        TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal(LevelEditor.EDITOR_ASSETS_FOLDER + "/question.png")));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.setUncheckLast(true);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);

        ScrollPane scrollPane = new ScrollPane(menu, skin);
        parentMenu.add(scrollPane);

        scrollPane.setDebug(true);

        int column = 1;
        for (LevelObject object : objects) {
            Table itemTable = new Table();
//            itemTable.setFillParent(true);
            TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegionDrawable(texture));
            SpriteDrawable downSprite = upDrawable.tint(Color.GREEN);
            ImageButton button = new ImageButton(upDrawable, downSprite, downSprite);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Ya clicked " + object.name());
                    levelEditor.dropObject(object.getClass());
                }
//
            });
            buttonGroup.add(button);
            itemTable.add(button);
            itemTable.row();
            Label label = new Label(object.name(), skin);
            itemTable.add(label).padBottom(20);
            menu.add(itemTable).padRight(20);
            if (column % 2 == 0) {
                menu.row();
            }
            column++;
        }

        stage.addActor(parentMenu);
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
        menu.add(backBtn).height(30);
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
