package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.thewizardsjourney.game.helper.GameInfo;

public class SettingsWidget extends Table {
    private final Label titleLabel;
    private final Label musicLabel;
    private final Label soundsLabel;
    private final Label languageLabel;
    private final Window settingsWindow;
    private final Slider musicSlider;
    private final Slider soundSlider;
    private final SelectBox<String> languageSelectBox;
    private final Button closeButton;
    private final GameInfo gameInfo;

    public SettingsWidget(Skin skin, GameInfo gameInfo) {
        super(skin);
        this.gameInfo = gameInfo;

        titleLabel = new Label(gameInfo.getI18NBundle().get("settings.title"), skin, "game-label");
        settingsWindow = new Window("", skin, "game-window");

        musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "game-slider");
        soundSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "game-slider");

        languageSelectBox = new SelectBox<>(skin, "game-select-box");
        languageSelectBox.setItems("Русский", "English");

        closeButton = new Button(skin, "game-close-button");

        musicLabel = new Label(gameInfo.getI18NBundle().get("settings.musicLabel"), skin, "game-label-without-window");
        soundsLabel = new Label(gameInfo.getI18NBundle().get("settings.soundsLabel"), skin, "game-label-without-window");
        languageLabel = new Label(gameInfo.getI18NBundle().get("settings.languageLabel"), skin, "game-label-without-window");

        setFillParent(true);
        setupUI();
        updateLanguageSelectBox();
    }

    private void setupUI() {
        settingsWindow.setMovable(false);
        settingsWindow.add(musicLabel).pad(10);
        settingsWindow.add(musicSlider).width(200).pad(10).row();
        settingsWindow.add(soundsLabel).pad(10);
        settingsWindow.add(soundSlider).width(200).pad(10).row();
        settingsWindow.add(languageLabel).pad(10);
        settingsWindow.add(languageSelectBox).width(200).pad(10).row();
        settingsWindow.pack();

        titleLabel.setAlignment(Align.center);

        add().expand().fill();
        add(titleLabel).height(200).top().padTop(20);
        add().expandX().fill();
        add(closeButton).top().right().padTop(20).padRight(20).row();
        add().expandX().fill();
        add(settingsWindow).center().padTop(20).padBottom(20);
        add().expandX().fill();
        pack();
    }

    public void updateLanguage() {
        titleLabel.setText(gameInfo.getI18NBundle().get("settings.title"));
        musicLabel.setText(gameInfo.getI18NBundle().get("settings.musicLabel"));
        soundsLabel.setText(gameInfo.getI18NBundle().get("settings.soundsLabel"));
        languageLabel.setText(gameInfo.getI18NBundle().get("settings.languageLabel"));
    }

    public void updateLanguageSelectBox() {
        if (gameInfo.getSelectedLanguage().equals("russian_language")) {
            languageSelectBox.setSelectedIndex(0);
        } else if (gameInfo.getSelectedLanguage().equals("english_language")) {
            languageSelectBox.setSelectedIndex(1);
        }
    }

    public void updateMusicSlider(float value) {
        musicSlider.setValue(value);
    }

    public void updateSoundsSlider(float value) {
        soundSlider.setValue(value);
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Window getSettingsWindow() {
        return settingsWindow;
    }

    public Slider getMusicSlider() {
        return musicSlider;
    }

    public Slider getSoundSlider() {
        return soundSlider;
    }

    public SelectBox<String> getLanguageSelectBox() {
        return languageSelectBox;
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
