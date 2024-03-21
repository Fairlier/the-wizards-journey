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
    private final Array<Entity> entities;
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
        entities = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        float frameTime = Math.min(deltaTime, MAX_FRAME_TIME);
        accumulator += frameTime;
        while (accumulator >= FIXED_STEP_TIME) {
            world.step(FIXED_STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= FIXED_STEP_TIME;
            test(accumulator / FIXED_STEP_TIME);
        }
        entities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        world.dispose();
    }

    private void interpolateEntities(float alpha) {
        for (Entity entity : entities) {
            BodyComponent bodyComponent = bm.get(entity);
            if (!bodyComponent.body.isActive()) continue;
            Transform bodyTransform = bodyComponent.body.getTransform();
            Vector2 bodyPosition = bodyTransform.getPosition();
            TransformComponent transformComponent = tm.get(entity);
            transformComponent.position.x = bodyPosition.x * alpha +
                    transformComponent.position.x * (1.0f - alpha);
            transformComponent.position.y = bodyPosition.y * alpha +
                    transformComponent.position.y * (1.0f - alpha);
            transformComponent.rotation = bodyTransform.getRotation() * alpha +
                    transformComponent.rotation * (1.0f - alpha);
        }
    }

    private void test(float alpha) {
        for (Entity entity : entities) {
            BodyComponent bodyComponent = bm.get(entity);
            if (!bodyComponent.body.isActive()) continue;
            Transform bodyTransform = bodyComponent.body.getTransform();
            Vector2 bodyPosition = bodyTransform.getPosition();
            TransformComponent transformComponent = tm.get(entity);
            transformComponent.position.x = MathUtils.lerp(
                    transformComponent.position.x, bodyPosition.x, alpha);
            transformComponent.position.y = MathUtils.lerp(
                    transformComponent.position.y, bodyPosition.y, alpha);
            transformComponent.rotation = MathUtils.lerp(
                    transformComponent.rotation, bodyTransform.getRotation(), alpha);
        }
    }
}
