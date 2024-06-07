package com.thewizardsjourney.game.screen;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.GAME_SCENE_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.GAME_SCENE_WIDTH;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.MENU_SCENE_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.MENU_SCENE_WIDTH;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_WIDTH;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.TheWizardsJourney;
import com.thewizardsjourney.game.ecs.system.AnimationSystem;
import com.thewizardsjourney.game.ecs.system.CameraSystem;
import com.thewizardsjourney.game.ecs.system.LightSystem;
import com.thewizardsjourney.game.ecs.system.OutOfBoundsSystem;
import com.thewizardsjourney.game.ecs.system.PhysicsSystem;
import com.thewizardsjourney.game.ecs.system.PlayerAbilitySystem;
import com.thewizardsjourney.game.ecs.system.PlayerCollisionSystem;
import com.thewizardsjourney.game.ecs.system.PlayerControlSystem;
import com.thewizardsjourney.game.ecs.system.PlayerMovementSystem;
import com.thewizardsjourney.game.ecs.system.PlayerStatisticsSystem;
import com.thewizardsjourney.game.ecs.system.PuzzleSensorSystem;
import com.thewizardsjourney.game.ecs.system.RenderingSystem;
import com.thewizardsjourney.game.map.MapHandler;
import com.thewizardsjourney.game.ui.MenuHUD;

public class MenuScreen extends ScreenAdapter {
    private final TheWizardsJourney main;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Engine engine;
    private MapHandler mapHandler;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
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
        dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        engine.update(delta);
        stage.act(Math.min(delta, 1.0f / 60.0f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        engine.removeAllEntities();
        engine.removeAllSystems();
        mapHandler.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
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
        viewport.update((int) MENU_SCENE_WIDTH, (int) MENU_SCENE_HEIGHT);

        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT), batch);

        Gdx.input.setInputProcessor(stage);

        engine = new Engine();
        mapHandler = new MapHandler(engine, this.main.getAssetHandler(), main.getGameInfo());
        PhysicsSystem physicsSystem = new PhysicsSystem(mapHandler.getWorld());
        PlayerMovementSystem playerMovementSystem = new PlayerMovementSystem(mapHandler.getWorld());
        LightSystem lightSystem = new LightSystem(mapHandler.getRayHandler(), camera);
        CameraSystem cameraSystem = new CameraSystem(camera, mapHandler.getMap());
        cameraSystem.setTargetEntity(mapHandler.getPlayer());
        RenderingSystem renderingSystem = new RenderingSystem(batch, viewport, mapHandler.getMap());
        AnimationSystem animationSystem = new AnimationSystem();
        OutOfBoundsSystem outOfBoundsSystem = new OutOfBoundsSystem(mapHandler.getMap());

        skin = new Skin(Gdx.files.internal("data/scene2D/ui-skin.json"));
        menuHUD = new MenuHUD(skin, main.getAssetHandler(), main.getGameInfo());
        stage.addActor(menuHUD);
        buttonProcessing(menuHUD);

        engine.addSystem(physicsSystem);
        engine.addSystem(lightSystem);
        engine.addSystem(playerMovementSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(outOfBoundsSystem);
    }

    private void buttonProcessing(MenuHUD menuHUD) {
        menuHUD.getSelectLevelWidget().getPlayButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setIntermediateScreen(main.getMenuScreen().getClass(), main.getGameScreen().getClass());
            }
        });
    }
}
