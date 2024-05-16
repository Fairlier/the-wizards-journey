package com.thewizardsjourney.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class GameHUD extends Table {
    private final Touchpad touchpad;
    private final TextButton pauseButton;
    private final TextButton jumpButton;
    private final TextButton castButton;
    private final TextButton reactionButton;
    private final TextButton switchButton;


    public GameHUD(Skin skin) {
        super(skin);

        Texture touchpad_background = new Texture(Gdx.files.internal("data/scene2D/touchpad_background.png"));
        Texture touchpad_knob = new Texture(Gdx.files.internal("data/scene2D/touchpad_knob.png"));
        Touchpad.TouchpadStyle ts = new Touchpad.TouchpadStyle();
        ts.background = new TextureRegionDrawable(new TextureRegion(touchpad_background));
        ts.knob = new TextureRegionDrawable(new TextureRegion(touchpad_knob));
        touchpad = new Touchpad(5.0f, ts);
        pauseButton = new TextButton("Pause", skin);
        jumpButton = new TextButton("Jump", skin);
        castButton = new TextButton("Ability", skin);
        switchButton = new TextButton("Change", skin);
        reactionButton = new TextButton("Reaction", skin);


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
        bottomRight.add(switchButton).size(75, 75).padTop(10);
        bottomRight.add(jumpButton).size(150, 150).padLeft(10).padRight(100).padTop(10);
        add(bottomRight).bottom().right().padBottom(100);

//        debug();

        reactionButton.setVisible(false);
        castButton.setVisible(false);
    }

    public Touchpad getTouchpad() {
        return touchpad;
    }

    public TextButton getPauseButton() {
        return pauseButton;
    }

    public TextButton getJumpButton() {
        return jumpButton;
    }

    public TextButton getCastButton() {
        return castButton;
    }

    public TextButton getReactionButton() {
        return reactionButton;
    }

    public TextButton getSwitchButton() {
        return switchButton;
    }
}
