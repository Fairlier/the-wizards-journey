package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PlayerMovementComponent implements Component {
    public boolean isRunning;
    public boolean isJumping;
    public float runSpeed;
    public float jumpSpeed;
    public final Vector2 runVelocity = new Vector2();
    public final Vector2 jumpVelocity = new Vector2();
}
