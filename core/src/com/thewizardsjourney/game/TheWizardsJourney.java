package com.thewizardsjourney.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.helper.GameInfo;
import com.thewizardsjourney.game.screen.GameScreen;
import com.thewizardsjourney.game.screen.LoadingScreen;
import com.thewizardsjourney.game.screen.MenuScreen;

public class TheWizardsJourney extends Game {
	private AssetsHandler assetsHandler;
	private GameInfo gameInfo;
	private Music music;
	private LoadingScreen loadingScreen;
	private MenuScreen menuScreen;
	private GameScreen gameScreen;

	@Override
	public void create() {
		gameInfo = new GameInfo();
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

	public GameInfo getGameInfo() {
		return gameInfo;
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
