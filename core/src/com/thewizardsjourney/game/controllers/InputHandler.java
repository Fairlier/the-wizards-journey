package com.thewizardsjourney.game.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.thewizardsjourney.game.screens.GameScreen;

public class InputHandler extends InputAdapter {
    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean action;
    private GameScreen gameScreen;

    public InputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            gameScreen.createSquare(screenX, screenY);
            return true;
        }
        return false;
        // TODO
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
        // TODO
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return super.touchCancelled(screenX, screenY, pointer, button);
        // TODO
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer);
        // TODO
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return super.scrolled(amountX, amountY);
        // TODO
    }

    @Override
    public boolean keyDown(int keycode) {
        action = false;
        if (keycode == Input.Keys.LEFT) {
            left = true;
            action = true;
            //gameScreen.movePlayerLeft();
        }
        if (keycode == Input.Keys.RIGHT) {
            right = true;
            action = true;
            //gameScreen.movePlayerRight();
        }
        if (keycode == Input.Keys.UP) {
            jump = true;
            action = true;
            //gameScreen.movePlayerUp();
        }
        return action;
    }

    @Override
    public boolean keyUp(int keycode) {
        action = false;
        if (keycode == Input.Keys.LEFT) {
            left = false;
            action = true;
        } else if (keycode == Input.Keys.RIGHT) {
            right = false;
            action = true;
        } else if (keycode == Input.Keys.UP) {
            jump = false;
            action = true;
        }
        return action;
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
}
