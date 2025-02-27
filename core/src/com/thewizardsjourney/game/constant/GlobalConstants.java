package com.thewizardsjourney.game.constant;

import com.badlogic.gdx.math.Vector2;

public class GlobalConstants {
    public static class Screens {
        public static final float MENU_SCENE_WIDTH = 12.80f;
        public static final float MENU_SCENE_HEIGHT = 7.20f;
        public static final float GAME_SCENE_WIDTH = 12.80f;
        public static final float GAME_SCENE_HEIGHT = 7.20f;
        public static final float VIRTUAL_WIDTH = 1280.0f;
        public static final float VIRTUAL_HEIGHT = 720.0f;
        public static final float UNIT_SCALE = 1.0f / 32.0f;
    }

    public static class Physics {
        public static final Vector2 GRAVITY = new Vector2(0, -9.8f);
    }
}
