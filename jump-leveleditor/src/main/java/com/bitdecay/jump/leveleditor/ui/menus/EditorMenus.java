package com.bitdecay.jump.leveleditor.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.jump.gdx.level.EditorTileset;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.ui.OptionsMode;

import javax.swing.*;
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

    private Map<MenuPage, Actor> topMenus = new HashMap<>();
    private Map<MenuPage, Actor> rightMenus = new HashMap<>();
    private Actor currentTopMenu;
    private Actor currentRightMenu;

    public EditorMenus(LevelEditor levelEditor, EditorHook hooker) {
        this.levelEditor = levelEditor;
        TextureAtlas menuAtlas = new TextureAtlas(Gdx.files.internal("skins/ui.atlas"));
        skin = new Skin(Gdx.files.internal("skins/menu-skin.json"), menuAtlas);
        stage = new Stage();
        topMenus.put(MenuPage.MainMenu, buildMainMenu());
        topMenus.put(MenuPage.CreateMenu, buildCreateMenu());
        topMenus.put(MenuPage.PlayerMenu, buildPlayerMenu());
        rightMenus.put(MenuPage.TileMenu, buildTilesetMenu(hooker.getTilesets()));
        rightMenus.put(MenuPage.LevelObjectMenu, buildObjectMenu(hooker.getCustomObjects()));
        topMenuTransition(MenuPage.MainMenu);
    }

    private Actor buildMainMenu() {
        Table menu = new Table();
        menu.setWidth(stage.getWidth());
        menu.align(Align.top);

        TextButton toolsBtn;
        TextButton playerPropsBtn;
        TextButton quitBtn;

        toolsBtn = new TextButton("tools", skin, "button");
        toolsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                topMenuTransition(MenuPage.CreateMenu);
                levelEditor.setMode(OptionsMode.SELECT);
            }
        });
        menu.add(toolsBtn).height(30);

        playerPropsBtn = new TextButton("player properties", skin, "button");
        playerPropsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                topMenuTransition(MenuPage.PlayerMenu);
            }
        });
        menu.add(playerPropsBtn).height(30);

        for(OptionsMode mode : OptionsMode.values()) {
            if(mode.group == -1) {
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

        quitBtn = new TextButton("exit", skin, "button");
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
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
                        rightMenuTransition(null);
                    }
                });
                menu.add(button).height(30);
            }
        }

        for(OptionsMode mode : OptionsMode.values()) {
            if(mode.group == 1) {
                TextButton button = new TextButton(mode.label, skin, "toggle-button");
                createGroup.add(button);
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        levelEditor.setMode(mode);
                        rightMenuTransition(MenuPage.TileMenu);
                    }
                });
                menu.add(button).height(30);
            }
        }

        for(OptionsMode mode : OptionsMode.values()) {
            if(mode.group == 2) {
                TextButton button = new TextButton(mode.label, skin, "toggle-button");
                createGroup.add(button);
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        levelEditor.setMode(mode);
                        rightMenuTransition(MenuPage.LevelObjectMenu);
                    }
                });
                menu.add(button).height(30);
            }
        }

        addBackButtonAndFinalizeMenu(menu, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                topMenuTransition(MenuPage.MainMenu);
                rightMenuTransition(null);
                levelEditor.setMode(OptionsMode.SELECT);
                createGroup.setChecked(OptionsMode.SELECT.label);
            }
        });
        return menu;
    }

    private Actor buildPlayerMenu() {
        Table menu = new Table();
        menu.setWidth(stage.getWidth());
        menu.align(Align.top);

        for(OptionsMode mode : OptionsMode.values()) {
            if(mode.group == 3) {
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

        addBackButtonAndFinalizeMenu(menu, new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        topMenuTransition(MenuPage.MainMenu);
                        rightMenuTransition(null);
                        levelEditor.setMode(OptionsMode.SELECT);
                    }
                });


        return menu;
    }

    private Actor buildTilesetMenu(List<EditorTileset> tilesets) {
        Table parentMenu = new Table();
        parentMenu.setVisible(false);
        parentMenu.setFillParent(true);
        parentMenu.setOrigin(Align.topRight);
        parentMenu.align(Align.right);

        Table menu = new Table();

        ScrollPane scrollPane = new ScrollPane(menu, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        parentMenu.add(scrollPane);

        for (EditorTileset object : tilesets) {
            Table itemTable = new Table();
            TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegionDrawable(object.texture));
            SpriteDrawable downSprite = upDrawable.tint(Color.GREEN);
            ImageButton button = new ImageButton(upDrawable, downSprite);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    levelEditor.setMaterial(object.id);
                }
            });
            itemTable.add(button);
            itemTable.row();
            Label label = new Label(object.displayName, skin);
            itemTable.add(label).padBottom(20);

            menu.add(itemTable).padRight(20);
            menu.row();
        }
        menu.padBottom(-20);

        stage.addActor(parentMenu);
        return parentMenu;
    }

    private Actor buildObjectMenu(List<RenderableLevelObject> objects) {
        Table parentMenu = new Table();
        parentMenu.setVisible(false);
        parentMenu.setFillParent(true);
        parentMenu.setOrigin(Align.topRight);
        parentMenu.align(Align.right);

        Table menu = new Table();

        ScrollPane scrollPane = new ScrollPane(menu, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        parentMenu.add(scrollPane);

        int column = 1;
        for (RenderableLevelObject object : objects) {
            Table itemTable = new Table();
            TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegionDrawable(object.texture()));
            SpriteDrawable downSprite = upDrawable.tint(Color.GREEN);
            ImageButton button = new ImageButton(upDrawable, downSprite);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    levelEditor.dropObject(object.getClass());
                }
            });
            itemTable.add(button);
            itemTable.row();
            Label label = new Label(object.name(), skin);
            itemTable.add(label).padBottom(20);

            if (column % 2 == 0) {
                menu.add(itemTable).padRight(20);
                menu.row();
            } else {
                menu.add(itemTable).padLeft(15).padRight(20);
            }
            column++;
        }
        menu.padBottom(-20);

        stage.addActor(parentMenu);
        return parentMenu;
    }

    private void addBackButtonAndFinalizeMenu(Table menu, ClickListener backAction) {
        TextButton backBtn;
        backBtn = new TextButton("back", skin, "button");
        backBtn.addListener(backAction);
        menu.add(backBtn).height(30);
        menu.align(Align.topLeft);
        menu.setFillParent(true);
        menu.setVisible(false);
        stage.addActor(menu);
    }

    private void topMenuTransition(MenuPage to) {
        if (currentTopMenu != null) {
            currentTopMenu.setVisible(false);
        }
        if (topMenus.containsKey(to)) {
            topMenus.get(to).setVisible(true);
            currentTopMenu = topMenus.get(to);
        }
    }

    private void rightMenuTransition(MenuPage to) {
        if (currentRightMenu != null) {
            currentRightMenu.setVisible(false);
        }
        if (rightMenus.containsKey(to)) {
            rightMenus.get(to).setVisible(true);
            currentRightMenu = rightMenus.get(to);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }
}
