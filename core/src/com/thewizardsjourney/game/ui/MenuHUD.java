package com.thewizardsjourney.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.thewizardsjourney.game.helper.GameInfo;
import com.thewizardsjourney.game.ui.widget.SelectLevelWidget;
import com.thewizardsjourney.game.ui.widget.SettingsWidget;

public class MenuHUD extends Table {
    private final Button playButton;
    private final Button shopButton;
    private final Button settingsButton;
    private final Button achievementsButton;
    private final SettingsWidget settingsWidget;
    private final SelectLevelWidget selectLevelWidget;

    public MenuHUD(Skin skin, GameInfo gameInfo) {
        super(skin);

        achievementsButton = new Button(skin, "game-achievement-button");
        settingsButton = new Button(skin, "game-settings-button");
        shopButton = new Button(skin, "game-shop-button");
        playButton = new Button(skin, "game-play-button");
        settingsWidget = new SettingsWidget(skin);
        selectLevelWidget = new SelectLevelWidget(skin, gameInfo);

        settingsWidget.setVisible(false);
        selectLevelWidget.setVisible(false);

        addActor(settingsWidget);
        addActor(selectLevelWidget);

        setFillParent(true);
        setupUI();
        buttonProcessing();
    }

    private void setupUI() {
        add(achievementsButton).left().top().pad(20);
        add().expand().top();
        add(settingsButton).right().top().pad(20);
        row();
        add(shopButton).left().bottom().pad(20);
        add().expand().bottom();
        add(playButton).right().bottom().pad(20);
    }

    private void buttonProcessing() {
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                achievementsButton.setVisible(false);
                settingsButton.setVisible(false);
                shopButton.setVisible(false);
                playButton.setVisible(false);
                selectLevelWidget.setVisible(true);
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                achievementsButton.setVisible(false);
                settingsButton.setVisible(false);
                shopButton.setVisible(false);
                playButton.setVisible(false);
                settingsWidget.setVisible(true);
            }
        });

        settingsWidget.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settingsWidget.setVisible(false);
                achievementsButton.setVisible(true);
                settingsButton.setVisible(true);
                shopButton.setVisible(true);
                playButton.setVisible(true);
            }
        });

        selectLevelWidget.getCloseButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectLevelWidget.setVisible(false);
                achievementsButton.setVisible(true);
                settingsButton.setVisible(true);
                shopButton.setVisible(true);
                playButton.setVisible(true);
            }
        });
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getShopButton() {
        return shopButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getAchievementsButton() {
        return achievementsButton;
    }

    public SettingsWidget getSettingsWidget() {
        return settingsWidget;
    }

    public SelectLevelWidget getSelectLevelWidget() {
        return selectLevelWidget;
    }
}
