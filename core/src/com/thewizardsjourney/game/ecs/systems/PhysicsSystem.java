package com.thewizardsjourney.game.ecs.systems;

import static com.thewizardsjourney.game.constants.Constants.PhysicsSystem.FIXED_STEP_TIME;
import static com.thewizardsjourney.game.constants.Constants.PhysicsSystem.MAX_FRAME_TIME;
import static com.thewizardsjourney.game.constants.Constants.PhysicsSystem.POSITION_ITERATIONS;
import static com.thewizardsjourney.game.constants.Constants.PhysicsSystem.VELOCITY_ITERATIONS;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.thewizardsjourney.game.ecs.components.BodyComponent;
import com.thewizardsjourney.game.ecs.components.TransformComponent;

public class PhysicsSystem extends IteratingSystem {
    private static float accumulator = 0.0f;
    private final World world;
    private final ComponentMapper<BodyComponent> bm =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<TransformComponent> tm =
            ComponentMapper.getFor(TransformComponent.class);

    public PhysicsSystem(World world) {
        super(Family.all(
                BodyComponent.class,
                TransformComponent.class
        ).get());
        this.world = world;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, MAX_FRAME_TIME);
        accumulator += frameTime;
        while (accumulator >= FIXED_STEP_TIME) {
            world.step(FIXED_STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= FIXED_STEP_TIME;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bm.get(entity);
        Transform bodyTransform = bodyComponent.body.getTransform();
        Vector2 bodyPosition = bodyTransform.getPosition();
        TransformComponent transformComponent = tm.get(entity);
        transformComponent.position.x = bodyPosition.x;
        transformComponent.position.y = bodyPosition.y;
        transformComponent.rotation = bodyTransform.getRotation();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        world.dispose();
    }
}
