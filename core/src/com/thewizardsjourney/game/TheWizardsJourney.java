package com.thewizardsjourney.game;

import static com.thewizardsjourney.game.constant.Asset.AssetPath;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.constant.Asset.AssetGroups.MapSettings;
import com.thewizardsjourney.game.constant.Asset.AssetGroups.MapList;
import com.thewizardsjourney.game.helper.GameData;
import com.thewizardsjourney.game.screen.GameScreen;
import com.thewizardsjourney.game.screen.LoadingScreen;
import com.thewizardsjourney.game.screen.MenuScreen;

public class TheWizardsJourney extends Game {
	private AssetsHandler assetsHandler;
	private GameData gameData;
	private Music music;
	private LoadingScreen loadingScreen;
	private MenuScreen menuScreen;
	private GameScreen gameScreen;

	@Override
	public void create() {
		gameData = new GameData();
		assetsHandler = new AssetsHandler();
		loadingScreen = new LoadingScreen(this);
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		setIntermediateScreen(null, MenuScreen.class);
	}

	@Override
	public void dispose() {
		assetsHandler.dispose();
	}

	public AssetsHandler getAssetHandler() {
		return assetsHandler;
	}

	public GameData getGameData() {
		return gameData;
	}

	public LoadingScreen getLoadingScreen() {
		return loadingScreen;
	}

	public MenuScreen getMenuScreen() {
		return menuScreen;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setIntermediateScreen(
			Class<? extends ScreenAdapter> previousScreen,
			Class<? extends ScreenAdapter> nextScreen) {
		loadingScreen.setScreens(previousScreen, nextScreen);
		setScreen(loadingScreen);
	}

	public void updateMusicVolume() {
		//
	}
}
