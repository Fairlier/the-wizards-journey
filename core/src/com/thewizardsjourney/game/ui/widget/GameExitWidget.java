package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.thewizardsjourney.game.helper.GameInfo;

public class GameExitWidget extends Table {
    private final Label titleLabel;
    private final Button resumeButton;
    private final Button homeButton;
    private final Button closeButton;
    private final Window gameExitWindow;
    private final GameInfo gameInfo;

    public GameExitWidget(Skin skin, GameInfo gameInfo) {
        super(skin);
        this.gameInfo = gameInfo;

        titleLabel = new Label(gameInfo.getI18NBundle().get("gameExit.title"), skin, "game-label");
        closeButton = new Button(skin, "game-close-button");
        resumeButton = new Button(skin, "game-resume-button");
        homeButton = new Button(skin, "game-home-button");
        gameExitWindow = new Window("", skin, "game-window");

        setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        gameExitWindow.setMovable(false);
        gameExitWindow.add(resumeButton).top().left().pad(20).uniform();
        gameExitWindow.add(homeButton).top().right().pad(20).uniform();
        gameExitWindow.pack();

        titleLabel.setAlignment(Align.center);

        add().expand().fill();
        add(titleLabel).height(200).top().padTop(20);
        add().expandX().fill();
        add(closeButton).top().right().padTop(20).padRight(20);
        row();
        add().expandX().fill();
        add(gameExitWindow).center().padTop(20).padBottom(20);
        add().expandX().fill();
        pack();
    }

    public void updateLanguage() {
        titleLabel.setText(gameInfo.getI18NBundle().get("gameOver.title"));
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
