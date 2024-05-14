package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.thewizardsjourney.game.ecs.component.PuzzleSensorComponent;
import com.thewizardsjourney.game.helper.EntityTypeInfo;

import java.util.Objects;

public class PuzzleSensorSystem extends IteratingSystem {
    private boolean isColliding;
    private final World world;
    private String targetObjectName;
    private final ComponentMapper<PuzzleSensorComponent> puzzleSensorComponentCM =
            ComponentMapper.getFor(PuzzleSensorComponent.class);

    public PuzzleSensorSystem(World world) {
        super(Family.all(PuzzleSensorComponent.class).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PuzzleSensorComponent puzzleSensorComponent = puzzleSensorComponentCM.get(entity);
        isColliding = false;
        targetObjectName = puzzleSensorComponent.targetObjectName;
        collisionCheck(puzzleSensorComponent.lowerBound, puzzleSensorComponent.upperBound);
        if (isColliding) {
            System.out.println("COLLISION");
        }
    }

    private void collisionCheck(Vector2 lowerBound, Vector2 upperBound) { // TODO константы?
        world.QueryAABB(callback, lowerBound.x, lowerBound.y, upperBound.x, upperBound.y);
    }

    private final QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (Objects.equals(((EntityTypeInfo) fixture.getUserData()).getObjectCategoryName(), targetObjectName)) { // TODO
                isColliding = true;
            }
            return true;
        }
    };
}
