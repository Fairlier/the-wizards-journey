package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.physics.box2d.Joint;

public class JointInfo {
    private Joint joint;
    private float motorSpeed;

    public Joint getJoint() {
        return joint;
    }

    public void setJoint(Joint joint) {
        this.joint = joint;
    }

    public float getMotorSpeed() {
        return motorSpeed;
    }

    public void setMotorSpeed(float motorSpeed) {
        this.motorSpeed = motorSpeed;
    }
}
