package com.thewizardsjourney.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuHUD extends Table {
    private final TextButton playButton;
    private final TextButton shopButton;
    private final TextButton settingsButton;
    private final TextButton achievementsButton;

    public MenuHUD(Skin skin) {
        super(skin);

        achievementsButton = new TextButton("Achievements", skin);
        settingsButton = new TextButton("Settings", skin);
        shopButton = new TextButton("SHOP", skin);
        playButton = new TextButton("PLAY", skin);

        setupUI();
        setFillParent(true);
    }

    private void setupUI() {
        add(achievementsButton).left().top().pad(20.0f);
        add().expand().top();
        add(settingsButton).right().top().pad(20.0f);
        row();
        add(shopButton).left().bottom().pad(20.0f);
        add().expand().bottom();
        add(playButton).right().bottom().pad(20.0f);
    }


    public TextButton getPlayButton() {
        return playButton;
    }

    public TextButton getShopButton() {
        return shopButton;
    }

    public TextButton getSettingsButton() {
        return settingsButton;
    }

    public TextButton getAchievementsButton() {
        return achievementsButton;
    }
}
