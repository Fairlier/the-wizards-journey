package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

public class InformationWidget extends Table {
    private final Label titleLabel;
    private final Button closeButton;
    private final Window pauseWindow;

    public InformationWidget(Skin skin) {
        super(skin);

        titleLabel = new Label("Info", skin, "game-label");
        closeButton = new Button(skin, "game-close-button");
        pauseWindow = new Window("", skin, "game-window");

        setFillParent(true);
        setupUI();
    }

    private void setupUI() {
        pauseWindow.setMovable(false);
        pauseWindow.pack();

        titleLabel.setAlignment(Align.center);

        top();
        add().expand().fill();
        add(titleLabel).height(200).center().padTop(20);
        add().expandX().fill();
        add(closeButton).top().right().padTop(20).padRight(20).row();
        add().expandX().fill();
        add(pauseWindow).center().padTop(20).padBottom(20);
        add().expandX().fill();
        pack();
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
