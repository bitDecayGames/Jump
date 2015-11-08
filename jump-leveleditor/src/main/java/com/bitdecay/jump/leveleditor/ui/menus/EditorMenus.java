package com.bitdecay.jump.leveleditor.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.jump.gdx.level.EditorIdentifierObject;
import com.bitdecay.jump.gdx.level.RenderableLevelObject;
import com.bitdecay.jump.leveleditor.EditorHook;
import com.bitdecay.jump.leveleditor.render.LevelEditor;
import com.bitdecay.jump.leveleditor.ui.ModeType;
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
        rightMenus.put(MenuPage.TileMenu, buildTilesetMenu(hooker.getTilesets()));
        rightMenus.put(MenuPage.LevelObjectMenu, buildObjectMenu(hooker.getCustomObjects()));
        rightMenus.put(MenuPage.ThemeMenu, buildThemeMenu(hooker.getThemes()));
        topMenuTransition(MenuPage.MainMenu);
    }

    private Actor buildMainMenu() {
        Table menu = new Table();
        menu.setWidth(stage.getWidth());
        menu.align(Align.top);

        Table miscActionTable = new Table();

        for (OptionsMode mode : OptionsMode.values()) {
            if (mode.type == ModeType.ACTION) {
                buildImageButton(miscActionTable, mode, null);
            }
        }
        menu.add(miscActionTable).padRight(40);

        ButtonGroup toolGroup = new ButtonGroup();
        toolGroup.setUncheckLast(true);
        toolGroup.setMaxCheckCount(1);
        toolGroup.setMinCheckCount(1);

        Table toolsTable = new Table();
        for (OptionsMode mode : OptionsMode.values()) {
            if (mode.type == ModeType.MOUSE) {
                buildImageButton(toolsTable, mode, toolGroup);
            }
        }
        menu.add(toolsTable);

        menu.align(Align.topLeft);
        menu.setFillParent(true);
        menu.setVisible(false);
        stage.addActor(menu);
        return menu;
    }

    private void buildImageButton(Table menu, OptionsMode mode, ButtonGroup btnGroup) {
        TextureRegion testIcon = new TextureRegion(new Texture(Gdx.files.internal(mode.icon)));
        TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegionDrawable(new TextureRegion(testIcon)));
        SpriteDrawable downSprite = upDrawable.tint(Color.GREEN);
        ImageButton button;
        if (btnGroup != null) {
            button = new ImageButton(upDrawable, downSprite, downSprite);
            btnGroup.add(button);
        } else {
            button = new ImageButton(upDrawable, downSprite);
        }
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                levelEditor.setMode(mode);
                rightMenuTransition(mode.menu);
            }
        });
        // If we want to set the size explicitly, do it like this
        // menu.add(button).width(32).height(32);
        menu.add(button);
    }


    private Actor buildTilesetMenu(List<EditorIdentifierObject> tilesets) {
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

        for (EditorIdentifierObject object : tilesets) {
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

    private Actor buildThemeMenu(List<EditorIdentifierObject> themes) {
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

        for (EditorIdentifierObject object : themes) {
            Table itemTable = new Table();
            TextureRegionDrawable upDrawable = new TextureRegionDrawable(new TextureRegionDrawable(object.texture));
            SpriteDrawable downSprite = upDrawable.tint(Color.GREEN);
            ImageButton button = new ImageButton(upDrawable, downSprite);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    levelEditor.curLevelBuilder.setTheme(object.id);
                    levelEditor.curLevelBuilder.fireToListeners();
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
