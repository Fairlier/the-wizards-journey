package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.component.PlayerAbilityComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.StatisticsComponent;

public class PlayerAbilitySystem extends IteratingSystem {
    private boolean isCasting;
    private float time;
    private MouseJointDef mouseJointDef;
    private MouseJoint mouseJoint;
    private boolean previousIsInAbilityMode;
    private Vector2 touchPoint;
    private final World world;
    private final InputHandler controller;
    private final Viewport viewport;
    private final ShapeRenderer renderer;
    private final ComponentMapper<BodyComponent> bodyComponentCM
            = ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<PlayerAbilityComponent> abilityComponentCM
            = ComponentMapper.getFor(PlayerAbilityComponent.class);
    private final ComponentMapper<FacingComponent> facingComponentCM =
            ComponentMapper.getFor(FacingComponent.class);
    private final ComponentMapper<StatisticsComponent> statisticsComponentCM =
            ComponentMapper.getFor(StatisticsComponent.class);

    public PlayerAbilitySystem(World world, InputHandler controller, Viewport viewport) {
        super(Family.all(
                BodyComponent.class,
                PlayerAbilityComponent.class,
                FacingComponent.class,
                PlayerComponent.class
        ).get());
        this.world = world;
        this.controller = controller;
        this.viewport = viewport;
        touchPoint = new Vector2();
        renderer = new ShapeRenderer();
        mouseJointDef = new MouseJointDef();
        BodyDef mouseBodyDef = new BodyDef();
        mouseBodyDef.position.set(touchPoint);
        mouseJointDef.bodyA = world.createBody(mouseBodyDef);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) { // TODO
        PlayerAbilityComponent playerAbilityComponent = abilityComponentCM.get(entity);
        playerAbilityComponent.isInAbilityModeChanged = (playerAbilityComponent.isInAbilityMode != previousIsInAbilityMode);
        isCasting = playerAbilityComponent.isInAbilityMode && playerAbilityComponent.isCasting;
        time = deltaTime;
        if (playerAbilityComponent.isInAbilityMode) {
            BodyComponent bodyComponent = bodyComponentCM.get(entity);
            FacingComponent facingComponent = facingComponentCM.get(entity);
            StatisticsComponent statisticsComponent = statisticsComponentCM.get(entity);
            renderer.setProjectionMatrix(viewport.getCamera().combined);
            Shape shape = bodyComponent.body.getFixtureList().get(0).getShape();
            if (shape instanceof PolygonShape) {
                PolygonShape polygonShape = (PolygonShape) shape;
                int vertexCount = polygonShape.getVertexCount();
                float[] verticesArray = new float[vertexCount * 2];
                for (int i = 0; i < vertexCount; i++) {
                    Vector2 vertex = new Vector2();
                    polygonShape.getVertex(i, vertex);
                    verticesArray[i * 2] = vertex.x + bodyComponent.body.getTransform().getPosition().x;
                    verticesArray[i * 2 + 1] = vertex.y + bodyComponent.body.getTransform().getPosition().y;
                }
                if (playerAbilityComponent.isInAbilityModeChanged) {
                    touchPoint.set(bodyComponent.body.getTransform().getPosition());
                    touchPoint.x += facingComponent.direction == ECSConstants.FacingDirection.LEFT
                            ? -1 * statisticsComponent.range : statisticsComponent.range;
                } else {
                    Vector3 worldCoordinates = new Vector3(controller.getFingerLocation(), 0);
                    if (!worldCoordinates.equals(Vector3.Zero)) {
                        viewport.getCamera().unproject(worldCoordinates);
                        Polygon polygon = new Polygon();
                        polygon.setVertices(verticesArray);
                        if (!polygon.contains(worldCoordinates.x, worldCoordinates.y)) {
                            touchPoint.set(worldCoordinates.x, worldCoordinates.y);
                            Vector2 bodyPosition = bodyComponent.body.getTransform().getPosition();
                            Vector2 direction = touchPoint.cpy().sub(bodyPosition);
                            direction.limit(statisticsComponent.range);
                            touchPoint.set(bodyPosition).add(direction);
                        }
                        Vector2 facing = new Vector2(bodyComponent.body.getTransform().getPosition());
                        facing.x += facingComponent.direction == ECSConstants.FacingDirection.LEFT
                                ? -1 * statisticsComponent.range : statisticsComponent.range;
                        if (touchPoint.cpy().sub(bodyComponent.body.getTransform().getPosition()).x *
                            facing.cpy().sub(bodyComponent.body.getTransform().getPosition()).x < 0) {
                            facingComponent.direction = facingComponent.direction == ECSConstants.FacingDirection.LEFT ?
                                    ECSConstants.FacingDirection.RIGHT : ECSConstants.FacingDirection.LEFT;
                        }
                    }
                }
                drawTouchPoint();
                drawLineToTouchPoint(bodyComponent.body.getTransform().getPosition());
                world.rayCast(callback, bodyComponent.body.getTransform().getPosition(), touchPoint);
                if (mouseJoint != null) {
                    mouseJoint.setTarget(touchPoint);
                }
            }
        }
        previousIsInAbilityMode = playerAbilityComponent.isInAbilityMode;
    }

