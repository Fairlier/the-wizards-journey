package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.thewizardsjourney.game.ui.widget.PlayerStatisticsWidget;

public class GameplayInfo {
    private boolean gameIsExit;
    private PlayerStatisticsWidget playerStatisticsWidget;
    private Button infoButton;

    public PlayerStatisticsWidget getPlayerStatisticsWidget() {
        return playerStatisticsWidget;
    }

    public void setPlayerStatisticsWidget(PlayerStatisticsWidget playerStatisticsWidget) {
        this.playerStatisticsWidget = playerStatisticsWidget;
    }

    public Button getInfoButton() {
        return infoButton;
    }

    public void setInfoButton(Button infoButton) {
        this.infoButton = infoButton;
    }

    public boolean isGameIsExit() {
        return gameIsExit;
    }

    public void setGameIsExit(boolean gameIsExit) {
        this.gameIsExit = gameIsExit;
    }
}
