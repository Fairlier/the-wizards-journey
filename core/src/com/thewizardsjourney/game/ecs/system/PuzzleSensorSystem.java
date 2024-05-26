package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.PuzzleSensorComponent;
import com.thewizardsjourney.game.helper.EntityTypeInfo;
import com.thewizardsjourney.game.helper.JointInfo;

import java.util.Objects;

public class PuzzleSensorSystem extends IteratingSystem {
    private final World world;
    private int currentCount;
    private String targetObjectName;
    private Color targetColor;
    private final ShapeRenderer renderer;
    private final Viewport viewport;
    private final ComponentMapper<BodyComponent> bodyComponentCM =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<PuzzleSensorComponent> puzzleSensorComponentCM =
            ComponentMapper.getFor(PuzzleSensorComponent.class);

    public PuzzleSensorSystem(World world, Viewport viewport) {
        super(Family.all(
                BodyComponent.class,
                PuzzleSensorComponent.class
        ).get());
        this.world = world;
        this.viewport = viewport;
        renderer = new ShapeRenderer();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bodyComponentCM.get(entity);
        PuzzleSensorComponent puzzleSensorComponent = puzzleSensorComponentCM.get(entity);
        currentCount = 0;
        targetObjectName = puzzleSensorComponent.targetObjectName;
        targetColor = puzzleSensorComponent.color;
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        Fixture fixture = bodyComponent.body.getFixtureList().get(0);
        drawShapeOutline(fixture, puzzleSensorComponent.drawShape);
        if (currentCount >= puzzleSensorComponent.numberOfTargets) {
            activateJoints(puzzleSensorComponent.joints);
        } else {
            deactivateJoints(puzzleSensorComponent.joints);
        }
    }

    private void collisionCheck(Vector2 lowerBound, Vector2 upperBound) {
        world.QueryAABB(callback, lowerBound.x, lowerBound.y, upperBound.x, upperBound.y);
    }

    private final QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            EntityTypeInfo entityTypeInfo = (EntityTypeInfo) fixture.getUserData();
            if (entityTypeInfo != null) {
                if (Objects.equals(entityTypeInfo.getObjectCategoryName(), targetObjectName)) {
                    if (targetColor != null) {
                        if (entityTypeInfo.getColor() != null && entityTypeInfo.getColor().equals(targetColor)) {
                            currentCount++;
                        }
                    } else {
                        currentCount++;
                    }
                }
            }
            return true;
        }
    };

    private void activateJoints(Array<JointInfo> joints) {
        for (JointInfo joint : joints) {
            if (joint.getJoint().getType() == JointDef.JointType.PrismaticJoint) {
                PrismaticJoint prismaticJoint = (PrismaticJoint) joint.getJoint();
                prismaticJoint.setMotorSpeed(joint.getMotorSpeed() * -1);
            }
        }
    }

    private void deactivateJoints(Array<JointInfo> joints) {
        for (JointInfo joint : joints) {
            if (joint.getJoint().getType() == JointDef.JointType.PrismaticJoint) {
                PrismaticJoint prismaticJoint = (PrismaticJoint) joint.getJoint();
                prismaticJoint.setMotorSpeed(joint.getMotorSpeed());
            }
        }
    }

    private void drawShapeOutline(Fixture fixture, boolean drawShape) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        Shape shape = fixture.getShape();
        if (Objects.requireNonNull(shape.getType()) == Shape.Type.Polygon) {
            PolygonShape polygonShape = (PolygonShape) shape;
            int vertexCount = polygonShape.getVertexCount();
            float[] verticesArray = new float[vertexCount * 2];
            for (int i = 0; i < vertexCount; i++) {
                Vector2 vertex = new Vector2();
                polygonShape.getVertex(i, vertex);
                verticesArray[i * 2] = vertex.x;
                verticesArray[i * 2 + 1] = vertex.y;
            }

            Vector2 lowerBound = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
            Vector2 upperBound = new Vector2(-Float.MAX_VALUE, -Float.MAX_VALUE);

            for (int i = 0; i < vertexCount; i++) {
                Vector2 vertex = new Vector2(verticesArray[i * 2], verticesArray[i * 2 + 1]);
                lowerBound.x = Math.min(lowerBound.x, vertex.x);
                lowerBound.y = Math.min(lowerBound.y, vertex.y);
                upperBound.x = Math.max(upperBound.x, vertex.x);
                upperBound.y = Math.max(upperBound.y, vertex.y);
            }
            if (drawShape) {
                renderer.polygon(verticesArray);
            }
            collisionCheck(lowerBound, upperBound);
        }
        renderer.end();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        renderer.dispose();
    }
}
