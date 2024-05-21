package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class PlayerStatisticsWidget extends WidgetGroup {
    private final Image background;
    private final Image healthBar;
    private final Image energyBar;
    private float currentHealth;
    private float maxHealth;
    private float currentEnergy;
    private float maxEnergy;

    public PlayerStatisticsWidget(Skin skin) {
//        background = new Image(skin, "game-player-bar");
//        healthBar = new Image(skin, "game-health-bar");
//        energyBar = new Image(skin, "game-energy-bar");

        Drawable backgroundDrawable = skin.getDrawable("game-player-bar");
        Drawable healthDrawable = skin.getDrawable("game-health-bar");
        Drawable energyDrawable = skin.getDrawable("game-energy-bar");

        background = new Image(backgroundDrawable);
        healthBar = new Image(healthDrawable);
        energyBar = new Image(energyDrawable);

        addActor(background);
        addActor(healthBar);
        addActor(energyBar);


        setPosition(-100, 0);

        updateBars();
        debug();
    }

    private void updateBars() {
        float healthRatio = currentHealth / maxHealth;
        float energyRatio = currentEnergy / maxEnergy;

        healthBar.setSize(healthBar.getDrawable().getMinWidth() * healthRatio, healthBar.getDrawable().getMinHeight());
        energyBar.setSize(energyBar.getDrawable().getMinWidth() * energyRatio, energyBar.getDrawable().getMinHeight());

        healthBar.setPosition(background.getX() + 20, background.getY() + 144);
        energyBar.setPosition(background.getX() + 20, background.getY() + 31);
    }

    public void setHealth(float currentHealth, float maxHealth) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        updateBars();
    }

    public void setEnergy(float currentEnergy, float maxEnergy) {
        this.currentEnergy = currentEnergy;
        this.maxEnergy = maxEnergy;
        updateBars();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
//        background.setSize(getWidth(), getHeight());
        updateBars();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
//        background.setPosition(getX(), getY());
        updateBars();
    }
}
