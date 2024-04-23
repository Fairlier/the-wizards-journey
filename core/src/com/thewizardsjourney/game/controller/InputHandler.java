package com.thewizardsjourney.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class InputHandler extends InputAdapter {
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean action;

    // TODO
    public boolean isDragged;
    private Vector2 fingerLocation = new Vector2(0,0);
    private Vector2 joystickLocation = new Vector2(0, 0);
    private byte joystickPointer = -1;
    private byte jumpPointer = -1;
    float BUTTON_RADIUS = 1 / 9f;
    Vector2 JUMP_BUTTON_CENTER = new Vector2(15 / 16f, 7 / 8f);
    //

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            if ((screenX < (Gdx.graphics.getWidth() >> 1)) && (joystickPointer == -1)) {
                joystickPointer = (byte) pointer;
                joystickLocation.x = screenX;
                joystickLocation.y = screenY;
            }
            else if (((screenX - JUMP_BUTTON_CENTER.x * Gdx.graphics.getWidth()) * (screenX - JUMP_BUTTON_CENTER.x * Gdx.graphics.getWidth())
                    + (screenY - JUMP_BUTTON_CENTER.y * Gdx.graphics.getHeight()) * (screenY - JUMP_BUTTON_CENTER.y * Gdx.graphics.getHeight())
                    <= BUTTON_RADIUS * Gdx.graphics.getHeight() * BUTTON_RADIUS * Gdx.graphics.getHeight()) && (jumpPointer == -1)) {
                jumpPointer = (byte) pointer;
                up = true;
            }
        }
        fingerLocation.x = screenX;
        fingerLocation.y = screenY;

        //Gdx.app.log("InputHandler", "touchDown " + String.valueOf(up));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;

        if (pointer == joystickPointer) {
            joystickPointer = -1;
            right = false;
            left = false;
        }
        if (pointer == jumpPointer) {
            jumpPointer = -1;
            up = false;
        }
        fingerLocation.x = screenX;
        fingerLocation.y = screenY;

        //Gdx.app.log("InputHandler", "touchUp " +
        //        String.valueOf(left) + " " + String.valueOf(right) + " " + String.valueOf(up));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (pointer == joystickPointer) {
                if (screenX > joystickLocation.x) {
                    right = true;
                    left = false;
                }
                else if (screenX < joystickLocation.x) {
                    left = true;
                    right = false;
                }
                else {
                    left = false;
                    right = false;
                }
            }
        }
        fingerLocation.x = screenX;
        fingerLocation.y = screenY;

        // Gdx.app.log("InputHandler", "touchDragged " + String.valueOf(left) + " " + String.valueOf(right));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        fingerLocation.x = screenX;
        fingerLocation.y = screenY;

        return false;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }
}
