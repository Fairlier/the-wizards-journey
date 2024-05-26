package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.StatisticsComponent;
import com.thewizardsjourney.game.helper.GameplayInfo;

public class PlayerStatisticsSystem extends IteratingSystem {
    private float previousHealth = -1;
    private float previousEnergy = -1;
    private final GameplayInfo gameplayInfo;
    private final ComponentMapper<StatisticsComponent> statisticsComponentCM =
            ComponentMapper.getFor(StatisticsComponent.class);
    private final ComponentMapper<PlayerComponent> playerComponentCM =
            ComponentMapper.getFor(PlayerComponent.class);

    public PlayerStatisticsSystem(GameplayInfo gameplayInfo) {
        super(Family.all(
                StatisticsComponent.class,
                PlayerComponent.class
        ).get());
        this.gameplayInfo = gameplayInfo;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatisticsComponent statisticsComponent = statisticsComponentCM.get(entity);
        if (previousHealth != statisticsComponent.health) {
            gameplayInfo.getPlayerStatisticsWidget().setHealth(statisticsComponent.health, statisticsComponent.maxHealth);
            previousHealth = statisticsComponent.health;
        }
        if (previousEnergy != statisticsComponent.energy) {
            gameplayInfo.getPlayerStatisticsWidget().setEnergy(statisticsComponent.energy, statisticsComponent.maxEnergy);
            previousEnergy = statisticsComponent.energy;
        }

        if (statisticsComponent.health <= 0) {
            gameplayInfo.getGameHUD().gameOver();
        }
    }
}
