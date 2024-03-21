package com.thewizardsjourney.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.thewizardsjourney.game.constants.Constants.StateType;

public class StateTypeComponent implements Component {
    public StateType stateType = StateType.IDLE;
}
