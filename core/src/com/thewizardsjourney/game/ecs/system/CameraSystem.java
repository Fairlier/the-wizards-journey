package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.thewizardsjourney.game.ecs.component.TransformComponent;

public class CameraSystem extends EntitySystem {
    private Entity entity;
    private final OrthographicCamera camera;

    public CameraSystem(OrthographicCamera camera) { // TODO
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        if (transformComponent != null) {
            camera.position.set(transformComponent.position.x, transformComponent.position.y, 0);
        }
    }

    public void setTargetEntity(Entity entity) {
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        if (transformComponent != null) {
            this.entity = entity;
        }
    }
}
