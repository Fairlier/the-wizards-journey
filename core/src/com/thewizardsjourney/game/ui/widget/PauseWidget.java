package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.thewizardsjourney.game.helper.GameInfo;

public class PauseWidget extends Table {
    private final Label titleLabel;
    private final Button resumeButton;
    private final Button homeButton;
    private final Button closeButton;
    private final Window pauseWindow;
    private final GameInfo gameInfo;

    public PauseWidget(Skin skin, GameInfo gameInfo) {
        super(skin);
        this.gameInfo = gameInfo;

        titleLabel = new Label(gameInfo.getI18NBundle().get("pause.title"), skin, "game-label");
        closeButton = new Button(skin, "game-close-button");
        resumeButton = new Button(skin, "game-resume-button");
        homeButton = new Button(skin, "game-home-button");
        pauseWindow = new Window("", skin, "game-window");

        setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        pauseWindow.setMovable(false);
        pauseWindow.add(resumeButton).top().left().pad(20).uniform();
        pauseWindow.add(homeButton).top().right().pad(20).uniform();
        pauseWindow.pack();

        titleLabel.setAlignment(Align.center);

        top();
        add().expand().fill();
        add(titleLabel).height(200).top().padTop(20);
        add().expandX().fill();
        add(closeButton).top().right().padTop(20).padRight(20).row();
        add().expandX().fill();
        add(pauseWindow).center().padTop(20).padBottom(20);
        add().expandX().fill();
        pack();
    }

    public void updateLanguage() {
        titleLabel.setText(gameInfo.getI18NBundle().get("pause.title"));
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getHomeButton() {
        return homeButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
