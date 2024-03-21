package com.thewizardsjourney.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class JumpComponent implements Component {
    public float speed;
    public final Vector2 velocity = new Vector2();
}
