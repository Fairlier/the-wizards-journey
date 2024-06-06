package com.thewizardsjourney.game.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class InputHandler extends InputAdapter {
    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean ability;
    private boolean cast;
    private Vector2 fingerLocation = new Vector2(0,0);

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (ability) {
            fingerLocation.set(screenX, screenY);
        } else {
            fingerLocation.setZero();
        }
        return false;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setAbility(boolean ability) {
        this.ability = ability;
    }

    public void setCast(boolean cast) {
        this.cast = cast;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isAbility() {
        return ability;
    }

    public boolean isCast() {
        return cast;
    }

    public Vector2 getFingerLocation() {
        return fingerLocation;
    }
}
