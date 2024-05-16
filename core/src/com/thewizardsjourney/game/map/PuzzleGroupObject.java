package com.thewizardsjourney.game.map;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.helper.JointInfo;

public class PuzzleGroupObject {
    private MapObjectData sensor;
    private ObjectMap<String, Array<MapObjectData>> mapObjectsForJoints;
    private Array<JointInfo> joints;

    public PuzzleGroupObject(MapObjectData sensor, ObjectMap<String, Array<MapObjectData>> mapObjectsForJoints) {
        this.sensor = sensor;
        this.mapObjectsForJoints = mapObjectsForJoints;
        joints = new Array<>();
    }

    public MapObjectData getSensor() {
        return sensor;
    }

    public void setSensor(MapObjectData sensor) {
        this.sensor = sensor;
    }

    public ObjectMap<String, Array<MapObjectData>> getMapObjectsForJoints() {
        return mapObjectsForJoints;
    }

    public void setMapObjectsForJoints(ObjectMap<String, Array<MapObjectData>> mapObjectsForJoints) {
        this.mapObjectsForJoints = mapObjectsForJoints;
    }

    public Array<JointInfo> getJoints() {
        return joints;
    }

    public void setJoints(Array<JointInfo> joints) {
        this.joints = joints;
    }
}
