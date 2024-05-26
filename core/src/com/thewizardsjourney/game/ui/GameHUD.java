package com.thewizardsjourney.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.thewizardsjourney.game.helper.GameInfo;
import com.thewizardsjourney.game.helper.GameplayInfo;
import com.thewizardsjourney.game.ui.widget.GameExitWidget;
import com.thewizardsjourney.game.ui.widget.GameOverWidget;
import com.thewizardsjourney.game.ui.widget.InformationWidget;
import com.thewizardsjourney.game.ui.widget.PauseWidget;
import com.thewizardsjourney.game.ui.widget.PlayerStatisticsWidget;

public class GameHUD extends Table {
    private final Touchpad touchpad;
    private final Button pauseButton;
    private final Button jumpButton;
    private final Button castButton;
    private final Button informationButton;
    private final Button switchButton;
    private final PlayerStatisticsWidget playerStatisticsWidget;
    private final PauseWidget pauseWidget;
    private final InformationWidget informationWidget;
    private final GameExitWidget gameExitWidget;
    private final GameOverWidget gameOverWidget;
    private boolean jumpButtonVisible;
    private final GameplayInfo gameplayInfo;

    public GameHUD(Skin skin, GameplayInfo gameplayInfo) {
        super(skin);
        this.gameplayInfo = gameplayInfo;

        pauseButton = new Button(skin, "game-pause-button");
        jumpButton = new Button(skin, "game-jump-button");
        castButton = new Button(skin, "game-cast-button");
        informationButton = new Button(skin, "game-info-button");
        switchButton = new Button(skin, "game-resume-button");
        touchpad = new Touchpad(5.0f, skin, "game-touchpad");
        playerStatisticsWidget = new PlayerStatisticsWidget(skin);
        pauseWidget = new PauseWidget(skin);
        informationWidget = new InformationWidget(skin);
        gameExitWidget = new GameExitWidget(skin);
        gameOverWidget = new GameOverWidget(skin);

        informationButton.setVisible(false);
        castButton.setVisible(false);
        jumpButtonVisible = true;

        gameplayInfo.setPlayerStatisticsWidget(playerStatisticsWidget);
        gameplayInfo.setInformationButton(informationButton);
        gameplayInfo.setInformationWidget(informationWidget);
        gameplayInfo.setGameHUD(this);

        playerStatisticsWidget.setVisible(true);
        pauseWidget.setVisible(false);
        informationWidget.setVisible(false);
        gameExitWidget.setVisible(false);
        gameOverWidget.setVisible(false);
        addActor(playerStatisticsWidget);
        addActor(pauseWidget);
        addActor(informationWidget);
        addActor(gameExitWidget);
        addActor(gameOverWidget);

        setupUI();
        setFillParent(true);
        buttonProcessing();
    }

    private void setupUI() {
        add(playerStatisticsWidget).left();
        add().expand().fill();
        add(pauseButton).expand().align(Align.topRight).pad(20);
        row();
        add(touchpad).size(175, 175).bottom().left().pad(100);
        add(castButton).expand().bottom().pad(10);
        Table bottomRight = new Table();
        bottomRight.add(informationButton);
        bottomRight.add(castButton).padLeft(10).padBottom(10).padRight(20);
        bottomRight.row();
        bottomRight.add(switchButton).padTop(10);
        bottomRight.add(jumpButton).padLeft(10).padRight(100).padTop(10);
        add(bottomRight).bottom().right().padBottom(100);
    }

    private void buttonProcessing() {
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                touchpad.setVisible(false);
                jumpButton.setVisible(false);
                switchButton.setVisible(false);
                castButton.setVisible(false);
                informationButton.setVisible(false);
                pauseButton.setVisible(false);
                playerStatisticsWidget.setVisible(false);
                pauseWidget.setVisible(true);
            }
        });

        pauseWidget.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseWidget.setVisible(false);
                touchpad.setVisible(true);
                switchButton.setVisible(true);
                if (jumpButtonVisible) {
                    jumpButton.setVisible(true);
                    castButton.setVisible(false);
                } else {
                    castButton.setVisible(true);
                    jumpButton.setVisible(false);
                }
                pauseButton.setVisible(true);
                playerStatisticsWidget.setVisible(true);
            }
        });

        informationButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                touchpad.setVisible(false);
                jumpButton.setVisible(false);
                switchButton.setVisible(false);
                castButton.setVisible(false);
                informationButton.setVisible(false);
                pauseButton.setVisible(false);
                playerStatisticsWidget.setVisible(false);
                if (gameplayInfo.isGameIsExit()) {
                    gameExitWidget.setVisible(true);
                } else {
                    informationWidget.update();
                    informationWidget.setVisible(true);
                }
            }
        });

        informationWidget.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                informationWidget.setVisible(false);
                touchpad.setVisible(true);
                switchButton.setVisible(true);
                if (jumpButtonVisible) {
                    jumpButton.setVisible(true);
                    castButton.setVisible(false);
                } else {
                    castButton.setVisible(true);
                    jumpButton.setVisible(false);
                }
                pauseButton.setVisible(true);
                playerStatisticsWidget.setVisible(true);
            }
        });

        gameExitWidget.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameExitWidget.setVisible(false);
                touchpad.setVisible(true);
                switchButton.setVisible(true);
                if (jumpButtonVisible) {
                    jumpButton.setVisible(true);
                    castButton.setVisible(false);
                } else {
                    castButton.setVisible(true);
                    jumpButton.setVisible(false);
                }
                pauseButton.setVisible(true);
                playerStatisticsWidget.setVisible(true);
            }
        });
    }

    public void gameOver() {
        touchpad.setVisible(false);
        jumpButton.setVisible(false);
        switchButton.setVisible(false);
        castButton.setVisible(false);
        informationButton.setVisible(false);
        pauseButton.setVisible(false);
        playerStatisticsWidget.setVisible(false);
        pauseWidget.setVisible(false);
        informationWidget.setVisible(false);
        gameExitWidget.setVisible(false);
        gameOverWidget.setVisible(true);
    }

    public Touchpad getTouchpad() {
        return touchpad;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getJumpButton() {
        return jumpButton;
    }

    public Button getCastButton() {
        return castButton;
    }

    public Button getInformationButton() {
        return informationButton;
    }

    public Button getSwitchButton() {
        return switchButton;
    }

    public PlayerStatisticsWidget getPlayerStatisticsWidget() {
        return playerStatisticsWidget;
    }

    public PauseWidget getPauseWidget() {
        return pauseWidget;
    }

    public boolean isJumpButtonVisible() {
        return jumpButtonVisible;
    }

    public void setJumpButtonVisible(boolean jumpButtonVisible) {
        this.jumpButtonVisible = jumpButtonVisible;
    }

    public GameExitWidget getGameExitWidget() {
        return gameExitWidget;
    }

    public GameOverWidget getGameOverWidget() {
        return gameOverWidget;
    }
}
