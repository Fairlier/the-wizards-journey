package com.thewizardsjourney.game.constant;

import com.badlogic.gdx.math.Vector2;

public class General {
    public static class Screens {
        public static final float SCENE_WIDTH = 12.80f;
        public static final float SCENE_HEIGHT = 7.20f;
        public static final float UNITS = 30.0f;
    }

    public static class Physics {
        public static final Vector2 GRAVITY = new Vector2(0, -9.8f);
    }

    public enum Categories {
        PLAYER,
        STATIC_OBJECT,
        NONE
    }
}
