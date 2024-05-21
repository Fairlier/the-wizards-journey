package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.PlayerMovementComponent;
import com.thewizardsjourney.game.ecs.component.SavePointComponent;
import com.thewizardsjourney.game.ecs.component.StatisticsComponent;

public class PlayerCollisionSystem extends IteratingSystem {
    private final ComponentMapper<BodyComponent> bodyComponentCM =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<CollisionComponent> collisionComponentCM =
            ComponentMapper.getFor(CollisionComponent.class);
    private final ComponentMapper<PlayerComponent> playerComponentCM =
            ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<SavePointComponent> savePointComponentCM =
            ComponentMapper.getFor(SavePointComponent.class);
    private final ComponentMapper<StatisticsComponent> statisticsComponentCM =
            ComponentMapper.getFor(StatisticsComponent.class);
    private final ComponentMapper<AnimationComponent> animationComponentCM =
            ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<PlayerMovementComponent> playerMovementComponentCM =
            ComponentMapper.getFor(PlayerMovementComponent.class);

    public PlayerCollisionSystem() {
        super(Family.all(
                BodyComponent.class,
                CollisionComponent.class,
                SavePointComponent.class,
                StatisticsComponent.class,
                AnimationComponent.class,
                PlayerMovementComponent.class,
                PlayerComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) { // TODO
        CollisionComponent collisionComponent = collisionComponentCM.get(entity);

        if (collisionComponent.firstCollidedEntity != null && collisionComponent.lastCollidedEntity == null) {
            beginCollision(collisionComponent, entity);
        }
        if (collisionComponent.firstCollidedEntity == null && collisionComponent.lastCollidedEntity != null) {
            endCollision(collisionComponent, entity);
        }
    }

    private void beginCollision(CollisionComponent collisionComponent, Entity entity) { // TODO
        BodyComponent bodyComponent = bodyComponentCM.get(entity);
        PlayerComponent playerComponent = playerComponentCM.get(entity);
        SavePointComponent savePointComponent = savePointComponentCM.get(entity);
        StatisticsComponent statisticsComponent = statisticsComponentCM.get(entity);
        AnimationComponent animationComponent = animationComponentCM.get(entity);
        PlayerMovementComponent playerMovementComponent = playerMovementComponentCM.get(entity);
        if (collisionComponent.firstCollidedEntity.getComponent(EntityTypeComponent.class) != null)  {
            EntityTypeComponent entityTypeComponent = collisionComponent.firstCollidedEntity.getComponent(EntityTypeComponent.class);
            if (entityTypeComponent.type == ECSConstants.EntityType.SENSOR_SAVE_POINT &&
                    collisionComponent.firstCollidedEntity.getComponent(BodyComponent.class) != null) {
                BodyComponent bodyComponentFirstCollidedEntity = collisionComponent.firstCollidedEntity.getComponent(BodyComponent.class);
                savePointComponent.position.set(bodyComponentFirstCollidedEntity.body.getTransform().getPosition());
            } else if (entityTypeComponent.type == ECSConstants.EntityType.COIN) {
                playerComponent.number_of_coins++;
            } else if (entityTypeComponent.type == ECSConstants.EntityType.SENSOR_HARM) {
                statisticsComponent.health = statisticsComponent.health == 0 ? 0 : statisticsComponent.health - 1;
                bodyComponent.body.setTransform(savePointComponent.position, 0);
            } else if (entityTypeComponent.type == ECSConstants.EntityType.SENSOR_INFO) {
                // TODO
            } else if (entityTypeComponent.type == ECSConstants.EntityType.SENSOR_EXIT) {
                // TODO
            } else if (entityTypeComponent.type == ECSConstants.EntityType.ROPE) {
                // TODO
                statisticsComponent.health = statisticsComponent.health == 0 ? 0 : statisticsComponent.health - 1;
                bodyComponent.body.setTransform(savePointComponent.position, 0);
            }
        }
    }

    private void endCollision(CollisionComponent collisionComponent, Entity entity) {

    }
}
