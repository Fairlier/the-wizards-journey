package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class PlayerStatisticsWidget extends WidgetGroup {
    private final Image background;
    private final Image healthBar;
    private final Image energyBar;
    private float currentHealth;
    private float maxHealth;
    private float currentEnergy;
    private float maxEnergy;
    private final float scale = 0.7f;

    public PlayerStatisticsWidget(Skin skin) {
        background = new Image(skin, "game-player-bar");
        healthBar = new Image(skin, "game-health-bar");
        energyBar = new Image(skin, "game-energy-bar");

        background.setScale(scale);
        healthBar.setScale(scale);
        energyBar.setScale(scale);

        background.setPosition(0, -45);

        addActor(background);
        addActor(healthBar);
        addActor(energyBar);

        updateBars();
        debug();
    }

    private void updateBars() {
        float healthRatio = currentHealth / maxHealth;
        float energyRatio = currentEnergy / maxEnergy;

        healthBar.setSize(healthBar.getDrawable().getMinWidth() * healthRatio, healthBar.getDrawable().getMinHeight());
        energyBar.setSize(energyBar.getDrawable().getMinWidth() * energyRatio, energyBar.getDrawable().getMinHeight());

        healthBar.setPosition(background.getX() + 20 * scale, background.getY() + 144 * scale);
        energyBar.setPosition(background.getX() + 20 * scale, background.getY() + 31 * scale);
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

    public Image getBackground() {
        return background;
    }

    public Image getHealthBar() {
        return healthBar;
    }

    public Image getEnergyBar() {
        return energyBar;
    }
}
