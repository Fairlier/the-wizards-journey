package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.thewizardsjourney.game.constant.ECS.PlayerStateType;

public class PlayerComponent implements Component {
    public PlayerStateType playerStateType = PlayerStateType.IDLE;
}
