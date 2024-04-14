package com.thewizardsjourney.game.screen;

import static com.thewizardsjourney.game.constant.Asset.AssetPath;
import static com.thewizardsjourney.game.constant.Asset.AssetGroups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.thewizardsjourney.game.TheWizardsJourney;
import com.thewizardsjourney.game.constant.Asset;

public class LoadingScreen extends ScreenAdapter {
    private final TheWizardsJourney main;
    private Class<? extends ScreenAdapter> previousScreen;
    private Class<? extends ScreenAdapter> nextScreen;

    public LoadingScreen(TheWizardsJourney main) {
        this.main = main;
        main.getAssetHandler().loadGroupsFromFile(AssetPath.ASSETS);
        main.getAssetHandler().loadGroup(AssetGroups.LoadingScreen.GROUP_NAME);
        main.getAssetHandler().loadMapFromDirectory(
                AssetPath.GAME_MAPS,
                AssetGroups.MapList.GROUP_NAME,
                AssetPath.MAP_FORMAT,
                AssetPath.MAP_TYPE);
        main.getAssetHandler().finishLoading();
        main.getGameData().setMapsName(
                main.getAssetHandler().getSortedMapNames(Asset.AssetGroups.MapList.GROUP_NAME)
        );
    }

    @Override
    public void show() {
        if (previousScreen == null && nextScreen != null ) {
            if (nextScreen.equals(MenuScreen.class)) {
                main.getAssetHandler().loadGroup(AssetGroups.MenuScreen.GROUP_NAME);
            }
        } else if (previousScreen != null && nextScreen != null) {
            if (previousScreen.equals(MenuScreen.class) && nextScreen.equals(GameScreen.class)) {
                main.getAssetHandler().unloadGroup(AssetGroups.MenuScreen.GROUP_NAME);
                main.getAssetHandler().loadGroup(AssetGroups.GameScreen.GROUP_NAME);
                main.getAssetHandler().loadGroup(AssetGroups.MapSettings.GROUP_NAME);
                main.getAssetHandler().loadGroup(AssetGroups.MapList.GROUP_NAME);
            } else if (previousScreen.equals(GameScreen.class) && nextScreen.equals(MenuScreen.class)) {
                main.getAssetHandler().unloadGroup(AssetGroups.GameScreen.GROUP_NAME);
                main.getAssetHandler().unloadGroup(AssetGroups.MapSettings.GROUP_NAME);
                main.getAssetHandler().unloadGroup(AssetGroups.MapList.GROUP_NAME);
                main.getAssetHandler().loadGroup(AssetGroups.MenuScreen.GROUP_NAME);
            }
        }
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (main.getAssetHandler().update()) {
            if (nextScreen != null) {
                if (nextScreen.equals(MenuScreen.class)) {
                    main.setScreen(main.getMenuScreen());
                } else if (nextScreen.equals(GameScreen.class)) {
                    main.setScreen(main.getGameScreen());
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void setScreens(
            Class<? extends ScreenAdapter> previousScreen,
            Class<? extends ScreenAdapter> nextScreen) {
        this.previousScreen = previousScreen;
        this.nextScreen = nextScreen;
    }
}
