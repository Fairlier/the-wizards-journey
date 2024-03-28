package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.thewizardsjourney.game.constant.ECS.FacingDirection;

public class FacingComponent implements Component {
    public FacingDirection direction = FacingDirection.RIGHT;
}
