package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.MovementComponent;

public class MovementSystem extends IteratingSystem {
    private final ComponentMapper<BodyComponent> bm =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<MovementComponent> mm =
            ComponentMapper.getFor(MovementComponent.class);

    public MovementSystem() {
        super(Family.all(
                BodyComponent.class,
                MovementComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bm.get(entity);
        MovementComponent movementComponent  = mm.get(entity);
        bodyComponent.body.setLinearVelocity(
                movementComponent.velocity.x,
                bodyComponent.body.getLinearVelocity().y
        );
    }
}
