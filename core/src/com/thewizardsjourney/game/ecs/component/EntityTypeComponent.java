package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.thewizardsjourney.game.constant.ECS.EntityType;

public class EntityTypeComponent implements Component {
    public EntityType type = EntityType.NONE;
}
