package com.thewizardsjourney.game.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {
    public int z = 0;
    public float rotation = 0.0f;
    public final Vector2 position = new Vector2();
    public final Vector2 scale = new Vector2();
}
