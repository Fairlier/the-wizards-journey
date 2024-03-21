package com.thewizardsjourney.game.constants;

public class Constants {
    public static class PhysicsSystem {
        public static final int VELOCITY_ITERATIONS = 6;
        public static final int POSITION_ITERATIONS = 2;
        public static final float FIXED_STEP_TIME = 1.0f / 60f;
        public static final float MAX_FRAME_TIME = 0.25f;
    }

    public static class MovementSystem {
        public static final float PROGRESS = 0.2f;
    }

    public enum FacingDirection {
        LEFT,
        RIGHT
    }

    public enum StateType {
        IDLE,
        JUMP,
        FALLING,
        RUN,
        HURT,
        ABILITY
    }
}
