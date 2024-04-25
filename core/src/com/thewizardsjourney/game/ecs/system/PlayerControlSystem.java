package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.thewizardsjourney.game.constant.ECS.PlayerStateType;
import com.thewizardsjourney.game.constant.ECS.FacingDirection;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;
import com.thewizardsjourney.game.ecs.component.MovementComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;

public class PlayerControlSystem extends IteratingSystem {
    private final InputHandler controller;
    private final ComponentMapper<MovementComponent> mm =
            ComponentMapper.getFor(MovementComponent.class);
    private final ComponentMapper<JumpComponent> jm =
            ComponentMapper.getFor(JumpComponent.class);
    private final ComponentMapper<FacingComponent> fm =
            ComponentMapper.getFor(FacingComponent.class);
    private final ComponentMapper<PlayerComponent> pm =
            ComponentMapper.getFor(PlayerComponent.class);

    public PlayerControlSystem(InputHandler controller) {
        super(Family.all(
                MovementComponent.class,
                JumpComponent.class,
                FacingComponent.class,
                PlayerComponent.class
        ).get());
        this.controller = controller;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) { // TODO
        MovementComponent movementComponent = mm.get(entity);
        JumpComponent jumpComponent = jm.get(entity);
        FacingComponent facingComponent = fm.get(entity);
        PlayerComponent playerComponent = pm.get(entity);

        if (controller.isLeft()) {
            movementComponent.velocity.x = -movementComponent.speed;
            facingComponent.direction = FacingDirection.LEFT;
            playerComponent.playerStateType = PlayerStateType.RUN;
        }
        else if (controller.isRight()) {
            movementComponent.velocity.x = movementComponent.speed;
            facingComponent.direction = FacingDirection.RIGHT;
            playerComponent.playerStateType = PlayerStateType.RUN;
        }
        else {
            movementComponent.velocity.setZero();
            playerComponent.playerStateType = PlayerStateType.IDLE;
        }

        if (controller.isUp()) {
            jumpComponent.state = true;
            jumpComponent.velocity.y = jumpComponent.speed;
            playerComponent.playerStateType = PlayerStateType.JUMP;
        }
        else {
            jumpComponent.state = false;
            jumpComponent.velocity.setZero();
            playerComponent.playerStateType = PlayerStateType.FALLING;
        }
    }
}
