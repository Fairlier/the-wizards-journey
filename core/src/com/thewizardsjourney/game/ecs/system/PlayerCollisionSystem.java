package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thewizardsjourney.game.constant.ECS.EntityType;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;

public class PlayerCollisionSystem extends IteratingSystem {
    private final float EPSILON = 0.02f;
    private final ComponentMapper<BodyComponent> bm =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<CollisionComponent> cm =
            ComponentMapper.getFor(CollisionComponent.class);

    private final ComponentMapper<JumpComponent> jm =
            ComponentMapper.getFor(JumpComponent.class);

    private final ComponentMapper<PlayerComponent> pm =
            ComponentMapper.getFor(PlayerComponent.class);

    public PlayerCollisionSystem() {
        super(Family.all(
                CollisionComponent.class,
                JumpComponent.class,
                PlayerComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) { // TODO
        BodyComponent bodyComponent = bm.get(entity);
        CollisionComponent collisionComponent = cm.get(entity);
        JumpComponent jumpComponent = jm.get(entity);
        PlayerComponent playerComponent = pm.get(entity);

        if (collisionComponent.firstCollidedEntity != null && collisionComponent.lastCollidedEntity == null) {
            beginCollision(bodyComponent, collisionComponent, jumpComponent, playerComponent);
        }

        if (collisionComponent.firstCollidedEntity != null && collisionComponent.lastCollidedEntity == null) {
            endCollision(bodyComponent, collisionComponent, jumpComponent, playerComponent);
        }
    }

    private void beginCollision(BodyComponent bodyComponent,
                                CollisionComponent collisionComponent,
                                JumpComponent jumpComponent,
                                PlayerComponent playerComponent) { // TODO
        if (collisionComponent.firstCollidedEntity.getComponent(EntityTypeComponent.class)
                .entityType == EntityType.STATIC_OBJECT) {
            System.out.println();
            }
    }

    private void endCollision(BodyComponent bodyComponent,
                              CollisionComponent collisionComponent,
                              JumpComponent jumpComponent,
                              PlayerComponent playerComponent) {

    }
}
