package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.utils.Array;

public class PuzzleSensorComponent implements Component {
    public String targetObjectName;
    public Vector2 lowerBound = new Vector2();
    public Vector2 upperBound = new Vector2();
    public Array<Joint> joints = new Array<>();
}
