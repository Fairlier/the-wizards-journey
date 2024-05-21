package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class CollisionComponent implements Component {
    public Entity firstCollidedEntity;
    public Entity lastCollidedEntity;
}
