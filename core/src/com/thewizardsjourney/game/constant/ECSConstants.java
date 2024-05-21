package com.thewizardsjourney.game.constant;

public class ECSConstants {
    public static class PhysicsSystem {
        public static final int VELOCITY_ITERATIONS = 6;
        public static final int POSITION_ITERATIONS = 2;
        public static final float FIXED_STEP_TIME = 1.0f / 60.0f;
        public static final float MAX_FRAME_TIME = 0.25f;
    }

    public enum FacingDirection {
        LEFT,
        RIGHT
    }

    public enum AnimationState {
        IDLE,
        JUMP,
        FALL,
        RUN,
        ABILITY,
        HURT
    }

    public enum EntityType {
        PLAYER,
        STATIC_OBJECT,
        DYNAMIC_OBJECT,
        SENSOR_SAVE_POINT,
        SENSOR_INFO,
        COIN,
        SENSOR_PUZZLE,
        PRISMATIC,
        DISTANCE,
        ROPE,
        NONE
    }
}
