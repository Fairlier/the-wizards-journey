package com.thewizardsjourney.game.constant;

public class Asset {
    public static final String ASSETS_PATH = "data/assets.json";

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

    public static class AssetGroup {
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
            public static final String MAP_1 = "map_1";
        }
    }
}
