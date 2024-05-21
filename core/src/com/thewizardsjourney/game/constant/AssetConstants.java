package com.thewizardsjourney.game.constant;

public class AssetConstants {
    public static class AssetPath {
        public static final String ASSETS = "data/assets.json";

        public static class Player {
            public static final String PARENT_DIRECTORY = "data/box2D/players";
            public static final String TEXTURE_ATLAS = "player_texture_atlas.atlas";
            public static final String ANIMATIONS = "player_animations.json";
            public static final String SETTINGS = "player_settings.json";
        }

        public static class Map {
            public static final String PARENT_DIRECTORY = "data/box2D/maps";
            public static final String TILED_MAP = "map_tiled_map.tmx";
            public static final String SETTINGS = "map_settings.json";
            public static final String MATERIALS = "map_materials.json";
        }
    }

    public static class TiledMapDefinitions {
        public static final String LN_STATIC_OBJECTS = "static_objects";
        public static final String LN_OTHER_OBJECTS = "other_objects";
        public static final String LN_PUZZLE_OBJECTS = "puzzle_objects";
        public static final String MP_MATERIAL = "material";
        public static final String MP_MATERIAL_DEFAULT = "default";
        public static final String OB_PLAYER = "player";
        public static final String OB_BOX = "box";
        public static final String OB_COIN = "coin";
        public static final String OB_SENSOR_HARM = "sensor_harm";
        public static final String OB_SENSOR_EXIT = "sensor_exit";
        public static final String OB_SENSOR_INFO = "sensor_info";
        public static final String OB_SENSOR_SAVE_POINT = "sensor_save_point";
        public static final String PO_SENSOR_PUZZLE = "sensor_puzzle";
        public static final String PO_FIXED = "fixed";
        public static final String PO_MOVING = "moving";
    }

    public static class CustomAsset {
        public static class AssetConfig {
            public static final String NAME = "name";
            public static final String ASSET_TYPE = "asset_type";
            public static final String TYPE = "type";
            public static final String PATH = "path";
        }

        public static class MaterialConfig {
            public static final String NAME = "name";
            public static final String DENSITY = "density";
            public static final String RESTITUTION = "restitution";
            public static final String FRICTION = "friction";
        }

        public static class AnimationConfig {
            public static final String STATE = "state";
            public static final String FRAME_DURATION = "frame_duration";
            public static final String PLAY_MODE = "play_mode";
            public static final String FRAMES = "frames";
            public static final String ANIMATION_SPEED = "animation_speed";
        }

        public static class MapConfig {
            public static final String NAME = "name";
            public static final String INTERACTED_OBJECTS = "interacted_objects";
            public static final String ACCESSIBLE = "accessible";
        }

        public static class PlayerConfig {
            public static final String NAME = "name";
            public static final String HEALTH = "health";
            public static final String ENERGY = "energy";
            public static final String RANGE = "range";
            public static final String MOVEMENT_SPEED = "movement_speed";
            public static final String JUMP_SPEED = "jump_speed";
            public static final String COST = "cost";
            public static final String PURCHASED = "purchased";
        }
    }

    public static class AssetGroups {
        public static class Default {
            public static final String GROUP_NAME = "default";
        }

        public static class LoadingScreen {
            public static final String GROUP_NAME = "loading_screen";
        }

        public static class MenuScreen {
            public static final String GROUP_NAME = "menu_screen";
        }

        public static class GameScreen {
            public static final String GROUP_NAME = "game_screen";
        }
    }
}
