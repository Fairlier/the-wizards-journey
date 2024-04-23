package com.thewizardsjourney.game.map;

import static com.thewizardsjourney.game.constant.Asset.AssetGroups.MapSettings;
import static com.thewizardsjourney.game.constant.Asset.AssetGroups.MapList;
import static com.thewizardsjourney.game.constant.Asset.MapData.LN_OTHER_OBJECTS;
import static com.thewizardsjourney.game.constant.Asset.MapData.LN_STATIC_OBJECTS;
import static com.thewizardsjourney.game.constant.Asset.MapData.MP_MATERIAL;
import static com.thewizardsjourney.game.constant.Asset.MapData.MP_MATERIAL_DEFAULT;
import static com.thewizardsjourney.game.constant.Asset.MapData.OB_PLAYER;
import static com.thewizardsjourney.game.constant.General.CollisionFilters.CATEGORY_PLAYER;
import static com.thewizardsjourney.game.constant.General.CollisionFilters.CATEGORY_STATIC_OBJECT;
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
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.asset.material.MaterialsData;
import com.thewizardsjourney.game.constant.ECS;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;
import com.thewizardsjourney.game.ecs.component.MovementComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.TransformComponent;
import com.thewizardsjourney.game.helper.GameData;

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

    public MapHandler(Engine engine, AssetsHandler assetsHandler, GameData gameData) {
        logger = new Logger("MapHandler", Logger.INFO);
        logger.info("initialising");

        this.engine = engine;
        this.assetsHandler = assetsHandler;

        world = new World(GRAVITY, true);

        BodyContactListener bodyContactListener = new BodyContactListener();
        world.setContactListener(bodyContactListener);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.25f);

        Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 15, SCENE_WIDTH*0.2f, SCENE_HEIGHT, 270, 45);

        map = assetsHandler.get(MapList.GROUP_NAME, gameData.getSelectedMapName());
        materials = ((MaterialsData) assetsHandler.get(
                MapSettings.GROUP_NAME, MapSettings.MATERIALS)
        ).getMaterials();

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
        for (MapObject object : layer.getObjects()) {
            if (object instanceof TextureMapObject){
                continue;
            }
            ObjectData objectData;
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

            Shape shape = objectData.getShape();
            BodyDef bodyDef = objectData.getBodyDef();

            MapProperties properties = object.getProperties();
            String material = properties.get(MP_MATERIAL, MP_MATERIAL_DEFAULT, String.class);
            logger.info("Body: " + object.getName() + ", material: " + material);

            handler.createObject(object, shape, bodyDef, material);

            shape.dispose();
        }
    }

    private void createStaticObject(MapObject object, Shape shape, BodyDef bodyDef, String material) {
        FixtureDef fixtureDef = materials.get(material);

        if (fixtureDef == null) {
            logger.error("material does not exist " + material + " using default");
            fixtureDef = materials.get(MP_MATERIAL_DEFAULT);
        }

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_STATIC_OBJECT;
        bodyDef.position.setZero();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        createEntityForStaticObject(body);
        bodies.add(body);

        fixtureDef.shape = null;
    }

    private void createEntityForStaticObject(Body body) {
        Entity entity = engine.createEntity();

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.entityType = ECS.EntityType.STATIC_OBJECT;
        entity.add(entityTypeComponent);

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(entity);
        entity.add(bodyComponent);

        engine.addEntity(entity);
    }

    private void createOtherObject(MapObject object, Shape shape, BodyDef bodyDef, String material) {
        String name = object.getName();
        if (name.equals(OB_PLAYER)) {
            createPlayerObject(shape, bodyDef, material);
        }
    }

    private void createPlayerObject(Shape shape, BodyDef bodyDef, String material) {
        FixtureDef fixtureDef = materials.get(material);

        if (fixtureDef == null) {
            logger.error("material does not exist " + material + " using default");
            fixtureDef = materials.get(MP_MATERIAL_DEFAULT);
        }

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_PLAYER;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        createEntityForPlayerObject(body);
        bodies.add(body);

        fixtureDef.shape = null;
    }

    private void createEntityForPlayerObject(Body body) {
        Entity entity = engine.createEntity();

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.entityType = ECS.EntityType.PLAYER;
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

        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        entity.add(collisionComponent);

        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        entity.add(playerComponent);

        engine.addEntity(entity);
    }

    private ObjectData getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                rectangle.width * 0.5f / units,
                rectangle.height * 0.5f / units);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(
                (rectangle.x + rectangle.width * 0.5f) / units,
                (rectangle.y + rectangle.height * 0.5f)  / units);
        return new ObjectData(shape, bodyDef);
    }

    private ObjectData getPolygon(PolygonMapObject polygonObject) {
        PolygonShape shape = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();
        float[] worldVertices = new float[vertices.length];
        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / units;
        }
        shape.set(worldVertices);
        BodyDef bodyDef = new BodyDef();
        return new ObjectData(shape, bodyDef);
    }

    private ObjectData getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2(
                    vertices[i * 2] / units,
                    vertices[i * 2 + 1] / units);
        }
        ChainShape shape = new ChainShape();
        shape.createChain(worldVertices);
        BodyDef bodyDef = new BodyDef();
        return new ObjectData(shape, bodyDef);
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
        void createObject(MapObject object, Shape shape, BodyDef bodyDef, String material);
    }

    private static class ObjectData {
        private final Shape shape;
        private final BodyDef bodyDef;

        public ObjectData(Shape shape, BodyDef bodyDef) {
            this.shape = shape;
            this.bodyDef = bodyDef;
        }

        public Shape getShape() {
            return shape;
        }

        public BodyDef getBodyDef() {
            return bodyDef;
        }
    }
}
