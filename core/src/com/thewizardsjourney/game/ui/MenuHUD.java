package com.thewizardsjourney.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.thewizardsjourney.game.helper.GameInfo;
import com.thewizardsjourney.game.ui.widget.SelectLevelWidget;
import com.thewizardsjourney.game.ui.widget.SettingsWidget;

public class MenuHUD extends Table {
    private final Label titleLabel;
    private final Button playButton;
    private final Button shopButton;
    private final Button settingsButton;
    private final Button achievementsButton;
    private final SettingsWidget settingsWidget;
    private final SelectLevelWidget selectLevelWidget;

    public MenuHUD(Skin skin, GameInfo gameInfo) {
        super(skin);

        titleLabel = new Label("МЕНЮ", skin, "game-label");
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
        titleLabel.setAlignment(Align.center);

        add(achievementsButton).left().top().pad(20);
        add().expand().fill();
        add(titleLabel).height(200).top().padTop(20);
        add().expand().fill();
        add(settingsButton).right().top().pad(20);
        row();
        add(shopButton).left().bottom().pad(20);
        add().expand().fill();
        add().expand().fill();
        add().expand().fill();
        add(playButton).right().bottom().pad(20);
        pack();
    }

    private void buttonProcessing() {
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                titleLabel.setVisible(false);
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
                titleLabel.setVisible(false);
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
                titleLabel.setVisible(true);
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
                titleLabel.setVisible(true);
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