    private final RayCastCallback callback = new RayCastCallback() { // TODO
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            Shape shape = fixture.getShape();
            if (isCasting) {
                if (mouseJoint == null) {
                    Body body = fixture.getBody();
                    mouseJointDef.bodyB = body;
                    mouseJointDef.collideConnected = true;
                    mouseJointDef.maxForce = 500.0f;
                    mouseJointDef.target.set(touchPoint);
                    mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);
                }
            } else {
                if (mouseJoint != null) {
                    world.destroyJoint(mouseJoint);
                    mouseJoint = null;
                }
            }
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.BLACK);
            switch (shape.getType()) {
                case Circle:
                    CircleShape circleShape = (CircleShape) shape;
                    Vector2 center = fixture.getBody().getWorldPoint(circleShape.getPosition());
                    float radius = circleShape.getRadius();
                    renderer.circle(center.x, center.y, radius);
                    break;
                case Edge:
                    EdgeShape edgeShape = (EdgeShape) shape;
                    Vector2 vertex1 = new Vector2();
                    Vector2 vertex2 = new Vector2();
                    edgeShape.getVertex1(vertex1);
                    edgeShape.getVertex2(vertex2);
                    renderer.line(vertex1, vertex2);
                    break;
                case Polygon:
                    PolygonShape polygonShape = (PolygonShape) shape;
                    int vertexCount = polygonShape.getVertexCount();
                    float[] verticesArray = new float[vertexCount * 2];
                    for (int i = 0; i < vertexCount; i++) {
                        Vector2 vertex = new Vector2();
                        polygonShape.getVertex(i, vertex);
                        verticesArray[i * 2] = vertex.x;
                        verticesArray[i * 2 + 1] = vertex.y;
                    }
                    renderer.polygon(verticesArray);
                    break;
                case Chain:
                    ChainShape chainShape = (ChainShape) shape;
                    int count = chainShape.getVertexCount();
                    Vector2 prevVertex = new Vector2();
                    Vector2 curVertex = new Vector2();
                    for (int i = 0; i < count; i++) {
                        chainShape.getVertex(i, curVertex);
                        if (i > 0) {
                            renderer.line(prevVertex, curVertex);
                        }
                        prevVertex.set(curVertex);
                    }
                    if (chainShape.isLooped()) {
                        chainShape.getVertex(0, curVertex);
                        renderer.line(prevVertex, curVertex);
                    }
                    break;
                default:
                    break;
            }
            renderer.end();
            return 0;
        }
    };

    private void drawTouchPoint() {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.circle(touchPoint.x, touchPoint.y, 0.1f, 100);
        renderer.end();
    }

    private void drawLineToTouchPoint(Vector2 position) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.line(position.x, position.y, touchPoint.x, touchPoint.y);
        renderer.end();
    }
}
