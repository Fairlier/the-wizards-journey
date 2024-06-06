package com.thewizardsjourney.game.ecs.system;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.UNIT_SCALE;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.ecs.EntityComparator;
import com.thewizardsjourney.game.ecs.component.RenderingComponent;
import com.thewizardsjourney.game.ecs.component.TransformComponent;

public class RenderingSystem extends SortedIteratingSystem {
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Array<TiledMapTileLayer> mapBackgroundLayers = new Array<>();
    private final Array<TiledMapTileLayer> mapForegroundLayers = new Array<>();
    private final FloatArray mapParallaxValues = new FloatArray();
    private final ComponentMapper<RenderingComponent> renderComponentCM =
            ComponentMapper.getFor(RenderingComponent.class);
    private final ComponentMapper<TransformComponent> transformComponentCM =
            ComponentMapper.getFor(TransformComponent.class);


    public RenderingSystem(SpriteBatch batch, Viewport viewport, TiledMap map) {
        super(Family.all(
                RenderingComponent.class,
                TransformComponent.class
        ).get(), new EntityComparator());
        this.batch = batch;
        this.viewport = viewport;
        camera = (OrthographicCamera) viewport.getCamera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE, batch);

        MapLayers layers = map.getLayers();
        for (int i = 0; i < layers.size(); i++) {
            MapLayer layer = layers.get(i);
            if (layer instanceof TiledMapTileLayer && layer.isVisible()) {
                TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
                if (tiledLayer.getName().startsWith("background")) {
                    mapBackgroundLayers.add(tiledLayer);
                } else {
                    mapForegroundLayers.add(tiledLayer);
                }
                mapParallaxValues.add(tiledLayer.getProperties().get("parallax_value", 0.0f, Float.class));
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        batch.setColor(Color.WHITE);
        AnimatedTiledMapTile.updateAnimationBaseTime();
        forceSort();
        viewport.apply();
        mapRenderer.setView(camera);
        float parallaxMinWidth = camera.viewportWidth * 0.5f;
        for (int i = 0; i < mapBackgroundLayers.size; i++) {
            renderTileLayer(mapBackgroundLayers.get(i), i, parallaxMinWidth);
        }

        super.update(deltaTime);
        for (int i = 0; i < mapForegroundLayers.size; ++i) {
            renderTileLayer(mapForegroundLayers.get(i), i + mapBackgroundLayers.size, parallaxMinWidth);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RenderingComponent renderingComponent = renderComponentCM.get(entity);
        TransformComponent transformComponent = transformComponentCM.get(entity);
        if (renderingComponent.sprite.getTexture() == null) {
            return;
        }
        float width = renderingComponent.sprite.getWidth();
        float height = renderingComponent.sprite.getHeight();
        renderingComponent.sprite.setPosition(transformComponent.position.x - width * 0.5f,
                transformComponent.position.y - height * 0.5f);
        batch.begin();
        renderingComponent.sprite.draw(batch);
        batch.end();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        mapRenderer.dispose();
    }

    private void renderTileLayer(TiledMapTileLayer layer, int parallaxIndex, float minWidth) {
        float parallaxValue = mapParallaxValues.get(parallaxIndex);
        if (parallaxValue == 0.0f && camera.position.x < minWidth) {
            batch.begin();
            mapRenderer.renderTileLayer(layer);
            batch.end();
        } else {
            final float originalValue = camera.position.x;
            camera.position.x += (minWidth - camera.position.x) * parallaxValue;
            camera.update();
            mapRenderer.setView(camera);
            batch.begin();
            mapRenderer.renderTileLayer(layer);
            batch.end();
            camera.position.x = originalValue;
            camera.update();
            mapRenderer.setView(camera);
        }
    }
}
