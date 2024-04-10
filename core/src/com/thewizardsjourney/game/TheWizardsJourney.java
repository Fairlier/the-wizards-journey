package com.thewizardsjourney.game;

import static com.thewizardsjourney.game.constant.Asset.ASSETS_PATH;
import static com.thewizardsjourney.game.constant.Asset.AssetGroup.MapSettings;

import com.badlogic.gdx.Game;
import com.thewizardsjourney.game.asset.AssetHandler;
import com.thewizardsjourney.game.constant.Asset;
import com.thewizardsjourney.game.screen.GameScreen;

public class TheWizardsJourney extends Game {
	AssetHandler assetHandler;

	@Override
	public void create() {
		assetHandler = new AssetHandler(ASSETS_PATH);
		assetHandler.loadGroup(MapSettings.GROUP_NAME);
		assetHandler.finishLoading();
 		setScreen(new GameScreen(this));
	}

	public AssetHandler getAssetHandler() {
		return assetHandler;
	}
}
