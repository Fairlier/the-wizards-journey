package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.thewizardsjourney.game.ui.GameHUD;
import com.thewizardsjourney.game.ui.widget.GameOverWidget;
import com.thewizardsjourney.game.ui.widget.InformationWidget;
import com.thewizardsjourney.game.ui.widget.PlayerStatisticsWidget;

public class GameplayInfo {
    private boolean gameIsExit;
    private PlayerStatisticsWidget playerStatisticsWidget;
    private Button informationButton;
    private InformationWidget informationWidget;
    private GameHUD gameHUD;

    public PlayerStatisticsWidget getPlayerStatisticsWidget() {
        return playerStatisticsWidget;
    }

    public void setPlayerStatisticsWidget(PlayerStatisticsWidget playerStatisticsWidget) {
        this.playerStatisticsWidget = playerStatisticsWidget;
    }

    public Button getInformationButton() {
        return informationButton;
    }

    public void setInformationButton(Button informationButton) {
        this.informationButton = informationButton;
    }

    public boolean isGameIsExit() {
        return gameIsExit;
    }

    public void setGameIsExit(boolean gameIsExit) {
        this.gameIsExit = gameIsExit;
    }

    public InformationWidget getInformationWidget() {
        return informationWidget;
    }

    public void setInformationWidget(InformationWidget informationWidget) {
        this.informationWidget = informationWidget;
    }

    public GameHUD getGameHUD() {
        return gameHUD;
    }

    public void setGameHUD(GameHUD gameHUD) {
        this.gameHUD = gameHUD;
    }
}
