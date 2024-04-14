package com.thewizardsjourney.game.screen;

import static com.thewizardsjourney.game.constant.General.Main.SCENE_HEIGHT;
import static com.thewizardsjourney.game.constant.General.Main.SCENE_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.TheWizardsJourney;

public class MenuScreen extends ScreenAdapter {
    private final TheWizardsJourney main;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    public MenuScreen(TheWizardsJourney main) {
        this.main = main;
    }

    @Override
    public void show() {
        initialization();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void initialization() {
        Gdx.app.log("MENU", "DA");
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.getCamera().position.set(
                viewport.getCamera().position.x + SCENE_WIDTH * 0.5f,
                viewport.getCamera().position.y + SCENE_HEIGHT * 0.5f,
                0
        );
        viewport.getCamera().update();
        viewport.update((int) SCENE_WIDTH, (int) SCENE_HEIGHT);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("data/scene2D/glassy-ui.json"));

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        TextButton playButton = new TextButton("PLAY", skin);
        TextButton shopButton = new TextButton("SHOP", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton achievementsButton = new TextButton("Achievements", skin);

        buttonTable.add(achievementsButton).top().left().pad(10f); // achievements в левом верхнем углу
        buttonTable.add().expandX().fillX(); // Пустая ячейка для отцентрирования кнопки "Settings"
        buttonTable.add(settingsButton).bottom().right().pad(10f); // settings в правом нижнем углу
        buttonTable.row(); // переход на следующую строку
        buttonTable.add().expand().bottom().right().pad(10f); // Пустая ячейка для размещения кнопки "PLAY" в верхнем левом углу
        buttonTable.add(playButton).bottom().right().pad(10f); // play в верхнем правом углу
        buttonTable.row(); // переход на следующую строку
        buttonTable.add(shopButton).bottom().left().pad(10f); // shop в левом нижнем углу
        buttonTable.add().expandX().fillX(); // Пустая ячейка для отцентрирования кнопки "Settings"
        buttonTable.add().expand().top().left().pad(10f); // Пустая ячейка для размещения кнопки "PLAY" в верхнем левом углу


        stage.addActor(buttonTable);
    }

}
