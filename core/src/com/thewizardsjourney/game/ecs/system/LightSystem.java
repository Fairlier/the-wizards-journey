package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

import box2dLight.RayHandler;

public class LightSystem extends EntitySystem {
    private final RayHandler rayHandler;
    private final OrthographicCamera camera;

    public LightSystem(RayHandler rayHandler, OrthographicCamera camera) {
        this.rayHandler = rayHandler;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
    }
}
