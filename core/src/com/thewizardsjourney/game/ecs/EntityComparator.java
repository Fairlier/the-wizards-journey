package com.thewizardsjourney.game.ecs;

import com.badlogic.ashley.core.Entity;
import com.thewizardsjourney.game.ecs.component.TransformComponent;

import java.util.Comparator;

public class EntityComparator implements Comparator<Entity> {
    @Override
    public int compare(Entity entityA, Entity entityB) {
        return Integer.compare(entityA.getComponent(TransformComponent.class).z,
                entityB.getComponent(TransformComponent.class).z);
    }
}
