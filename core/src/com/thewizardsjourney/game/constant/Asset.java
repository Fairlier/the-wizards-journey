package com.thewizardsjourney.game.constant;

public class Asset {
    public static class AssetPath {
        public static final String ASSETS = "data/assets.json";

        public static class Player {
            public static final String PARENT = "data/box2D/players";
            public static final String TEXTURE_ATLAS = "_texture_atlas.atlas";
            public static final String ANIMATIONS = "_animations.json";
            public static final String SETTINGS = "_settings.json";
        }

        public static class Map {
            public static final String PARENT = "data/box2D/maps";
            public static final String TILED_MAP = "_tiled_map.tmx";
            public static final String SETTINGS = "_settings.json";
        }
    }

    public static class TiledMapDefinitions {
        public static final String LN_STATIC_OBJECTS = "static_objects";
        public static final String LN_OTHER_OBJECTS = "other_objects";
        public static final String MP_MATERIAL = "material";
        public static final String MP_MATERIAL_DEFAULT = "default";
        public static final String OB_PLAYER = "player";
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
            public static final String COST = "cost";
            public static final String PURCHASED = "purchased";
        }
    }

    public static class AssetGroups {
        public static class General {
            public static final String GROUP_NAME = "general";
            public static final String MATERIALS = "materials";
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

        public static class PlayerTextureAtlases {
            public static final String GROUP_NAME = "player_texture_atlases";
        }

        public static class PlayerAnimations {
            public static final String GROUP_NAME = "player_animations";

        }
        public static class PlayerSettings {
            public static final String GROUP_NAME = "player_settings";
        }

        public static class Maps {
            public static final String GROUP_NAME = "maps";
        }
        
        public static class MapsSettings {
            public static final String GROUP_NAME = "maps_settings";
        }
    }
}
