package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component, Comparable<TransformComponent> {
    public int z = 0; // TODO
    public float rotation = 0.0f;
    public final Vector2 position = new Vector2();
    public final Vector2 scale = new Vector2();

    @Override
    public int compareTo(TransformComponent other) {
        int zDifference = Integer.compare(other.z, z);
        return (zDifference == 0) ? Float.compare(other.position.y, position.y) : zDifference;
    }
}
