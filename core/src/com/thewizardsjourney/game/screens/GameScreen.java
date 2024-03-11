package com.thewizardsjourney.game.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.thewizardsjourney.game.ecs.PhysicsDebugSystem;

public class GameScreen extends ScreenAdapter { // TODO
    private static final String TAG = "GameScreen";

    private static final float SCENE_WIDTH = 12.80f;
    private static final float SCENE_HEIGHT = 7.20f;

    private Viewport viewport;
    private OrthographicCamera camera;

    private PooledEngine engine;

    private Vector3 point = new Vector3();
    BodyDef defaultDynamicBodyDef;
    World world;
    FixtureDef boxFixtureDef;
    PolygonShape square;
    Body player;

    private InputHandler inputHandler;

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

        inputHandler = new InputHandler(this);

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
        player = world.createBody(defaultDynamicBodyDef);
        player.createFixture(boxFixtureDef);

        engine = new PooledEngine();

        PhysicsDebugSystem physicsDebugSystem = new PhysicsDebugSystem(world, viewport);
        engine.addSystem(physicsDebugSystem);
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
        player.applyLinearImpulse(-2f, 0,
                player.getPosition().x, player.getPosition().y, true);
    }

    public void movePlayerRight() {
        player.applyLinearImpulse(2f, 0,
                player.getPosition().x, player.getPosition().y, true);
    }

    public void movePlayerUp() {
        player.applyLinearImpulse(0, 5f,
                player.getPosition().x, player.getPosition().y, true);
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
        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1/60f, 6, 2);
        camera.position.set(player.getPosition(), 0);
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
