package com.thewizardsjourney.game.asset.animation;

import com.badlogic.gdx.utils.Array;

public class AnimationAttributes {
    private String state;
    private float frameDuration;
    private float animationSpeed;
    private String playMode;
    private Array<String> frames;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public String getPlayMode() {
        return playMode;
    }

    public void setPlayMode(String playMode) {
        this.playMode = playMode;
    }

    public Array<String> getFrames() {
        return frames;
    }

    public void setFrames(Array<String> frames) {
        this.frames = frames;
    }
}
