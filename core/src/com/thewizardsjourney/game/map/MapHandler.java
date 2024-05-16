package com.thewizardsjourney.game.map;

import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.LN_OTHER_OBJECTS;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.LN_STATIC_OBJECTS;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.MP_MATERIAL_DEFAULT;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.OB_PLAYER;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_HEIGHT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.VIRTUAL_WIDTH;
import static com.thewizardsjourney.game.constant.GlobalConstants.Physics.GRAVITY;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.ecs.component.PlayerAbilityComponent;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;
import com.thewizardsjourney.game.ecs.component.PlayerMovementComponent;
import com.thewizardsjourney.game.ecs.component.PuzzleSensorComponent;
import com.thewizardsjourney.game.ecs.component.RenderComponent;
import com.thewizardsjourney.game.ecs.component.StatisticsComponent;
import com.thewizardsjourney.game.ecs.component.TransformComponent;
import com.thewizardsjourney.game.helper.GameInfo;
import com.thewizardsjourney.game.helper.JointInfo;
import com.thewizardsjourney.game.helper.MapInfo;
import com.thewizardsjourney.game.helper.EntityTypeInfo;
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
            System.out.println("TEST");
            // TODO сменить экран на меню, то есть выдать ошибку
        }

        createObjects(mapInfo.getMap(), LN_STATIC_OBJECTS, this::createStaticObjects);
        createObjects(mapInfo.getMap(), LN_OTHER_OBJECTS, this::createOtherObjects);

        MapLayers layers = mapInfo.getMap().getLayers();
        for (MapLayer layer : layers) {
            if (layer.getName().startsWith("puzzle_objects")) { // TODO
                createObjects(mapInfo.getMap(), layer.getName(), this::createPuzzleObjects);
            }
        }
    }

    public void dispose() {
        Array<Joint> joints = new Array<>();
        world.getJoints(joints);
        for (Joint joint : joints) {
            world.destroyJoint(joint);
        }
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }
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
            MapObjectData objectData = new MapObjectData(object);
            objectsData.add(objectData);
        }
        handler.createObject(objectsData);
    }

    private void createStaticObjects(Array<MapObjectData> objectsData) {
        for (MapObjectData objectData : objectsData) {
            Shape shape = objectData.getShapeStaticObject();
            if (shape == null) {
                continue;
            }
            FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
            if (fixtureDef == null) {
                logger.error("material does not exist, using default");
                fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
            }
            fixtureDef.shape = shape;
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);
            EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                    ECSConstants.EntityType.STATIC_OBJECT,
                    objectData.getObject().getName());
            body.getFixtureList().get(0).setUserData(entityTypeInfo);

            createEntityForStaticObject(body);

            fixtureDef.shape = null;
            shape.dispose();
        }
    }

    private void createOtherObjects(Array<MapObjectData> objectsData) {
        for (MapObjectData objectData : objectsData) {
            switch (objectData.getObject().getName()) {
                case OB_PLAYER:
                    createPlayerObject(objectData);
                    break;
                case "box":
                    Shape shape = objectData.getShapeOtherObject();
                    if (shape == null) {
                        return;
                    }
                    FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
                    if (fixtureDef == null) {
                        logger.error("material does not exist, using default");
                        fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
                    }
                    fixtureDef.shape = shape;
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.position.set(objectData.getCenterPositionOtherObject());
                    bodyDef.type = BodyDef.BodyType.DynamicBody;
                    bodyDef.fixedRotation = true;

                    Body body = world.createBody(bodyDef);
                    body.createFixture(fixtureDef);
                    EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                            ECSConstants.EntityType.NONE,
                            objectData.getObject().getName());
                    body.getFixtureList().get(0).setUserData(entityTypeInfo);

                    fixtureDef.shape = null;
                    shape.dispose();
                default:
                    break;
            }
        }
    }

    private void createEntityForStaticObject(Body body) {
        Entity entity = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(entity);
        entity.add(bodyComponent);

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ((EntityTypeInfo) body.getFixtureList().get(0).getUserData()).getEntityType();
        entity.add(entityTypeComponent);

        engine.addEntity(entity);
    }

    private void createPlayerObject(MapObjectData objectData) { // TODO
        Shape shape = objectData.getShapeOtherObject();
        if (shape == null) {
            return;
        }
        FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
        if (fixtureDef == null) {
            logger.error("material does not exist, using default");
            fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
        }
        fixtureDef.shape = shape;
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(objectData.getCenterPositionOtherObject());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                ECSConstants.EntityType.PLAYER,
                objectData.getObject().getName());
        body.getFixtureList().get(0).setUserData(entityTypeInfo);

        createEntityForPlayerObject(body);
        fixtureDef.shape = null;
        shape.dispose();
    }

    private void createPuzzleObjects(Array<MapObjectData> objectsData) { // TODO
        ObjectMap<String, PuzzleGroupObject> puzzleGroupObjects = new ObjectMap<>();
        for (MapObjectData objectData : objectsData) {
            String group = objectData.getObject().getProperties().get("group", String.class);
            String jointType = objectData.getObject().getProperties().get("joint_type", String.class);
            switch (objectData.getObject().getName()) {
                case "sensor":
                    if (!puzzleGroupObjects.containsKey(group)) {
                        puzzleGroupObjects.put(group, new PuzzleGroupObject(null, new ObjectMap<>()));
                    }
                    puzzleGroupObjects.get(group).setSensor(objectData);
                    break;
                default:
                    if (!puzzleGroupObjects.containsKey(group)) {
                        puzzleGroupObjects.put(group, new PuzzleGroupObject(null, new ObjectMap<>()));
                    }
                    if (!puzzleGroupObjects.get(group).getMapObjectsForJoints().containsKey(jointType)) {
                        puzzleGroupObjects.get(group).getMapObjectsForJoints().put(jointType, new Array<>());
                    }
                    puzzleGroupObjects.get(group).getMapObjectsForJoints().get(jointType).add(objectData);
                    break;
            }
        }
        for (String group : puzzleGroupObjects.keys()) {
            PuzzleGroupObject puzzleGroupObject = puzzleGroupObjects.get(group);
            for (String jointType : puzzleGroupObject.getMapObjectsForJoints().keys()) {
                createPuzzleJointForGroup(puzzleGroupObject, jointType, puzzleGroupObject.getMapObjectsForJoints().get(jointType));
            }
        }
        for (PuzzleGroupObject puzzleGroupObject : puzzleGroupObjects.values()) {
            if (puzzleGroupObject.getSensor() != null) {
                createPuzzleSensorForGroup(puzzleGroupObject, puzzleGroupObject.getSensor());
            }
        }
    }

    private void createPuzzleJointForGroup(PuzzleGroupObject groupObject, String jointType, Array<MapObjectData> objectsData) {
        switch (jointType) {
            case "prismatic":
                createPrismaticJoint(groupObject, objectsData);
                break;
            case "distance":
                createDistanceJoint(groupObject, objectsData);
                break;
            case "rope":
                createRopeJoint(groupObject, objectsData);
                break;
            default:
                System.err.println("Joint type " + jointType + " is not recognized.");
                break;
        }
    }

    private void createPuzzleSensorForGroup(PuzzleGroupObject puzzleGroupObject, MapObjectData objectData) { // TODO
        Shape shape = objectData.getShapeStaticObject();
        if (shape == null) {
            return;
        }
        FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
        if (fixtureDef == null) {
            logger.error("material does not exist, using default");
            fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
        }
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                ECSConstants.EntityType.PUZZLE_SENSOR,
                objectData.getObject().getName());
        body.getFixtureList().get(0).setUserData(entityTypeInfo);

        float[] vertices = objectData.getVerticesStaticObject();
        createEntityForPuzzleSensorObject(body, puzzleGroupObject, new Vector2(vertices[0], vertices[1]), new Vector2(vertices[4], vertices[5]));

        fixtureDef.shape = null;
        shape.dispose();
    }

    private void createEntityForPuzzleSensorObject(Body body, PuzzleGroupObject puzzleGroupObject, Vector2 lowerBound, Vector2 upperBound) {
        Entity entity = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(entity);
        entity.add(bodyComponent);

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ((EntityTypeInfo) body.getFixtureList().get(0).getUserData()).getEntityType();
        entity.add(entityTypeComponent);

        PuzzleSensorComponent puzzleSensorComponent = engine.createComponent(PuzzleSensorComponent.class);
        puzzleSensorComponent.targetObjectName = puzzleGroupObject.getSensor().getObject().getProperties().get("target_object", "", String.class);
        puzzleSensorComponent.lowerBound = lowerBound;
        puzzleSensorComponent.upperBound = upperBound;
        puzzleSensorComponent.joints = puzzleGroupObject.getJoints();
        entity.add(puzzleSensorComponent);

        engine.addEntity(entity);
    }

    private void createPrismaticJoint(PuzzleGroupObject puzzleGroupObject, Array<MapObjectData> objectsData) {
        Body fixedBody = null;
        Body movingBody = null;
        Vector2 fixedBodySize = null;
        Vector2 movingBodySize = null;
        String direction = null;
        String movementDirection = null;
        for (MapObjectData objectData : objectsData) {
            if (objectData.getObject().getName().equals("fixed")) {
                Shape shape = objectData.getShapeOtherObject();
                if (shape == null) {
                    return;
                }
                FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
                if (fixtureDef == null) {
                    logger.error("material does not exist, using default");
                    fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
                }
                fixtureDef.shape = shape;

                BodyDef bodyDef = new BodyDef();
                bodyDef.position.set(objectData.getCenterPositionOtherObject());
                fixedBodySize = objectData.getSizeOtherObject();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.fixedRotation = true;

                fixedBody = world.createBody(bodyDef);
                fixedBody.createFixture(fixtureDef);
                EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                        ECSConstants.EntityType.PRISMATIC_FIXED,
                        objectData.getObject().getName());
                fixedBody.getFixtureList().get(0).setUserData(entityTypeInfo);

                direction = objectData.getObject().getProperties().get("direction", "", String.class);
                movementDirection = objectData.getObject().getProperties().get("movement_direction", "", String.class);

                fixtureDef.shape = null;
                shape.dispose();
            } else if (objectData.getObject().getName().equals("moving")) {
                Shape shape = objectData.getShapeOtherObject();
                if (shape == null) {
                    return;
                }
                FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
                if (fixtureDef == null) {
                    logger.error("material does not exist, using default");
                    fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
                }
                fixtureDef.shape = shape;

                BodyDef bodyDef = new BodyDef();
                bodyDef.position.set(objectData.getCenterPositionOtherObject());
                movingBodySize = objectData.getSizeOtherObject();
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                bodyDef.fixedRotation = true;

                movingBody = world.createBody(bodyDef);
                movingBody.createFixture(fixtureDef);
                EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                        ECSConstants.EntityType.PRISMATIC_MOVING,
                        objectData.getObject().getName());
                movingBody.getFixtureList().get(0).setUserData(entityTypeInfo);

                fixtureDef.shape = null;
                shape.dispose();
            }
        }
        if (fixedBody == null || movingBody == null ||
            fixedBodySize == null || movingBodySize == null ||
            direction == null) {
            return;
        }
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();

        if (direction.equals("vertical")) {
            prismaticJointDef.localAxisA.set(0, 1);
            if (movementDirection.equals("up")) {
                prismaticJointDef.motorSpeed = 5;
            } else if (movementDirection.equals("down")) {
                prismaticJointDef.motorSpeed = -5;
            }
            prismaticJointDef.lowerTranslation = -(fixedBodySize.y * 0.5f + movingBodySize.y * 0.5f);
            prismaticJointDef.upperTranslation = 0;
        } else if (direction.equals("horizontal")) {
            prismaticJointDef.localAxisA.set(1, 0);
            if (movementDirection.equals("right")) {
                prismaticJointDef.motorSpeed = 5;
            } else if (movementDirection.equals("left")) {
                prismaticJointDef.motorSpeed = -5;
            }
            prismaticJointDef.lowerTranslation = -(fixedBodySize.x * 0.5f + movingBodySize.x * 0.5f);
            prismaticJointDef.upperTranslation = 0;
        } else {
            return;
        }

        prismaticJointDef.enableMotor = true;
        prismaticJointDef.maxMotorForce = 500;
        prismaticJointDef.bodyA = fixedBody;
        prismaticJointDef.bodyB = movingBody;
        prismaticJointDef.enableLimit = true;
        prismaticJointDef.collideConnected = false;

        Joint joint = world.createJoint(prismaticJointDef);
        JointInfo jointInfo = new JointInfo();
        jointInfo.setJoint(joint);
        jointInfo.setMotorSpeed(prismaticJointDef.motorSpeed);
        puzzleGroupObject.getJoints().add(jointInfo);

        createEntityForJointObjects(fixedBody, joint);
        createEntityForJointObjects(movingBody, joint);
    }

    private void createEntityForJointObjects(Body body, Joint joint) { // TODO
        Entity entity = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(entity);
        entity.add(bodyComponent);

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ((EntityTypeInfo) body.getFixtureList().get(0).getUserData()).getEntityType();
        entity.add(entityTypeComponent);

        engine.addEntity(entity);
    }

    private void createDistanceJoint(PuzzleGroupObject puzzleGroupObject, Array<MapObjectData> objectsData) {
        Body fixedBody = null;
        Body movingBody = null;

        for (MapObjectData objectData : objectsData) {
            if (objectData.getObject().getName().equals("fixed")) {
                Shape shape = objectData.getShapeOtherObject();
                if (shape == null) {
                    return;
                }
                FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
                if (fixtureDef == null) {
                    logger.error("material does not exist, using default");
                    fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
                }
                fixtureDef.shape = shape;

                BodyDef bodyDef = new BodyDef();
                bodyDef.position.set(objectData.getCenterPositionOtherObject());
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.fixedRotation = true;

                fixedBody = world.createBody(bodyDef);
                fixedBody.createFixture(fixtureDef);
                EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                        ECSConstants.EntityType.DISTANCE_FIXED,
                        objectData.getObject().getName());
                fixedBody.getFixtureList().get(0).setUserData(entityTypeInfo);

                fixtureDef.shape = null;
                shape.dispose();
            } else if (objectData.getObject().getName().equals("moving")) {
                Shape shape = objectData.getShapeOtherObject();
                if (shape == null) {
                    return;
                }
                FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
                if (fixtureDef == null) {
                    logger.error("material does not exist, using default");
                    fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
                }
                fixtureDef.shape = shape;

                BodyDef bodyDef = new BodyDef();
                bodyDef.position.set(objectData.getCenterPositionOtherObject());
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                bodyDef.fixedRotation = true;

                movingBody = world.createBody(bodyDef);
                movingBody.createFixture(fixtureDef);
                EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                        ECSConstants.EntityType.DISTANCE_MOVING,
                        objectData.getObject().getName());
                movingBody.getFixtureList().get(0).setUserData(entityTypeInfo);

                fixtureDef.shape = null;
                shape.dispose();
            }
        }

        if (fixedBody == null || movingBody == null) {
            return;
        }
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = fixedBody;
        distanceJointDef.bodyB = movingBody;
        distanceJointDef.length = movingBody.getPosition().dst(fixedBody.getPosition());
        distanceJointDef.dampingRatio = 0.5f;
        distanceJointDef.frequencyHz = 1.5f;
        Joint joint = world.createJoint(distanceJointDef);
        JointInfo jointInfo = new JointInfo();
        jointInfo.setJoint(joint);
        puzzleGroupObject.getJoints().add(jointInfo);
        createEntityForJointObjects(fixedBody, joint);
        createEntityForJointObjects(movingBody, joint);
    }

    private void createRopeJoint(PuzzleGroupObject puzzleGroupObject, Array<MapObjectData> objectsData) {
        if (objectsData.size < 2) {
            System.out.println("At least two objects are required to create a rope joint.");
            return;
        }

        Array<Body> bodies = new Array<>();

        for (MapObjectData objectData : objectsData) {
            Shape shape = objectData.getShapeOtherObject();
            if (shape == null) {
                return;
            }
            FixtureDef fixtureDef = mapInfo.getMaterialsData().getMaterials().get(objectData.getMaterial());
            if (fixtureDef == null) {
                logger.error("material does not exist, using default");
                fixtureDef = mapInfo.getMaterialsData().getMaterials().get(MP_MATERIAL_DEFAULT);
            }
            fixtureDef.shape = shape;

            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(objectData.getCenterPositionOtherObject());
            bodyDef.type = objectData.getObject().getName().equals("fixed") ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
            System.out.println("body name: " + objectData.getObject().getName());
            System.out.println("body type: " + bodyDef.type);
            bodyDef.fixedRotation = true;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef);
            EntityTypeInfo entityTypeInfo = new EntityTypeInfo(
                    ECSConstants.EntityType.ROPE,
                    objectData.getObject().getName());
            body.getFixtureList().get(0).setUserData(entityTypeInfo);

            bodies.add(body);

            fixtureDef.shape = null;
            shape.dispose();
        }

        for (int i = 0; i < bodies.size - 1; i++) {
            Body bodyA = bodies.get(i);
            Body bodyB = bodies.get(i + 1);

            RopeJointDef ropeJointDef = new RopeJointDef();
            ropeJointDef.bodyA = bodyA;
            ropeJointDef.bodyB = bodyB;
            ropeJointDef.localAnchorA.set(0, 0);
            ropeJointDef.localAnchorB.set(0, 0);

            Vector2 anchorA = bodyA.getWorldCenter();
            Vector2 anchorB = bodyB.getWorldCenter();
            ropeJointDef.maxLength = anchorA.dst(anchorB);

            Joint joint = world.createJoint(ropeJointDef);
            JointInfo jointInfo = new JointInfo();
            jointInfo.setJoint(joint);
            puzzleGroupObject.getJoints().add(jointInfo);

            createEntityForJointObjects(bodyA, joint);
            createEntityForJointObjects(bodyB, joint);
        }
    }

    private void createEntityForPlayerObject(Body body) { // TODO
        player = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        bodyComponent.body = body;
        bodyComponent.body.setUserData(player);
        player.add(bodyComponent);

        EntityTypeComponent entityTypeComponent = engine.createComponent(EntityTypeComponent.class);
        entityTypeComponent.type = ((EntityTypeInfo) body.getFixtureList().get(0).getUserData()).getEntityType();
        player.add(entityTypeComponent);

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.position.set(body.getPosition());
        player.add(transformComponent);

        FacingComponent facingComponent = engine.createComponent(FacingComponent.class);
        player.add(facingComponent);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        player.add(animationComponent);

        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        player.add(collisionComponent);

        PlayerAbilityComponent playerAbilityComponent = engine.createComponent(PlayerAbilityComponent.class);
        player.add(playerAbilityComponent);

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
