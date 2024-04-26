package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.thewizardsjourney.game.constant.ECS;

public class AnimationComponent implements Component {
    public ECS.AnimationStateType stateType = ECS.AnimationStateType.IDLE;
}
