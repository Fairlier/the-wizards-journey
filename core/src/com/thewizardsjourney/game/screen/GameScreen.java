package com.thewizardsjourney.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.TheWizardsJourney;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;
import com.thewizardsjourney.game.ecs.component.MovementComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.StateTypeComponent;
import com.thewizardsjourney.game.ecs.component.TransformComponent;
import com.thewizardsjourney.game.ecs.system.JumpSystem;
import com.thewizardsjourney.game.ecs.system.MovementSystem;
import com.thewizardsjourney.game.ecs.system.PhysicsDebugSystem;
import com.thewizardsjourney.game.ecs.system.PhysicsSystem;
import com.thewizardsjourney.game.ecs.system.PlayerControlSystem;
import com.thewizardsjourney.game.map.MapHandler;

public class GameScreen extends ScreenAdapter { // TODO
    private TheWizardsJourney main;

    private static final float SCENE_WIDTH = 12.80f;
    private static final float SCENE_HEIGHT = 7.20f;

    private Viewport viewport;
    private OrthographicCamera camera;

    private Engine engine;

    BodyDef defaultDynamicBodyDef;
    FixtureDef boxFixtureDef;
    PolygonShape square;
    Body playerBody;
    Entity player;

    MapHandler mapHandler;
    OrthogonalTiledMapRenderer renderer;

    private InputHandler controller;

    public GameScreen(TheWizardsJourney main) {
        this.main = main;
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
        engine = new Engine();
        mapHandler = new MapHandler(engine, this.main.getAssetHandler());
        World world = mapHandler.getWorld();
        renderer = new OrthogonalTiledMapRenderer(mapHandler.getMap(), 1.0f / 30.0f);

        defaultDynamicBodyDef = new BodyDef();
        defaultDynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        square = new PolygonShape();
        square.setAsBox(0.5f, 1f);
        boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = square;
        boxFixtureDef.density = 0.8f;
        boxFixtureDef.friction = 0.8f;
        boxFixtureDef.restitution = 0.15f;
        defaultDynamicBodyDef.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f);
        playerBody = world.createBody(defaultDynamicBodyDef);
        playerBody.createFixture(boxFixtureDef);
        player = engine.createEntity();
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = playerBody;
        bodyComponent.body.setUserData(player);
        player.add(bodyComponent);
        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.position.set(SCENE_WIDTH * 0.5f, SCENE_HEIGHT * 0.5f);
        player.add(transformComponent);
        MovementComponent movementComponent = engine.createComponent(MovementComponent.class);
        movementComponent.speed = 17.0f;
        player.add(movementComponent);
        JumpComponent jumpComponent = engine.createComponent(JumpComponent.class);
        jumpComponent.speed = 2.0f;
        player.add(jumpComponent);
        FacingComponent facingComponent = engine.createComponent(FacingComponent.class);
        player.add(facingComponent);
        StateTypeComponent stateTypeComponent = engine.createComponent(StateTypeComponent.class);
        player.add(stateTypeComponent);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        player.add(playerComponent);

        PhysicsDebugSystem physicsDebugSystem = new PhysicsDebugSystem(world, viewport);
        PhysicsSystem physicsSystem = new PhysicsSystem(world);
        MovementSystem movementSystem = new MovementSystem();
        JumpSystem jumpSystem = new JumpSystem();
        PlayerControlSystem playerControlSystem = new PlayerControlSystem(controller);
        engine.addEntity(player);
        engine.addSystem(physicsDebugSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(jumpSystem);
        engine.addSystem(playerControlSystem);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(playerBody.getPosition(), 0);
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
        engine.removeAllEntities();
        engine.removeAllSystems();
        square.dispose();
        mapHandler.destroyPhysics();
    }
}
