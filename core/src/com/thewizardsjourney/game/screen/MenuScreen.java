package com.thewizardsjourney.game.screen;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.MENU_SCENE_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.MENU_SCENE_WIDTH;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.TheWizardsJourney;
import com.thewizardsjourney.game.ui.MenuHUD;

public class MenuScreen extends ScreenAdapter {
    private final TheWizardsJourney main;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private MenuHUD menuHUD;

    public MenuScreen(TheWizardsJourney main) {
        this.main = main;
    }

    @Override
    public void show() {
        initialization();
    }

    @Override
    public void hide() {
        Gdx.app.log("MenuScreen", "HIDE");
        dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(Math.min(delta, 1.0f / 60.0f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }

    private void initialization() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(MENU_SCENE_WIDTH, MENU_SCENE_HEIGHT, camera);
        viewport.getCamera().position.set(
                viewport.getCamera().position.x + MENU_SCENE_WIDTH * 0.5f,
                viewport.getCamera().position.y + MENU_SCENE_HEIGHT * 0.5f,
                0
        );
        viewport.getCamera().update();
        viewport.update((int) VIRTUAL_WIDTH, (int) VIRTUAL_HEIGHT);

        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT), batch);
        skin = new Skin(Gdx.files.internal("data/scene2D/glassy-ui.json"));
        menuHUD = new MenuHUD(skin);
        Gdx.input.setInputProcessor(stage);

        stage.addActor(menuHUD);

        menuHUD.getPlayButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setIntermediateScreen(main.getMenuScreen().getClass(), main.getGameScreen().getClass());
            }
        });
    }

}
