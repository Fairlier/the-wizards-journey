package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class InformationWidget extends Table {
    private final Button closeButton;
    private final Window pauseWindow;
    private Image image;

    public InformationWidget(Skin skin) {
        super(skin);

        closeButton = new Button(skin, "game-close-button");
        pauseWindow = new Window("", skin, "game-window");
        image = new Image();

        setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        clear();
        pauseWindow.clear();

        pauseWindow.setMovable(false);
        pauseWindow.add(image);
        pauseWindow.pack();

        add().expand().fill();
        add().expandX().fill();
        add(closeButton).top().right().padTop(20).padRight(20);
        row();
        add().expandX().fill();
        add(pauseWindow).size(500, 550).top().padBottom(20);
        add().expandX().fill();
        pack();
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void update() {
        setupUI();
    }
}
