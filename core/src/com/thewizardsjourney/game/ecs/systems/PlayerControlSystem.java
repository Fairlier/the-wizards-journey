package com.thewizardsjourney.game.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.thewizardsjourney.game.constants.Constants;
import com.thewizardsjourney.game.controllers.InputHandler;
import com.thewizardsjourney.game.ecs.components.FacingComponent;
import com.thewizardsjourney.game.ecs.components.JumpComponent;
import com.thewizardsjourney.game.ecs.components.MovementComponent;
import com.thewizardsjourney.game.ecs.components.PlayerComponent;
import com.thewizardsjourney.game.ecs.components.StateTypeComponent;

public class PlayerControlSystem extends IteratingSystem {
    private InputHandler controller;
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
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movementComponent = mm.get(entity);
        JumpComponent jumpComponent = jm.get(entity);
        FacingComponent facingComponent = fm.get(entity);
        StateTypeComponent stateTypeComponent = stm.get(entity);
        PlayerComponent playerComponent = pm.get(entity);

        if (controller.isLeft()) {
            movementComponent.velocity.x = -movementComponent.speed;
            facingComponent.direction = Constants.FacingDirection.LEFT;
            stateTypeComponent.stateType = Constants.StateType.RUN;
        } else if (controller.isRight()) {
            movementComponent.velocity.x = movementComponent.speed;
            facingComponent.direction = Constants.FacingDirection.RIGHT;
            stateTypeComponent.stateType = Constants.StateType.RUN;
        } else {
            movementComponent.velocity.x = 0.0f;
            stateTypeComponent.stateType = Constants.StateType.IDLE;
        }

        if (controller.isJump()) {
            jumpComponent.velocity.y = jumpComponent.speed;
            stateTypeComponent.stateType = Constants.StateType.JUMP;
        } else {
            jumpComponent.velocity.y = 0.0f;
            stateTypeComponent.stateType = Constants.StateType.FALLING;
        }
    }
}
