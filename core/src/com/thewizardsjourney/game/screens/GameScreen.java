package com.thewizardsjourney.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.TheWizardsJourney;
import com.thewizardsjourney.game.controllers.InputHandler;
import com.thewizardsjourney.game.ecs.components.BodyComponent;
import com.thewizardsjourney.game.ecs.components.FacingComponent;
import com.thewizardsjourney.game.ecs.components.JumpComponent;
import com.thewizardsjourney.game.ecs.components.MovementComponent;
import com.thewizardsjourney.game.ecs.components.PlayerComponent;
import com.thewizardsjourney.game.ecs.components.StateTypeComponent;
import com.thewizardsjourney.game.ecs.components.TransformComponent;
import com.thewizardsjourney.game.ecs.systems.JumpSystem;
import com.thewizardsjourney.game.ecs.systems.MovementSystem;
import com.thewizardsjourney.game.ecs.systems.PhysicsDebugSystem;
import com.thewizardsjourney.game.ecs.systems.PhysicsSystem;
import com.thewizardsjourney.game.ecs.systems.PlayerControlSystem;

public class GameScreen extends ScreenAdapter { // TODO
    private static final String TAG = "GameScreen";

    private static final float SCENE_WIDTH = 12.80f;
    private static final float SCENE_HEIGHT = 7.20f;

    private Viewport viewport;
    private OrthographicCamera camera;

    private Engine engine;

    private Vector3 point = new Vector3();
    BodyDef defaultDynamicBodyDef;
    World world;
    FixtureDef boxFixtureDef;
    PolygonShape square;
    Body playerBody;
    Entity player;

    private InputHandler controller;

    public GameScreen(TheWizardsJourney theWizardsJourney) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);
        viewport.getCamera().position.set(
                viewport.getCamera().position.x + SCENE_WIDTH * 0.5f,
                viewport.getCamera().position.y + SCENE_HEIGHT * 0.5f,
                0
        );
        viewport.getCamera().update();
        viewport.update((int) SCENE_WIDTH, (int) SCENE_HEIGHT);

        controller = new InputHandler(this);

        engine = new Engine();

        world = new World(new Vector2(0, -9.8f), true);
        createABox();
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

    private void createABox() {
        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(new Vector2[] {
                new Vector2(0f, 0f),
                new Vector2(SCENE_WIDTH * 2f, 0f),
                new Vector2(SCENE_WIDTH * 2f, SCENE_HEIGHT * 2f),
                new Vector2(0f, SCENE_HEIGHT * 2f),
        });
        BodyDef chainBodyDef = new BodyDef();
        chainBodyDef.type = BodyDef.BodyType.StaticBody;
        chainBodyDef.position.set(.0f, .0f);
        Body groundBody = world.createBody(chainBodyDef);
        FixtureDef groundFD = new FixtureDef();
        groundFD.filter.categoryBits = 0x0004;;
        groundFD.filter.maskBits = -1;
        groundFD.shape = chainShape;
        groundFD.density = 0;
        groundBody.createFixture(groundFD);
        chainShape.dispose();
    }

    public void movePlayerLeft() {
//        playerBody.applyLinearImpulse(-2f, 0,
//                playerBody.getPosition().x, playerBody.getPosition().y, true);
        playerBody.setLinearVelocity(MathUtils.lerp(playerBody.getLinearVelocity().x, -7f, 0.2f), playerBody.getLinearVelocity().y);
    }

    public void movePlayerRight() {
//        playerBody.applyLinearImpulse(2f, 0,
//                playerBody.getPosition().x, playerBody.getPosition().y, true);
        playerBody.setLinearVelocity(MathUtils.lerp(playerBody.getLinearVelocity().x, 7f, 0.2f), playerBody.getLinearVelocity().y);
    }

    public void movePlayerUp() {
//        playerBody.applyLinearImpulse(0, 5f,
//                playerBody.getPosition().x, playerBody.getPosition().y, true);
        playerBody.applyLinearImpulse(
                0,
                0.8f * playerBody.getMass(),
                playerBody.getWorldCenter().x,
                playerBody.getWorldCenter().y,
                true
        );
    }

    private void createSquare(float x, float y) {
        defaultDynamicBodyDef.position.set(x,y);
        Body body = world.createBody(defaultDynamicBodyDef);
        body.createFixture(boxFixtureDef);
    }

    public void createSquare(int screenX, int screenY) {
        viewport.getCamera().unproject(point.set(screenX, screenY, 0));
        createSquare(point.x, point.y);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // world.step(1/60f, 6, 2);
        camera.position.set(playerBody.getPosition(), 0);
        camera.update();
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        engine.removeAllEntities();
        engine.removeAllSystems();
        square.dispose();
        world.dispose();
    }
}
