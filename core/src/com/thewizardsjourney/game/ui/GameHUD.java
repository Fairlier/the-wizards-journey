package com.thewizardsjourney.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.thewizardsjourney.game.helper.GameplayInfo;
import com.thewizardsjourney.game.ui.widget.PauseWidget;
import com.thewizardsjourney.game.ui.widget.PlayerStatisticsWidget;

public class GameHUD extends Table {
    private final Touchpad touchpad;
    private final Button pauseButton;
    private final Button jumpButton;
    private final Button castButton;
    private final Button infoButton;
    private final Button switchButton;
    private final PlayerStatisticsWidget playerStatisticsWidget;
    private final PauseWidget pauseWidget;
    private boolean jumpButtonVisible;

    public GameHUD(Skin skin, GameplayInfo gameplayInfo) {
        super(skin);

        pauseButton = new Button(skin, "game-pause-button");
        jumpButton = new Button(skin, "game-jump-button");
        castButton = new Button(skin, "game-cast-button");
        infoButton = new Button(skin, "game-info-button");
        switchButton = new Button(skin, "game-resume-button");
        touchpad = new Touchpad(5.0f, skin, "game-touchpad");
        playerStatisticsWidget = new PlayerStatisticsWidget(skin);
        pauseWidget = new PauseWidget(skin);
        gameplayInfo.setPlayerStatisticsWidget(playerStatisticsWidget);

        playerStatisticsWidget.setVisible(false);
        pauseWidget.setVisible(false);
        addActor(pauseWidget);

        setupUI();
        setFillParent(true);
        buttonProcessing();
    }

    private void setupUI() {
        add().expand().align(Align.topLeft).pad(10);
        add().left().expand().fill();
        add(pauseButton).expand().align(Align.topRight).pad(20);
        row();
        add(touchpad).size(175, 175).bottom().left().pad(100);
        add(castButton).expand().bottom().pad(10);
        Table bottomRight = new Table();
        bottomRight.add(infoButton);
        bottomRight.add(castButton).padLeft(10).padBottom(10).padRight(20);
        bottomRight.row();
        bottomRight.add(switchButton).padTop(10);
        bottomRight.add(jumpButton).padLeft(10).padRight(100).padTop(10);
        add(bottomRight).bottom().right().padBottom(100);

        infoButton.setVisible(false);
        castButton.setVisible(false);
        jumpButtonVisible = true;
    }

    private void buttonProcessing() {
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                touchpad.setVisible(false);
                jumpButton.setVisible(false);
                switchButton.setVisible(false);
                castButton.setVisible(false);
                infoButton.setVisible(false);
                pauseButton.setVisible(false);
                pauseWidget.setVisible(true);
            }
        });

        pauseWidget.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseWidget.setVisible(false);
                touchpad.setVisible(true);
                jumpButton.setVisible(true);
                switchButton.setVisible(true);
                castButton.setVisible(true);
                pauseButton.setVisible(true);
            }
        });
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

    public Button getInfoButton() {
        return infoButton;
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
}
