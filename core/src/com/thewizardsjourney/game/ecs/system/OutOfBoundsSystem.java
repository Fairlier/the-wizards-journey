package com.thewizardsjourney.game.ecs.system;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.UNIT_SCALE;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.SavePointComponent;

public class OutOfBoundsSystem extends IteratingSystem {
    private final Rectangle boundaries;
    private final ComponentMapper<BodyComponent> bodyComponentCM =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<EntityTypeComponent> entityTypeComponentCM =
            ComponentMapper.getFor(EntityTypeComponent.class);
    private final ComponentMapper<SavePointComponent> savePointComponentCM =
            ComponentMapper.getFor(SavePointComponent.class);

    public OutOfBoundsSystem(TiledMap map) {
        super(Family.all(
                BodyComponent.class,
                EntityTypeComponent.class,
                SavePointComponent.class
        ).get());
        boundaries = new Rectangle();
        float mapWidth = map.getProperties().get("width", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class);
        float tileSize = map.getProperties().get("tilewidth", Integer.class);

        float newMapWidth = mapWidth * tileSize * UNIT_SCALE;
        float newMapHeight = mapHeight * tileSize * UNIT_SCALE;

        boundaries.set(0, 0, newMapWidth, newMapHeight);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bodyComponentCM.get(entity);
        EntityTypeComponent entityTypeComponent = entityTypeComponentCM.get(entity);
        SavePointComponent savePointComponent = savePointComponentCM.get(entity);

        if (!boundaries.contains(bodyComponent.body.getTransform().getPosition())) {
            if (entityTypeComponent.type == ECSConstants.EntityType.PLAYER) {
                bodyComponent.body.setTransform(savePointComponent.position, 0);
                bodyComponent.body.setLinearVelocity(0, 0);
            }
        }
    }
}
