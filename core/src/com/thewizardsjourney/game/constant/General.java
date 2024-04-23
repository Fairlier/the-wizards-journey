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

    public static class CollisionFilters {
        public static final short CATEGORY_PLAYER = 0x0001;
        public static final short CATEGORY_STATIC_OBJECT = 0x0002;
        public static final short CATEGORY_SENSOR = 0x0004;

        public static final short MASK_PLAYER = ~CATEGORY_PLAYER;
        public static final short MASK_STATIC_OBJECT = -1;
        public static final short MASK_SENSOR = CATEGORY_PLAYER;
    }
}
