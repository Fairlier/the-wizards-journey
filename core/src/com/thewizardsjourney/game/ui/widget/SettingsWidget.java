package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

public class SettingsWidget extends Table {
    private final Label titleLabel;
    private final Label musicLabel;
    private final Label soundLabel;
    private final Label languageLabel;
    private final Window settingsWindow;
    private final Slider musicSlider;
    private final Slider soundSlider;
    private final SelectBox<String> languageSelectBox;
    private final Button closeButton;

    public SettingsWidget(Skin skin) {
        super(skin);

        titleLabel = new Label("Settings", skin, "game-label");
        settingsWindow = new Window("", skin, "game-window");
        musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "game-slider");
        soundSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "game-slider");
        languageSelectBox = new SelectBox<>(skin, "game-select-box");
        closeButton = new Button(skin, "game-close-button");

        musicLabel = new Label("Music", skin, "big");
        soundLabel = new Label("Sound", skin, "big");
        languageLabel = new Label("Language", skin, "big");

        settingsWindow.setMovable(false);


        setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        languageSelectBox.setItems("English", "Русский");

        settingsWindow.add(musicLabel).pad(10);
        settingsWindow.add(musicSlider).width(200).pad(10).row();
        settingsWindow.add(soundLabel).pad(10);
        settingsWindow.add(soundSlider).width(200).pad(10).row();
        settingsWindow.add(languageLabel).pad(10);
        settingsWindow.add(languageSelectBox).width(200).pad(10).row();
        settingsWindow.pack();

        titleLabel.setAlignment(Align.center);

        top();
        add().expand().fill();
        add(titleLabel).height(200).center().padTop(20);
        add().expandX().fill();
        add(closeButton).top().right().padTop(20).padRight(20).row();
        add().expandX().fill();
        add(settingsWindow).center().padTop(20).padBottom(20);
        add().expandX().fill();

        pack();
        debug();
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
