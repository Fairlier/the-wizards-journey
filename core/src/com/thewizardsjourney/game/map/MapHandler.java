package com.thewizardsjourney.game.map;

import static com.thewizardsjourney.game.constant.Asset.AssetGroups.General.GROUP_NAME;
import static com.thewizardsjourney.game.constant.Asset.AssetGroups.General.MATERIALS;
import static com.thewizardsjourney.game.constant.Asset.AssetGroups.Maps;
import static com.thewizardsjourney.game.constant.Asset.TiledMapDefinitions.LN_OTHER_OBJECTS;
import static com.thewizardsjourney.game.constant.Asset.TiledMapDefinitions.LN_STATIC_OBJECTS;
import static com.thewizardsjourney.game.constant.Asset.TiledMapDefinitions.MP_MATERIAL;
import static com.thewizardsjourney.game.constant.Asset.TiledMapDefinitions.MP_MATERIAL_DEFAULT;
import static com.thewizardsjourney.game.constant.Asset.TiledMapDefinitions.OB_PLAYER;
import static com.thewizardsjourney.game.constant.General.Screens.SCENE_HEIGHT;
import static com.thewizardsjourney.game.constant.General.Screens.SCENE_WIDTH;
import static com.thewizardsjourney.game.constant.General.Physics.GRAVITY;
import static com.thewizardsjourney.game.constant.General.Screens.UNITS;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.asset.material.MaterialsData;
import com.thewizardsjourney.game.constant.ECS;
import com.thewizardsjourney.game.constant.General;
import com.thewizardsjourney.game.ecs.component.AbilityComponent;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;
import com.thewizardsjourney.game.ecs.component.MovementComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.TransformComponent;
import com.thewizardsjourney.game.helper.GameInfo;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;

public class MapHandler {
    // TODO
    private float units = UNITS;
    private Logger logger; // ?
    private Engine engine;
    private AssetsHandler assetsHandler;
    private TiledMap map;
    private World world;
    private RayHandler rayHandler;
    private Array<Body> bodies = new Array<>();
    private ObjectMap<String, FixtureDef> materials;

