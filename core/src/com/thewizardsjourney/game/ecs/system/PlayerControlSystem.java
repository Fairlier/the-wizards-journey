package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.thewizardsjourney.game.constant.ECS;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;
import com.thewizardsjourney.game.ecs.component.MovementComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.StateTypeComponent;

public class PlayerControlSystem extends IteratingSystem {
    private final InputHandler controller;
    private final ComponentMapper<MovementComponent> mm =
            ComponentMapper.getFor(MovementComponent.class);
    private final ComponentMapper<JumpComponent> jm =
            ComponentMapper.getFor(JumpComponent.class);
    private final ComponentMapper<FacingComponent> fm =
            ComponentMapper.getFor(FacingComponent.class);
    private final ComponentMapper<StateTypeComponent> stm =
            ComponentMapper.getFor(StateTypeComponent.class);
    private final ComponentMapper<PlayerComponent> pm =
            ComponentMapper.getFor(PlayerComponent.class);

    public PlayerControlSystem(InputHandler controller) {
        super(Family.all(
                MovementComponent.class,
                JumpComponent.class,
                FacingComponent.class,
                StateTypeComponent.class,
                PlayerComponent.class
        ).get());
        this.controller = controller;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) { // TODO
        MovementComponent movementComponent = mm.get(entity);
        JumpComponent jumpComponent = jm.get(entity);
        FacingComponent facingComponent = fm.get(entity);
        StateTypeComponent stateTypeComponent = stm.get(entity);
        PlayerComponent playerComponent = pm.get(entity);

        if (controller.isLeft()) {
            movementComponent.velocity.x = -movementComponent.speed;
            facingComponent.direction = ECS.FacingDirection.LEFT;
            stateTypeComponent.stateType = ECS.StateType.RUN;
        }
        else if (controller.isRight()) {
            movementComponent.velocity.x = movementComponent.speed;
            facingComponent.direction = ECS.FacingDirection.RIGHT;
            stateTypeComponent.stateType = ECS.StateType.RUN;
        }
        else {
            movementComponent.velocity.setZero();
            stateTypeComponent.stateType = ECS.StateType.IDLE;
        }

        if (controller.isUp()) {
            jumpComponent.velocity.y = jumpComponent.speed;
            stateTypeComponent.stateType = ECS.StateType.JUMP;
        }
        else {
            jumpComponent.velocity.y = 0.0f;
            stateTypeComponent.stateType = ECS.StateType.FALLING;
        }
    }
}
