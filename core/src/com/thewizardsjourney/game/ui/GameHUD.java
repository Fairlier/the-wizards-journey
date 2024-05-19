package com.thewizardsjourney.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class GameHUD extends Table {
    private final Touchpad touchpad;
    private final Button pauseButton;
    private final Button jumpButton;
    private final Button castButton;
    private final Button reactionButton;
    private final Button switchButton;
    private final Image a;


    public GameHUD(Skin skin) {
        super(skin);
        pauseButton = new Button(skin, "game-pause-button");
        jumpButton = new Button(skin, "game-jump-button");
        castButton = new Button(skin, "game-cast-button");
        reactionButton = new Button(skin, "game-info-button");
        switchButton = new Button(skin, "game-button");
        touchpad = new Touchpad(5.0f, skin, "game-touchpad");

        a = new Image(skin, "game-health-bar");


        setupUI();
        setFillParent(true);
    }

    private void setupUI() {
        add().expand().align(Align.topLeft).pad(10);
        add().expand().fill();
        add(pauseButton).size(150, 150).expand().align(Align.topRight).pad(20);
        row();
        add(touchpad).size(175, 175).bottom().left().pad(100);
        add(castButton).expand().bottom().pad(10);
        Table bottomRight = new Table();
        bottomRight.add(reactionButton).size(75, 75);
        bottomRight.add(castButton).size(150, 150).padLeft(10).padBottom(10).padRight(20);
        bottomRight.row();
        bottomRight.add(switchButton).padTop(10);
        bottomRight.add(jumpButton).padLeft(10).padRight(100).padTop(10);
        add(bottomRight).bottom().right().padBottom(100);

//        debug();

        reactionButton.setVisible(false);
        castButton.setVisible(false);
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

    public Button getReactionButton() {
        return reactionButton;
    }

    public Button getSwitchButton() {
        return switchButton;
    }
}
