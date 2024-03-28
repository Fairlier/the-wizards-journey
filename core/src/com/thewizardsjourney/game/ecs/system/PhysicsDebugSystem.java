package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PhysicsDebugSystem extends EntitySystem {
    private final World world;
    private final Viewport viewport;
    private final Box2DDebugRenderer debugRenderer;

    public PhysicsDebugSystem(World world, Viewport viewport) {
        this.world = world;
        this.viewport = viewport;
        debugRenderer = new Box2DDebugRenderer(
                true,
                true,
                false,
                true,
                true,
                true
        );
    }

    @Override
    public void update(float deltaTime) {
        debugRenderer.render(world, viewport.getCamera().combined);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        world.dispose();
        debugRenderer.dispose();
    }
}