    public MapHandler(Engine engine, AssetsHandler assetsHandler, GameInfo gameInfo) {
        logger = new Logger("MapHandler", Logger.INFO);
        logger.info("initialising");

        this.engine = engine;
        this.assetsHandler = assetsHandler;

        world = new World(GRAVITY, true);

        BodyContactListener bodyContactListener = new BodyContactListener();
        world.setContactListener(bodyContactListener);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 0.25f);

        Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 15, SCENE_WIDTH*0.2f, SCENE_HEIGHT, 270, 45);

        map = assetsHandler.get(Maps.GROUP_NAME, gameInfo.getSelectedMapName());
        materials = ((MaterialsData) assetsHandler.get(GROUP_NAME, MATERIALS)).getMaterials();

        createObjects(map, LN_STATIC_OBJECTS, this::createStaticObject);
        createObjects(map, LN_OTHER_OBJECTS, this::createOtherObject);
    }

    public void dispose() {
        for (Body body : bodies) {
            world.destroyBody(body);
        }
        bodies.clear();
        world.dispose();
    }

    private void createObjects(Map map, String layerName, ObjectCreationHandler handler) {
        MapLayer layer = map.getLayers().get(layerName);
        if (layer == null) {
            logger.error("layer " + layerName + " does not exist");
            return;
        }
        Array<MapObjectData> objectsData = new Array<>();
        for (MapObject object : layer.getObjects()) {
            if (object instanceof TextureMapObject){
                continue;
            }
            MapObjectData objectData;
            if (object instanceof RectangleMapObject) {
                objectData = getRectangle((RectangleMapObject) object);
                logger.info("RectangleMapObject " + object);
            }  else if (object instanceof PolygonMapObject) {
                objectData = getPolygon((PolygonMapObject) object);
                logger.info("PolygonMapObject " + object);
            }  else if (object instanceof PolylineMapObject) {
                objectData = getPolyline((PolylineMapObject) object);
                logger.info("PolylineMapObject " + object);
            } else {
                logger.error("non suported shape " + object);
                continue;
            }
            objectsData.add(objectData);
        }
        handler.createObject(objectsData);
        for (MapObjectData objectData : objectsData) {
            if (objectData != null) {
                if (objectData.getShape() != null) {
                    objectData.getShape().dispose();
                }
            }
        }
    }

    private void createStaticObject(Array<MapObjectData> objectsData) {
        for (MapObjectData objectData : objectsData) {
            MapProperties properties = objectData.getObject().getProperties();
            String material = properties.get(MP_MATERIAL, MP_MATERIAL_DEFAULT, String.class);

            FixtureDef fixtureDef = materials.get(material);
            if (fixtureDef == null) {
                logger.error("material does not exist " + material + " using default");
                fixtureDef = materials.get(MP_MATERIAL_DEFAULT);
            }
            fixtureDef.shape = objectData.getShape();

            BodyDef bodyDef = objectData.getBodyDef();
            bodyDef.position.setZero();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);

            createEntityForStaticObject(body);
            bodies.add(body);

            fixtureDef.shape = null;
        }
    }

    private void createEntityForStaticObject(Body body) {
        Entity entity = engine.createEntity();

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ECS.EntityType.STATIC_OBJECT;
        entity.add(entityTypeComponent);

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(entity);
        entity.add(bodyComponent);

        engine.addEntity(entity);
    }

    private void createOtherObject(Array<MapObjectData> objectsData) {
        for (MapObjectData objectData : objectsData) {
            if (objectData.getObject().getName().equals(OB_PLAYER)) {
                createPlayerObject(objectData);
            }
        }
    }

    private void createPlayerObject(MapObjectData objectData) {
        MapProperties properties = objectData.getObject().getProperties();
        String material = properties.get(MP_MATERIAL, MP_MATERIAL_DEFAULT, String.class);

        FixtureDef fixtureDef = materials.get(material);
        if (fixtureDef == null) {
            logger.error("material does not exist " + material + " using default");
            fixtureDef = materials.get(MP_MATERIAL_DEFAULT);
        }
        fixtureDef.shape = objectData.getShape();

        BodyDef bodyDef = objectData.getBodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.getFixtureList().get(0).setUserData(General.Categories.PLAYER);

        createEntityForPlayerObject(body);
        bodies.add(body);

        fixtureDef.shape = null;
    }

    private void createEntityForPlayerObject(Body body) {
        Entity entity = engine.createEntity();

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ECS.EntityType.PLAYER;
        entity.add(entityTypeComponent);

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(entity);
        entity.add(bodyComponent);

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.position.set(body.getPosition());
        entity.add(transformComponent);

        MovementComponent movementComponent = engine.createComponent(MovementComponent.class);
        movementComponent.speed = 8.0f;
        entity.add(movementComponent);

        JumpComponent jumpComponent = engine.createComponent(JumpComponent.class);
        jumpComponent.speed = 2.0f;
        entity.add(jumpComponent);

        FacingComponent facingComponent = engine.createComponent(FacingComponent.class);
        entity.add(facingComponent);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        entity.add(animationComponent);

        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        entity.add(collisionComponent);

        AbilityComponent abilityComponent = engine.createComponent(AbilityComponent.class);
        abilityComponent.state = true;
        entity.add(abilityComponent);

        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        entity.add(playerComponent);

        engine.addEntity(entity);
    }

    private MapObjectData getRectangle(RectangleMapObject object) {
        Rectangle rectangle = object.getRectangle();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                rectangle.width * 0.5f / units,
                rectangle.height * 0.5f / units);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(
                (rectangle.x + rectangle.width * 0.5f) / units,
                (rectangle.y + rectangle.height * 0.5f)  / units);
        return new MapObjectData(object, shape, bodyDef);
    }

    private MapObjectData getPolygon(PolygonMapObject object) {
        PolygonShape shape = new PolygonShape();
        float[] vertices = object.getPolygon().getTransformedVertices();
        float[] worldVertices = new float[vertices.length];
        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / units;
        }
        shape.set(worldVertices);
        BodyDef bodyDef = new BodyDef();
        return new MapObjectData(object, shape, bodyDef);
    }

    private MapObjectData getPolyline(PolylineMapObject object) {
        float[] vertices = object.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2(
                    vertices[i * 2] / units,
                    vertices[i * 2 + 1] / units);
        }
        ChainShape shape = new ChainShape();
        shape.createChain(worldVertices);
        BodyDef bodyDef = new BodyDef();
        return new MapObjectData(object, shape, bodyDef);
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    private interface ObjectCreationHandler {
        void createObject(Array<MapObjectData> objectsData);
    }
}
