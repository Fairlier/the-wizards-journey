package com.thewizardsjourney.game.ecs.system;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.UNIT_SCALE;

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
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.ecs.component.PuzzleSensorComponent;
import com.thewizardsjourney.game.helper.EntityTypeInfo;
import com.thewizardsjourney.game.helper.JointInfo;

import java.util.Objects;

public class PuzzleSensorSystem extends IteratingSystem {
    private final World world;
    private final Viewport viewport;
    private int currentCount;
    private String targetObjectName;
    private Color targetColor;
    private final ShapeRenderer shapeRenderer;
    private final ComponentMapper<PuzzleSensorComponent> puzzleSensorComponentCM =
            ComponentMapper.getFor(PuzzleSensorComponent.class);

    public PuzzleSensorSystem(World world, Viewport viewport) {
        super(Family.all(PuzzleSensorComponent.class).get());
        this.world = world;
        this.viewport = viewport;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PuzzleSensorComponent puzzleSensorComponent = puzzleSensorComponentCM.get(entity);
        currentCount = 0;
        targetObjectName = puzzleSensorComponent.targetObjectName;
        targetColor = puzzleSensorComponent.color;
        collisionCheck(puzzleSensorComponent.lowerBound, puzzleSensorComponent.upperBound);
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
                System.out.println(entityTypeInfo.getObjectCategoryName() + " " + targetObjectName);
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

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        shapeRenderer.dispose();
    }
}
