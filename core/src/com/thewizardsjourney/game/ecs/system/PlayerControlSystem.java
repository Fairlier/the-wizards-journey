package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.thewizardsjourney.game.constant.ECSConstants.FacingDirection;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.component.PlayerAbilityComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.PlayerMovementComponent;

public class PlayerControlSystem extends IteratingSystem {
    private final InputHandler controller;
    private final ComponentMapper<PlayerMovementComponent> playerMovementComponentCM =
            ComponentMapper.getFor(PlayerMovementComponent.class);
    private final ComponentMapper<FacingComponent> facingComponentCM =
            ComponentMapper.getFor(FacingComponent.class);
    private final ComponentMapper<PlayerAbilityComponent> abilityComponentCM =
            ComponentMapper.getFor(PlayerAbilityComponent.class);

    public PlayerControlSystem(InputHandler controller) {
        super(Family.all(
                PlayerMovementComponent.class,
                PlayerAbilityComponent.class,
                FacingComponent.class,
                PlayerComponent.class
        ).get());
        this.controller = controller;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FacingComponent facingComponent = facingComponentCM.get(entity);
        PlayerMovementComponent playerMovementComponent  = playerMovementComponentCM.get(entity);
        PlayerAbilityComponent playerAbilityComponent = abilityComponentCM.get(entity);

        if (controller.isLeft()) {
            playerMovementComponent.isRunning = true;
            playerMovementComponent.runVelocity.x = -playerMovementComponent.runSpeed;
            facingComponent.direction = FacingDirection.LEFT;
        } else if (controller.isRight()) {
            playerMovementComponent.isRunning = true;
            playerMovementComponent.runVelocity.x = playerMovementComponent.runSpeed;
            facingComponent.direction = FacingDirection.RIGHT;
        } else {
            playerMovementComponent.isRunning = false;
            playerMovementComponent.runVelocity.setZero();
        }

        if (controller.isJump()) {
            playerMovementComponent.isJumping = true;
            playerMovementComponent.jumpVelocity.y = playerMovementComponent.jumpSpeed;
        } else {
            playerMovementComponent.isJumping = false;
            playerMovementComponent.jumpVelocity.setZero();
        }

        if (controller.isAbility()) {
            playerAbilityComponent.isInAbilityMode = true;
            if (controller.isCast()) {
                playerAbilityComponent.isCasting = true;
            } else {
                playerAbilityComponent.isCasting = false;
            }
        } else {
            playerAbilityComponent.isInAbilityMode = false;
        }
    }
}
