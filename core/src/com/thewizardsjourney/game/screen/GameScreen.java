package com.thewizardsjourney.game.screen;

import static com.thewizardsjourney.game.constant.General.Screens.UNITS;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.TheWizardsJourney;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.system.JumpSystem;
import com.thewizardsjourney.game.ecs.system.LightSystem;
import com.thewizardsjourney.game.ecs.system.MovementSystem;
import com.thewizardsjourney.game.ecs.system.PhysicsDebugSystem;
import com.thewizardsjourney.game.ecs.system.PhysicsSystem;
import com.thewizardsjourney.game.ecs.system.PlayerCollisionSystem;
import com.thewizardsjourney.game.ecs.system.PlayerControlSystem;
import com.thewizardsjourney.game.map.MapHandler;

public class GameScreen extends ScreenAdapter { // TODO
    private final TheWizardsJourney main;

    private static final float SCENE_WIDTH = 12.80f;
    private static final float SCENE_HEIGHT = 7.20f;

    private Viewport viewport;
    private OrthographicCamera camera;

    private Engine engine;

    private MapHandler mapHandler;
    private OrthogonalTiledMapRenderer renderer;
    private InputHandler controller;

    public GameScreen(TheWizardsJourney main) {
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
        renderer.setView(camera);
        renderer.render();

        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        engine.removeAllEntities();
        engine.removeAllSystems();
        mapHandler.dispose();
    }

    private void initialization() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.getCamera().position.set(
                viewport.getCamera().position.x + SCENE_WIDTH * 0.5f,
                viewport.getCamera().position.y + SCENE_HEIGHT * 0.5f,
                0
        );
        viewport.getCamera().update();
        viewport.update((int) SCENE_WIDTH, (int) SCENE_HEIGHT);

        controller = new InputHandler();
        Gdx.input.setInputProcessor(controller);
        engine = new Engine();

        mapHandler = new MapHandler(engine, this.main.getAssetHandler(), main.getGameData());
        renderer = new OrthogonalTiledMapRenderer(mapHandler.getMap(), 1.0f / UNITS);

        PhysicsDebugSystem physicsDebugSystem = new PhysicsDebugSystem(mapHandler.getWorld(), viewport);
        PhysicsSystem physicsSystem = new PhysicsSystem(mapHandler.getWorld());
        LightSystem lightSystem = new LightSystem(mapHandler.getRayHandler(), camera);
        MovementSystem movementSystem = new MovementSystem();
        JumpSystem jumpSystem = new JumpSystem();
        PlayerControlSystem playerControlSystem = new PlayerControlSystem(controller);
        PlayerCollisionSystem playerCollisionSystem = new PlayerCollisionSystem();

        engine.addSystem(physicsDebugSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(lightSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(jumpSystem);
        engine.addSystem(playerControlSystem);
        engine.addSystem(playerCollisionSystem);
    }
}
