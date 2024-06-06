package com.thewizardsjourney.game.screen;

import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetGroups;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.SETTINGS_DEFAULT_LANGUAGE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.I18NBundle;
import com.thewizardsjourney.game.TheWizardsJourney;

import java.util.Locale;

public class LoadingScreen extends ScreenAdapter {
    private final TheWizardsJourney main;
    private Class<? extends ScreenAdapter> previousScreen;
    private Class<? extends ScreenAdapter> nextScreen;

    public LoadingScreen(TheWizardsJourney main) {
        this.main = main;
        main.getAssetHandler().parseGroupsFromFile(AssetPath.ASSETS);
        main.getGameInfo().setMapGroupNames(main.getAssetHandler().parseMapsFromDirectory(AssetPath.Map.PARENT_DIRECTORY));
        main.getGameInfo().setPlayerGroupNames(main.getAssetHandler().parsePlayersFromDirectory(AssetPath.Player.PARENT_DIRECTORY));
        main.getAssetHandler().loadGroup(AssetGroups.Default.GROUP_NAME);
        main.getAssetHandler().loadGroup(AssetGroups.LoadingScreen.GROUP_NAME);
        for (String mapGroupName : main.getGameInfo().getMapGroupNames()) {
            main.getAssetHandler().loadGroup(mapGroupName);
        }
        for (String playerGroupName : main.getGameInfo().getPlayerGroupNames()) {
            main.getAssetHandler().loadGroup(playerGroupName);
        }
        main.getAssetHandler().finishLoading();
        main.getGameInfo().setSelectedMapGroupName(main.getGameInfo().getMenuMapGroupName());

        I18NBundle i18NBundle;
        String language = main.getAssetHandler().getLanguage();
        i18NBundle = main.getAssetHandler().get(AssetGroups.Default.GROUP_NAME, language);
        main.getGameInfo().setSelectedLanguage(language);
        main.getGameInfo().setI18NBundle(i18NBundle);
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
                for (String mapGroupName : main.getGameInfo().getMapGroupNames()) {
                    if (mapGroupName.equals(main.getGameInfo().getSelectedMapGroupName())) {
                        continue;
                    }
                    main.getAssetHandler().unloadGroup(mapGroupName);
                }
                for (String playerGroupName : main.getGameInfo().getPlayerGroupNames()) {
                    if (playerGroupName.equals(main.getGameInfo().getSelectedPlayerGroupName())) {
                        continue;
                    }
                    main.getAssetHandler().unloadGroup(playerGroupName);
                }

                main.getAssetHandler().loadGroup(AssetGroups.GameScreen.GROUP_NAME);
            } else if (previousScreen.equals(GameScreen.class) && nextScreen.equals(MenuScreen.class)) {
                main.getAssetHandler().unloadGroup(AssetGroups.GameScreen.GROUP_NAME);

                for (String mapGroupName : main.getGameInfo().getMapGroupNames()) {
                    if (mapGroupName.equals(main.getGameInfo().getSelectedMapGroupName())) {
                        continue;
                    }
                    main.getAssetHandler().loadGroup(mapGroupName);
                }
                for (String playerGroupName : main.getGameInfo().getPlayerGroupNames()) {
                    if (playerGroupName.equals(main.getGameInfo().getSelectedPlayerGroupName())) {
                        continue;
                    }
                    main.getAssetHandler().loadGroup(playerGroupName);
                }
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
