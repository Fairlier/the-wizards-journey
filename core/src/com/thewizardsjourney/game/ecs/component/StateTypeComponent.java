package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.thewizardsjourney.game.constant.ECS.StateType;

public class StateTypeComponent implements Component {
    public StateType stateType = StateType.IDLE;
}
