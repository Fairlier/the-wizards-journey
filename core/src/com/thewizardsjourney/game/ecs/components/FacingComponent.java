package com.thewizardsjourney.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.thewizardsjourney.game.constants.Constants.FacingDirection;

public class FacingComponent implements Component {
    public FacingDirection direction = FacingDirection.RIGHT;
}
