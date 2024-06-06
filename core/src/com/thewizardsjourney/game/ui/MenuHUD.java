package com.thewizardsjourney.game.ui;

import static com.thewizardsjourney.game.constant.AssetConstants.AssetGroups.Default.ENGLISH_LANGUAGE;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetGroups.Default.RUSSIAN_LANGUAGE;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.constant.AssetConstants;
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
    private final AssetsHandler assetsHandler;
    private final GameInfo gameInfo;

    public MenuHUD(Skin skin, AssetsHandler assetsHandler, GameInfo gameInfo) {
        super(skin);
        this.assetsHandler = assetsHandler;
        this.gameInfo = gameInfo;

        titleLabel = new Label(gameInfo.getI18NBundle().get("menu.title"), skin, "game-label");
        achievementsButton = new Button(skin, "game-achievement-button");
        settingsButton = new Button(skin, "game-settings-button");
        shopButton = new Button(skin, "game-shop-button");
        playButton = new Button(skin, "game-play-button");
        settingsWidget = new SettingsWidget(skin, gameInfo);
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

        settingsWidget.getLanguageSelectBox().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedLanguage = settingsWidget.getLanguageSelectBox().getSelected();
                if (selectedLanguage.equals(gameInfo.getSelectedLanguage())) {
                    return;
                }
                if (selectedLanguage.equals("Русский")) {
                    selectedLanguage = RUSSIAN_LANGUAGE;
                } else if (selectedLanguage.equals("English")) {
                    selectedLanguage = ENGLISH_LANGUAGE;
                }
                gameInfo.setSelectedLanguage(selectedLanguage);
                assetsHandler.setLanguage(selectedLanguage);
                gameInfo.setI18NBundle(assetsHandler.get(AssetConstants.AssetGroups.Default.GROUP_NAME, selectedLanguage));
                updateLanguage();

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

    public void updateLanguage() {
        titleLabel.setText(gameInfo.getI18NBundle().get("menu.title"));
        settingsWidget.updateLanguage();
        settingsWidget.updateLanguageSelectBox();
        selectLevelWidget.updateLanguage();
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
