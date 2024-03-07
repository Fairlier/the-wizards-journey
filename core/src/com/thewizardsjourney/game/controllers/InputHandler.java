package com.thewizardsjourney.game.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.thewizardsjourney.game.screens.GameScreen;

public class InputHandler extends InputAdapter {
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
}
