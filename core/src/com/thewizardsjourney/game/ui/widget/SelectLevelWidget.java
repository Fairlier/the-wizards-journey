package com.thewizardsjourney.game.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.thewizardsjourney.game.helper.GameInfo;

public class SelectLevelWidget extends Table {
    private final Label titleLabel;
    private final Button closeButton;
    private final Window selectLevelWindow;
    private final Button prevButton;
    private final Button nextButton;
    private final TextButton levelButton0;
    private final TextButton levelButton1;
    private final TextButton playButton;
    private final GameInfo gameInfo;

    private int currentPage = 0;
    private int totalPages = 0;
    private final Array<String> mapGroupNames;

    public SelectLevelWidget(Skin skin, GameInfo gameInfo) {
        super(skin);
        this.gameInfo = gameInfo;
        mapGroupNames = gameInfo.getMapGroupNamesForLevelSelection();
        totalPages = (mapGroupNames.size + 1) / 2;

        titleLabel = new Label("Select level", skin, "game-label");
        selectLevelWindow = new Window("", skin, "game-window");
        closeButton = new Button(skin, "game-close-button");
        prevButton = new Button(skin, "game-prev-button");
        nextButton = new Button(skin, "game-next-button");
        levelButton0 = new TextButton("", skin, "game-text-button");
        levelButton1 = new TextButton("", skin, "game-text-button");
        playButton = new TextButton("Play", skin, "game-text-button");

        setFillParent(true);
        setupUI();
        updateLevelButtons();
        buttonProcessing();
    }

    private void setupUI() {
        selectLevelWindow.setMovable(false);
        selectLevelWindow.add(levelButton0).top().left().pad(20).uniform();
        selectLevelWindow.add(levelButton1).top().right().pad(20).uniform();
        selectLevelWindow.pack();

        titleLabel.setAlignment(Align.center);

        top();
        add().expand().fill();
        add().expand().fill();
        add(titleLabel).height(200).center().padTop(20);
        add().expandX().fill();
        add().expandX().fill();
        add(closeButton).top().right().padTop(20).padRight(20);
        row();
        add().expandX().fill();
        add(prevButton).center().right().padRight(20);
        add(selectLevelWindow).center().padTop(20).padBottom(20);
        add(nextButton).center().left().padLeft(20);
        add().expandX().fill();
        row();
        add().expandX().fill();
        add().expandX().fill();
        add(playButton).top().center();
        add().expandX().fill();
        add().expandX().fill();

        playButton.setVisible(false);

        pack();
        debug();
    }

    private void buttonProcessing() {
        prevButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (currentPage > 0) {
                    currentPage--;
                    updateLevelButtons();
                }
            }
        });

        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (currentPage < totalPages - 1) {
                    currentPage++;
                    updateLevelButtons();
                }
            }
        });

        levelButton0.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameInfo.setSelectedMapGroupName(mapGroupNames.get(Integer.parseInt(String.valueOf(levelButton0.getText()))));
                playButton.setVisible(true);
            }
        });

        levelButton1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameInfo.setSelectedMapGroupName(mapGroupNames.get(Integer.parseInt(String.valueOf(levelButton1.getText()))));
                playButton.setVisible(true);
            }
        });
    }

    private void updateLevelButtons() {
        int index0 = currentPage * 2;
        int index1 = index0 + 1;

        playButton.setVisible(false);
        gameInfo.setSelectedMapGroupName("");

        if (index0 < mapGroupNames.size) {
            levelButton0.setText(String.valueOf(index0));
            levelButton0.setVisible(true);
        } else {
            levelButton0.setVisible(false);
        }

        if (index1 < mapGroupNames.size) {
            levelButton1.setText(String.valueOf(index1));
            levelButton1.setVisible(true);
        } else {
            levelButton1.setVisible(false);
        }
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public Window getSelectLevelWindow() {
        return selectLevelWindow;
    }

    public Button getPrevButton() {
        return prevButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public TextButton getLevelButton0() {
        return levelButton0;
    }

    public TextButton getLevelButton1() {
        return levelButton1;
    }

    public TextButton getPlayButton() {
        return playButton;
    }
}
