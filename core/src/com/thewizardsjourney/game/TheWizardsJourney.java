package com.thewizardsjourney.game;

import com.badlogic.gdx.Game;
import com.thewizardsjourney.game.screen.GameScreen;

public class TheWizardsJourney extends Game {
	private static final String TAG = "TheWizardsJourney";

	@Override
	public void create() {
 		setScreen(new GameScreen(this));
	}
}
