package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;

public class JumpSystem extends IteratingSystem {
    private final ComponentMapper<BodyComponent> bm =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<JumpComponent> jm =
            ComponentMapper.getFor(JumpComponent.class);

    public JumpSystem() {
        super(Family.all(
                BodyComponent.class,
                JumpComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bm.get(entity);
        JumpComponent jumpComponent  = jm.get(entity);
        // TODO
        bodyComponent.body.applyLinearImpulse(
                0,
                jumpComponent.velocity.y * bodyComponent.body.getMass(),
                bodyComponent.body.getWorldCenter().x,
                bodyComponent.body.getWorldCenter().y,
                true
        );
    }
}
