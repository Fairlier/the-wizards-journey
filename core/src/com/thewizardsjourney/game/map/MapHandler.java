package com.thewizardsjourney.game.map;

import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.LN_OTHER_OBJECTS;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.LN_STATIC_OBJECTS;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.MP_MATERIAL;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.MP_MATERIAL_DEFAULT;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.OB_PLAYER;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_WIDTH;
import static com.thewizardsjourney.game.constant.GlobalConstants.Physics.GRAVITY;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.UNIT_SCALE;

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
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.constant.GlobalConstants;
import com.thewizardsjourney.game.ecs.component.AbilityComponent;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.PlayerMovementComponent;
import com.thewizardsjourney.game.ecs.component.RenderComponent;
import com.thewizardsjourney.game.ecs.component.StatisticsComponent;
import com.thewizardsjourney.game.ecs.component.TransformComponent;
import com.thewizardsjourney.game.helper.GameInfo;
import com.thewizardsjourney.game.helper.MapInfo;
import com.thewizardsjourney.game.helper.PlayerInfo;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;

public class MapHandler {
    // TODO
    private Logger logger; // ?
    private Engine engine;
    private World world;
    private RayHandler rayHandler;
    private Array<Body> bodies = new Array<>();
    private MapInfo mapInfo;
    private PlayerInfo playerInfo;
    private Entity player;
    private final AssetsHandler assetsHandler;

    public MapHandler(Engine engine, AssetsHandler assetsHandler, GameInfo gameInfo) {
        logger = new Logger("MapHandler", Logger.INFO);
        logger.info("initialising");

        this.engine = engine;

        world = new World(GRAVITY, true);

        BodyContactListener bodyContactListener = new BodyContactListener();
        world.setContactListener(bodyContactListener);

        rayHandler = new RayHandler(world);
        // TODO
       rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 0.25f);

        Light conelight = new ConeLight(rayHandler, 32, Color.WHITE, 15, VIRTUAL_WIDTH *0.2f, VIRTUAL_HEIGHT, 270, 45);

        this.assetsHandler = assetsHandler;

        mapInfo = new MapInfo(this.assetsHandler);
        playerInfo = new PlayerInfo(this.assetsHandler);

        player = null;

        if (!mapInfo.setMapInfo(gameInfo.getSelectedMapGroupName()) ||
            !playerInfo.setPlayerInfo(gameInfo.getSelectedPlayerGroupName())) {
            // TODO сменить экран на меню, то есть выдать ошибку
        }

        createObjects(mapInfo.getMap(), LN_STATIC_OBJECTS, this::createStaticObject);
        createObjects(mapInfo.getMap(), LN_OTHER_OBJECTS, this::createOtherObject);
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

            FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(material);
            if (fixtureDef == null) {
                logger.error("material does not exist " + material + " using default");
                fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
            }
            fixtureDef.shape = objectData.getShape();

            BodyDef bodyDef = objectData.getBodyDef();
            bodyDef.position.setZero();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);

            //createEntityForStaticObject(body);
            bodies.add(body);

            fixtureDef.shape = null;
        }
    }

    private void createEntityForStaticObject(Body body) {
        Entity entity = engine.createEntity();

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ECSConstants.EntityType.STATIC_OBJECT;
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

    private void createPlayerObject(MapObjectData objectData) { // TODO
        MapProperties properties = objectData.getObject().getProperties();
        String material = properties.get(MP_MATERIAL, MP_MATERIAL_DEFAULT, String.class);

        FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(material);
        if (fixtureDef == null) {
            logger.error("material does not exist " + material + " using default");
            fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
        }
        fixtureDef.shape = objectData.getShape();

        BodyDef bodyDef = objectData.getBodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.getFixtureList().get(0).setUserData(GlobalConstants.Categories.PLAYER);

        createEntityForPlayerObject(body);
        bodies.add(body);

        fixtureDef.shape = null;
    }

    private void createEntityForPlayerObject(Body body) { // TODO
        player = engine.createEntity();

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ECSConstants.EntityType.PLAYER;
        player.add(entityTypeComponent);

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(player);
        player.add(bodyComponent);

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.position.set(body.getPosition());
        player.add(transformComponent);

        FacingComponent facingComponent = engine.createComponent(FacingComponent.class);
        player.add(facingComponent);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        player.add(animationComponent);

        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        player.add(collisionComponent);

        AbilityComponent abilityComponent = engine.createComponent(AbilityComponent.class);
        player.add(abilityComponent);

        RenderComponent renderComponent = engine.createComponent(RenderComponent.class);
        player.add(renderComponent);

        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        player.add(playerComponent);

        StatisticsComponent statisticsComponent = engine.createComponent(StatisticsComponent.class);
        player.add(statisticsComponent);

        PlayerMovementComponent playerMovementComponent = engine.createComponent(PlayerMovementComponent.class);
        player.add(playerMovementComponent);

        setPlayerComponentValues();

        engine.addEntity(player);
    }

    private MapObjectData getRectangle(RectangleMapObject object) {
        Rectangle rectangle = object.getRectangle();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                rectangle.width * 0.5f * UNIT_SCALE,
                rectangle.height * 0.5f * UNIT_SCALE);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(
                (rectangle.x + rectangle.width * 0.5f) * UNIT_SCALE,
                (rectangle.y + rectangle.height * 0.5f) * UNIT_SCALE);
        return new MapObjectData(object, shape, bodyDef);
    }

    private MapObjectData getPolygon(PolygonMapObject object) {
        PolygonShape shape = new PolygonShape();
        float[] vertices = object.getPolygon().getTransformedVertices();
        float[] worldVertices = new float[vertices.length];
        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] * UNIT_SCALE;
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
                    vertices[i * 2] * UNIT_SCALE,
                    vertices[i * 2 + 1] * UNIT_SCALE);
        }
        ChainShape shape = new ChainShape();
        shape.createChain(worldVertices);
        BodyDef bodyDef = new BodyDef();
        return new MapObjectData(object, shape, bodyDef);
    }

    public void setPlayerInfo(String playerGroupName) {
        PlayerInfo playerInfo = new PlayerInfo(assetsHandler);
        if (playerInfo.setPlayerInfo(playerGroupName)) {
            this.playerInfo = playerInfo;
            setPlayerComponentValues();
        }
    }

    private void setPlayerComponentValues() {
        if (player == null) {
            return;
        }
        if (player.getComponent(AnimationComponent.class) != null &&
            player.getComponent(PlayerMovementComponent.class) != null &&
            player.getComponent(StatisticsComponent.class) != null) {
            player.getComponent(AnimationComponent.class).animations = playerInfo.getAnimations();
            player.getComponent(AnimationComponent.class).animationsAttributes = playerInfo.getAnimationsAttributes();
            player.getComponent(PlayerMovementComponent.class).runSpeed = playerInfo.getPlayerSettingsData().getMovementSpeed();
            player.getComponent(PlayerMovementComponent.class).jumpSpeed = playerInfo.getPlayerSettingsData().getJumpSpeed();
            player.getComponent(StatisticsComponent.class).health = playerInfo.getPlayerSettingsData().getHealth();
            player.getComponent(StatisticsComponent.class).energy = playerInfo.getPlayerSettingsData().getEnergy();
            player.getComponent(StatisticsComponent.class).range = playerInfo.getPlayerSettingsData().getRange();
        }
    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return mapInfo.getMap();
    }

    public Entity getPlayer() {
        return player;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    private interface ObjectCreationHandler {
        void createObject(Array<MapObjectData> objectsData);
    }
}
