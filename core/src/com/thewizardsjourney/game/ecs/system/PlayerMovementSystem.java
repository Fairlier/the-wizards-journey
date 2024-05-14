package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.ecs.component.PlayerAbilityComponent;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.PlayerMovementComponent;
import com.thewizardsjourney.game.helper.EntityTypeInfo;

public class PlayerMovementSystem extends IteratingSystem {
    private boolean isColliding;
    private final World world;
    private final ComponentMapper<BodyComponent> bodyComponentCM =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<PlayerMovementComponent> playerMovementComponentCM =
            ComponentMapper.getFor(PlayerMovementComponent.class);
    private final ComponentMapper<AnimationComponent> animationComponentCM =
            ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<PlayerAbilityComponent> abilityComponentCM =
            ComponentMapper.getFor(PlayerAbilityComponent.class);

    public PlayerMovementSystem(World world) {
        super(Family.all(
                BodyComponent.class,
                PlayerMovementComponent.class,
                PlayerAbilityComponent.class,
                AnimationComponent.class
        ).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bodyComponentCM.get(entity);
        PlayerMovementComponent playerMovementComponent  = playerMovementComponentCM.get(entity);
        AnimationComponent animationComponent = animationComponentCM.get(entity);
        PlayerAbilityComponent playerAbilityComponent = abilityComponentCM.get(entity);
        isColliding = false;
        collisionCheck(bodyComponent.body);
        ECSConstants.AnimationState previousState = animationComponent.state;
        if (!isColliding) {
            if (bodyComponent.body.getLinearVelocity().y <= 0) {
                animationComponent.state = ECSConstants.AnimationState.FALL;
            }
        } else {
            if (playerMovementComponent.isJumping) {
                bodyComponent.body.setLinearVelocity(
                        bodyComponent.body.getLinearVelocity().x,
                        playerMovementComponent.jumpVelocity.y * bodyComponent.body.getMass()
                );
                animationComponent.state = ECSConstants.AnimationState.JUMP;
            }
            if (bodyComponent.body.getLinearVelocity().equals(Vector2.Zero)) {
                if (playerAbilityComponent.isInAbilityMode) {
                    animationComponent.state = ECSConstants.AnimationState.ABILITY;
                } else {
                    animationComponent.state = ECSConstants.AnimationState.IDLE;
                }
            }
            if (playerMovementComponent.isRunning && bodyComponent.body.getLinearVelocity().y == 0) {
                animationComponent.state = ECSConstants.AnimationState.RUN;
            }
        }
        bodyComponent.body.setLinearVelocity(
                playerMovementComponent.runVelocity.x,
                bodyComponent.body.getLinearVelocity().y
        );
        animationComponent.isStateChanged = (animationComponent.state != previousState);
    }

    private void collisionCheck(Body body) { // TODO константы?
        Vector2 bottomLeftPoint = new Vector2();
        Vector2 bottomRightPoint = new Vector2();
        ((PolygonShape) body.getFixtureList().get(0).getShape()).getVertex(0, bottomLeftPoint);
        ((PolygonShape) body.getFixtureList().get(0).getShape()).getVertex(1, bottomRightPoint);
        bottomLeftPoint.set(
                body.getTransform().getPosition().x + (bottomRightPoint.x * 0.7f),
                body.getTransform().getPosition().y + bottomLeftPoint.y);
        bottomRightPoint.set(
                body.getTransform().getPosition().x + (bottomRightPoint.x * 0.7f),
                body.getTransform().getPosition().y + (bottomRightPoint.y * 1.01f));
        world.QueryAABB(callback, bottomLeftPoint.x, bottomLeftPoint.y, bottomRightPoint.x, bottomRightPoint.y);
    }

    private final QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (((EntityTypeInfo) fixture.getUserData()).getEntityType() != ECSConstants.EntityType.PLAYER) { // TODO
                isColliding = true;
            }
            return true;
        }
    };
}
