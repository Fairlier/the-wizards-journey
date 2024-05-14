package com.thewizardsjourney.game.map;

import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.MP_MATERIAL;
import static com.thewizardsjourney.game.constant.AssetConstants.TiledMapDefinitions.MP_MATERIAL_DEFAULT;
import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.UNIT_SCALE;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class MapObjectData {
    private MapObject object;

    public MapObjectData(MapObject object) {
        this.object = object;
    }

    private float[] transformVertices(float[] vertices) {
        float[] worldVertices = new float[vertices.length];
        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] * UNIT_SCALE;
        }
        return worldVertices;
    }

    public Shape getShapeStaticObject() {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            float[] vertices = new float[] {
                    rectangle.x, rectangle.y,
                    rectangle.x + rectangle.width, rectangle.y,
                    rectangle.x + rectangle.width, rectangle.y + rectangle.height,
                    rectangle.x, rectangle.y + rectangle.height
            };
            PolygonShape shape = new PolygonShape();
            shape.set(transformVertices(vertices));
            return shape;
        } else if (object instanceof PolygonMapObject) {
            float[] vertices = ((PolygonMapObject) object).getPolygon().getTransformedVertices();
            PolygonShape shape = new PolygonShape();
            shape.set(transformVertices(vertices));
            return shape;
        } else if (object instanceof PolylineMapObject) {
            float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
            ChainShape shape = new ChainShape();
            shape.createChain(transformVertices(vertices));
            return shape;
        }
        return null;
    }

    public float[] getVerticesStaticObject() {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            float[] vertices = new float[] {
                    rectangle.x, rectangle.y,
                    rectangle.x + rectangle.width, rectangle.y,
                    rectangle.x + rectangle.width, rectangle.y + rectangle.height,
                    rectangle.x, rectangle.y + rectangle.height
            };
            return transformVertices(vertices);
        } else if (object instanceof PolygonMapObject) {
            float[] vertices = ((PolygonMapObject) object).getPolygon().getTransformedVertices();
            return transformVertices(vertices);
        } else if (object instanceof PolylineMapObject) {
            float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
            return transformVertices(vertices);
        }
        return null;
    }

    public Shape getShapeOtherObject() {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    rectangle.width * 0.5f * UNIT_SCALE,
                    rectangle.height * 0.5f * UNIT_SCALE);
            return shape;
        }
        return null;
    }

    public Vector2 getCenterPositionOtherObject() {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            return new Vector2(
                    (rectangle.x + rectangle.width * 0.5f) * UNIT_SCALE,
                    (rectangle.y + rectangle.height * 0.5f) * UNIT_SCALE);
        }
        return null;
    }

    public Vector2 getSizeOtherObject() {
        if (object instanceof RectangleMapObject) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            return rectangle.getSize(new Vector2()).scl(UNIT_SCALE);
        }
        return null;
    }

    public String getMaterial() {
        MapProperties properties = object.getProperties();
        return properties.get(MP_MATERIAL, MP_MATERIAL_DEFAULT, String.class);
    }

    public MapObject getObject() {
        return object;
    }
}
