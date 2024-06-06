package com.thewizardsjourney.game.ecs.system;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.UNIT_SCALE;

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
    private final Vector2 maxCameraPosition = new Vector2();

    public CameraSystem(OrthographicCamera camera, TiledMap map) {
        this.camera = camera;

        float mapWidth = map.getProperties().get("width", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class);
        float tileSize = map.getProperties().get("tilewidth", Integer.class);

        float mapWidthInPixels = mapWidth * tileSize * UNIT_SCALE;
        float mapHeightInPixels = mapHeight * tileSize * UNIT_SCALE;

        if (mapWidthInPixels >= camera.viewportWidth && mapHeightInPixels >= camera.viewportHeight) {
            maxCameraPosition.set(mapWidthInPixels, mapHeightInPixels);
        } else {
            maxCameraPosition.set(0, 0);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (entity != null) {
            TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
            if (transformComponent != null) {
                float camW = camera.viewportWidth * 0.5f;
                float camH = camera.viewportHeight * 0.5f;
                Vector2 entityPos = transformComponent.position;

                camera.position.set(
                        MathUtils.clamp(entityPos.x, camW, maxCameraPosition.x - camW),
                        MathUtils.clamp(entityPos.y, camH, maxCameraPosition.y - camH),
                        0
                );
                camera.update();
            }
        }
    }

    public void setTargetEntity(Entity entity) {
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        if (transformComponent != null) {
            this.entity = entity;
        }
    }
}
