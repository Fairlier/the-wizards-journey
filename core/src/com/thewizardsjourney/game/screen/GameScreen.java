package com.thewizardsjourney.game.screen;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.GAME_SCENE_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.GAME_SCENE_WIDTH;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_WIDTH;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.TheWizardsJourney;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.system.OutOfBoundsSystem;
import com.thewizardsjourney.game.ecs.system.PlayerAbilitySystem;
import com.thewizardsjourney.game.ecs.system.AnimationSystem;
import com.thewizardsjourney.game.ecs.system.CameraSystem;
import com.thewizardsjourney.game.ecs.system.LightSystem;
import com.thewizardsjourney.game.ecs.system.PhysicsSystem;
import com.thewizardsjourney.game.ecs.system.PlayerCollisionSystem;
import com.thewizardsjourney.game.ecs.system.PlayerControlSystem;
import com.thewizardsjourney.game.ecs.system.PlayerMovementSystem;
import com.thewizardsjourney.game.ecs.system.PlayerStatisticsSystem;
import com.thewizardsjourney.game.ecs.system.PuzzleSensorSystem;
import com.thewizardsjourney.game.ecs.system.RenderingSystem;
import com.thewizardsjourney.game.helper.GameplayInfo;
import com.thewizardsjourney.game.map.MapHandler;
import com.thewizardsjourney.game.ui.GameHUD;

public class GameScreen extends ScreenAdapter {
    private final TheWizardsJourney main;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Engine engine;
    private MapHandler mapHandler;
    private InputHandler controller;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private GameHUD gameHUD;
    private InputMultiplexer inputMultiplexer;
    private GameplayInfo gameplayInfo;

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
        viewport = new FitViewport(GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT, camera);
        viewport.getCamera().position.set(
                viewport.getCamera().position.x + GAME_SCENE_WIDTH * 0.5f,
                viewport.getCamera().position.y + GAME_SCENE_HEIGHT * 0.5f,
                0
        );
        viewport.getCamera().update();
        viewport.update((int) GAME_SCENE_WIDTH, (int) GAME_SCENE_HEIGHT);

        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT), batch);

        gameplayInfo = new GameplayInfo();

        inputMultiplexer = new InputMultiplexer();
        controller = new InputHandler();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(controller);
        Gdx.input.setInputProcessor(inputMultiplexer);

        engine = new Engine();

        mapHandler = new MapHandler(engine, this.main.getAssetHandler(), main.getGameInfo());
        PhysicsSystem physicsSystem = new PhysicsSystem(mapHandler.getWorld());
        LightSystem lightSystem = new LightSystem(mapHandler.getRayHandler(), camera);
        PlayerMovementSystem playerMovementSystem = new PlayerMovementSystem(mapHandler.getWorld());
        PlayerControlSystem playerControlSystem = new PlayerControlSystem(controller);
        PlayerCollisionSystem playerCollisionSystem = new PlayerCollisionSystem(gameplayInfo, mapHandler.getMapInfo());
        PlayerAbilitySystem playerAbilitySystem = new PlayerAbilitySystem(mapHandler.getWorld(), controller, viewport);
        CameraSystem cameraSystem = new CameraSystem(camera, mapHandler.getMap());
        cameraSystem.setTargetEntity(mapHandler.getPlayer());
        RenderingSystem renderingSystem = new RenderingSystem(batch, viewport, mapHandler.getMap());
        AnimationSystem animationSystem = new AnimationSystem();
        PuzzleSensorSystem puzzleSensorSystem = new PuzzleSensorSystem(mapHandler.getWorld(), viewport);
        OutOfBoundsSystem outOfBoundsSystem = new OutOfBoundsSystem(mapHandler.getMap());
        PlayerStatisticsSystem playerStatisticsSystem = new PlayerStatisticsSystem(gameplayInfo);


        skin = new Skin(Gdx.files.internal("data/scene2D/ui-skin.json"));
        gameHUD = new GameHUD(skin, main.getGameInfo(), gameplayInfo);
        gameplayInfo.getPlayerStatisticsWidget().setHealth(mapHandler.getPlayerInfo().getPlayerSettingsData().getHealth(),
                mapHandler.getPlayerInfo().getPlayerSettingsData().getHealth());
        gameplayInfo.getPlayerStatisticsWidget().setEnergy(mapHandler.getPlayerInfo().getPlayerSettingsData().getEnergy(),
                mapHandler.getPlayerInfo().getPlayerSettingsData().getEnergy());
        stage.addActor(gameHUD);
        buttonProcessing();

        engine.addSystem(physicsSystem);
        engine.addSystem(lightSystem);
        engine.addSystem(playerMovementSystem);
        engine.addSystem(playerControlSystem);
        engine.addSystem(playerCollisionSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(playerAbilitySystem);
        engine.addSystem(puzzleSensorSystem);
        engine.addSystem(outOfBoundsSystem);
        engine.addSystem(playerStatisticsSystem);
    }

    private void buttonProcessing() {
        gameHUD.getTouchpad().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Touchpad touchpad = (Touchpad) actor;
                float knobPercentX = touchpad.getKnobPercentX();
                if (knobPercentX > 0) {
                    controller.setRight(true);
                    controller.setLeft(false);
                } else if (knobPercentX < 0) {
                    controller.setRight(false);
                    controller.setLeft(true);
                } else {
                    controller.setRight(false);
                    controller.setLeft(false);
                }
            }
        });

        gameHUD.getCastButton().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.setCast(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.setCast(false);
            }
        });

        gameHUD.getJumpButton().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.setJump(true);
                return true;

            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                controller.setJump(false);
            }
        });

        gameHUD.getSwitchButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameHUD.getJumpButton().isVisible()) {
                    gameHUD.getJumpButton().setVisible(false);
                    gameHUD.getTouchpad().setVisible(false);
                    gameHUD.getCastButton().setVisible(true);
                    gameHUD.setJumpButtonVisible(false);
                    controller.setAbility(true);

                } else if (gameHUD.getCastButton().isVisible()) {
                    gameHUD.getCastButton().setVisible(false);
                    controller.setAbility(false);
                    controller.getFingerLocation().setZero();
                    gameHUD.getJumpButton().setVisible(true);
                    gameHUD.setJumpButtonVisible(true);
                    gameHUD.getTouchpad().setVisible(true);
                }
            }
        });

        gameHUD.getPauseWidget().getResumeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setIntermediateScreen(main.getGameScreen().getClass(), main.getGameScreen().getClass());
            }
        });

        gameHUD.getPauseWidget().getHomeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.getGameInfo().setSelectedMapGroupName(main.getGameInfo().getMenuMapGroupName());
                main.setIntermediateScreen(main.getGameScreen().getClass(), main.getMenuScreen().getClass());
            }
        });

        gameHUD.getGameExitWidget().getResumeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setIntermediateScreen(main.getGameScreen().getClass(), main.getGameScreen().getClass());
            }
        });

        gameHUD.getGameExitWidget().getHomeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.getGameInfo().setSelectedMapGroupName(main.getGameInfo().getMenuMapGroupName());
                main.setIntermediateScreen(main.getGameScreen().getClass(), main.getMenuScreen().getClass());
            }
        });

        gameHUD.getGameOverWidget().getResumeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.setIntermediateScreen(main.getGameScreen().getClass(), main.getGameScreen().getClass());
            }
        });

        gameHUD.getGameOverWidget().getHomeButton().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                main.getGameInfo().setSelectedMapGroupName(main.getGameInfo().getMenuMapGroupName());
                main.setIntermediateScreen(main.getGameScreen().getClass(), main.getMenuScreen().getClass());
            }
        });
    }
}
