package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.utils.Array;
import com.thewizardsjourney.game.helper.JointInfo;

public class PuzzleSensorComponent implements Component {
    public int numberOfTargets;
    public String targetObjectName;
    public Color color;
    public Vector2 lowerBound = new Vector2();
    public Vector2 upperBound = new Vector2();
    public Array<JointInfo> joints = new Array<>();
}
