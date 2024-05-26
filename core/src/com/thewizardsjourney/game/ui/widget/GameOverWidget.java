package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

public class GameOverWidget extends Table {
    private final Label titleLabel;
    private final Button resumeButton;
    private final Button homeButton;
    private final Window gameOverWindow;

    public GameOverWidget(Skin skin) {
        super(skin);

        titleLabel = new Label("ВЫ ПРОИГРАЛИ!", skin, "game-label");
        resumeButton = new Button(skin, "game-resume-button");
        homeButton = new Button(skin, "game-home-button");
        gameOverWindow = new Window("", skin, "game-window");

        setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        gameOverWindow.setMovable(false);
        gameOverWindow.add(resumeButton).top().left().pad(20).uniform();
        gameOverWindow.add(homeButton).top().right().pad(20).uniform();
        gameOverWindow.pack();

        titleLabel.setAlignment(Align.center);

        add().expand().fill();
        add(titleLabel).height(200).top().padTop(20);
        add().expandX().fill();
        row();
        add().expandX().fill();
        add(gameOverWindow).center().padTop(20).padBottom(20);
        add().expandX().fill();
        pack();
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getHomeButton() {
        return homeButton;
    }
}
