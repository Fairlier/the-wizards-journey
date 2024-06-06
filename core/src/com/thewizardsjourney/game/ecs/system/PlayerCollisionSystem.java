package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.PlayerMovementComponent;
import com.thewizardsjourney.game.ecs.component.SavePointComponent;
import com.thewizardsjourney.game.ecs.component.StatisticsComponent;
import com.thewizardsjourney.game.helper.EntityTypeInfo;
import com.thewizardsjourney.game.helper.GameplayInfo;
import com.thewizardsjourney.game.helper.MapInfo;

public class PlayerCollisionSystem extends IteratingSystem {
    private final GameplayInfo gameplayInfo;
    private final MapInfo mapInfo;
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

    public PlayerCollisionSystem(GameplayInfo gameplayInfo, MapInfo mapInfo) {
        super(Family.all(
                BodyComponent.class,
                CollisionComponent.class,
                SavePointComponent.class,
                StatisticsComponent.class,
                AnimationComponent.class,
                PlayerMovementComponent.class,
                PlayerComponent.class
        ).get());
        this.gameplayInfo = gameplayInfo;
        this.mapInfo = mapInfo;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent collisionComponent = collisionComponentCM.get(entity);
        if (collisionComponent.firstCollidedEntity != null && collisionComponent.lastCollidedEntity == null) {
            beginCollision(collisionComponent, entity);
        }
        if (collisionComponent.firstCollidedEntity == null && collisionComponent.lastCollidedEntity != null) {
            endCollision(collisionComponent, entity);
        }
    }

    private void beginCollision(CollisionComponent collisionComponent, Entity entity) {
        BodyComponent bodyComponent = bodyComponentCM.get(entity);
        PlayerComponent playerComponent = playerComponentCM.get(entity);
        SavePointComponent savePointComponent = savePointComponentCM.get(entity);
        StatisticsComponent statisticsComponent = statisticsComponentCM.get(entity);
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
                gameplayInfo.getInformationButton().setVisible(true);
                gameplayInfo.setGameIsExit(false);
                gameplayInfo.getInformationWidget().setImage(new Image(mapInfo.getPuzzlesAtlas().findRegion(
                        ((EntityTypeInfo) collisionComponent.firstCollidedEntity.getComponent(BodyComponent.class).body.getFixtureList().get(0).getUserData()).getAtlasRegionName())));
            } else if (entityTypeComponent.type == ECSConstants.EntityType.SENSOR_EXIT) {
                gameplayInfo.getInformationButton().setVisible(true);
                gameplayInfo.setGameIsExit(true);
            } else if (entityTypeComponent.type == ECSConstants.EntityType.ROPE) {
                statisticsComponent.health = statisticsComponent.health == 0 ? 0 : statisticsComponent.health - 1;
                bodyComponent.body.setTransform(savePointComponent.position, 0);
            }
        }
    }

    private void endCollision(CollisionComponent collisionComponent, Entity entity) {
        if (collisionComponent.lastCollidedEntity.getComponent(EntityTypeComponent.class) != null)  {
            EntityTypeComponent entityTypeComponent = collisionComponent.lastCollidedEntity.getComponent(EntityTypeComponent.class);
            if(entityTypeComponent.type == ECSConstants.EntityType.SENSOR_INFO) {
                gameplayInfo.getInformationButton().setVisible(false);
            } else if (entityTypeComponent.type == ECSConstants.EntityType.SENSOR_EXIT) {
                gameplayInfo.getInformationButton().setVisible(false);
            }
        }
    }
}
