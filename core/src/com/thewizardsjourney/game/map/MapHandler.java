package com.thewizardsjourney.game.map;

import static com.thewizardsjourney.game.constant.Asset.AssetGroups.MapSettings;
import static com.thewizardsjourney.game.constant.General.MapHandler.GRAVITY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.asset.material.MaterialsData;
import com.thewizardsjourney.game.constant.Asset;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;
import com.thewizardsjourney.game.ecs.component.MovementComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.StateTypeComponent;
import com.thewizardsjourney.game.ecs.component.TransformComponent;
import com.thewizardsjourney.game.helper.GameData;

import box2dLight.RayHandler;

public class MapHandler {
    // TODO
    private float units = 30.0f;
    private Logger logger; // ?
    private Engine engine;
    private AssetsHandler assetsHandler;
    private TiledMap map;
    private World world;
    private RayHandler rayHandler;
    private Array<Body> bodies = new Array<>();
    private ObjectMap<String, FixtureDef> materials;

    public MapHandler(Engine engine, AssetsHandler assetsHandler, GameData gameData) {
        logger = new Logger("MapHandler", Logger.INFO);
        logger.info("initialising");

        this.engine = engine;
        this.assetsHandler = assetsHandler;

        world = new World(GRAVITY, true);
        rayHandler = new RayHandler(world);

        map = assetsHandler.get(Asset.AssetGroups.MapList.GROUP_NAME, gameData.getSelectedMapName());
        materials = ((MaterialsData) assetsHandler.get(MapSettings.GROUP_NAME, MapSettings.MATERIALS)).getMaterials();
        createPhysics(map, "physics");
        createPlayer(map, "playerSpawnLocation");
    }

    private void createPhysics(Map map, String layerName) {
        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) {
            logger.error("layer " + layerName + " does not exist");
            return;
        }
        for (MapObject object : layer.getObjects()) {
            if (object instanceof TextureMapObject){
                continue;
            }
            Shape shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangle = (RectangleMapObject) object;
                shape = getRectangle(rectangle);
                logger.info("RectangleMapObject " + object);
            }  else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject) object);
                logger.info("PolygonMapObject " + object);
            }  else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
                logger.info("PolylineMapObject " + object);
            } else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject)object);
                logger.info("CircleMapObject " + object);
            } else {
                logger.error("non suported shape " + object);
                continue;
            }
            MapProperties properties = object.getProperties();
            String material = properties.get("material", "default", String.class);
            FixtureDef fixtureDef = materials.get(material);
            if (fixtureDef == null) {
                logger.error("material does not exist " + material + " using default");
                fixtureDef = materials.get("default");
            }
            logger.info("Body: " + object.getName() + ", material: " + material);

            fixtureDef.shape = shape;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);

            bodies.add(body);

            BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
            bodyComponent.body = body;
            TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
            transformComponent.position.set(bodyComponent.body.getPosition());
            Entity entity = engine.createEntity();
            entity.add(bodyComponent);
            entity.add(transformComponent);
            engine.addEntity(entity);

            fixtureDef.shape = null;
            shape.dispose();
        }
    }

    private void createPlayer(Map map, String layerName) {
    }

    public void dispose() {
        for (Body body : bodies) {
            world.destroyBody(body);
        }
        bodies.clear();
        world.dispose();
    }

    private Shape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / units,
                (rectangle.y + rectangle.height * 0.5f ) / units);
        polygon.setAsBox(rectangle.width * 0.5f / units,
                rectangle.height * 0.5f / units,
                size,
                0.0f);
        return polygon;
    }

    private Shape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / units);
        circleShape.setPosition(new Vector2(circle.x / units, circle.y / units));
        return circleShape;
    }

    private Shape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / units;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private Shape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / units;
            worldVertices[i].y = vertices[i * 2 + 1] / units;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }
}
