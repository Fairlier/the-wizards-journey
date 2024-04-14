package com.thewizardsjourney.game.constant;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class Asset {
    public static class AssetPath {
        public static final String ASSETS = "data/assets.json";
        public static final String GAME_MAPS = "data/box2D/game_maps";
        public static final String MAP_FORMAT = ".tmx";
        public static final Class<?> MAP_TYPE = TiledMap.class;

    }

    public static class AssetData {
        public static final String NAME = "name";
        public static final String ASSET_TYPE = "assetType";
        public static final String TYPE = "type";
        public static final String PATH = "path";
    }

    public static class CustomAsset {
        public static class Material {
            public static final String NAME = "name";
            public static final String DENSITY = "density";
            public static final String RESTITUTION = "restitution";
            public static final String FRICTION = "friction";
        }
    }

    public static class AssetGroups {
        public static class LoadingScreen {

            public static final String GROUP_NAME = "loading_screen";
        }
        public static class MenuScreen {

            public static final String GROUP_NAME = "menu_screen";
        }
        public static class GameScreen {

            public static final String GROUP_NAME = "game_screen";
        }
        public static class MapSettings {

            public static final String GROUP_NAME = "map_settings";
            public static final String MATERIALS = "materials";
        }
        public static class MapList {

            public static final String GROUP_NAME = "map_list";
        }
        public static class Graphics {
            public static final String GROUP_NAME = "graphics";
        }
    }
}
