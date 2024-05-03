package com.thewizardsjourney.game.asset.map;

import static com.thewizardsjourney.game.constant.Asset.AssetPath.Map.TILED_MAP;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.MapConfig.ACCESSIBLE;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.MapConfig.INTERACTED_OBJECTS;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.MapConfig.NAME;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class MapSettingsLoader extends AsynchronousAssetLoader<MapSettingsData, MapSettingsLoader.MapSettingsParameter> {
    private MapSettingsData settings;

    public MapSettingsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MapSettingsParameter parameter) {
        FileHandle mapFile = file.parent().child(file.parent().name() + TILED_MAP);
        TiledMap map = manager.get(mapFile.path(), TiledMap.class);
        if (map != null) {
            settings = new MapSettingsData();
            try {
                JsonReader reader = new JsonReader();
                JsonValue root = reader.parse(file);
                settings.setName(root.getString(NAME));
                Array<String> interactedObjects = new Array<>();
                JsonValue interactedObjectsValue = root.get(INTERACTED_OBJECTS);
                for (JsonValue interactedObjectValue : interactedObjectsValue) {
                    interactedObjects.add(interactedObjectValue.asString());
                }
                settings.setInteractedObjects(interactedObjects);
                settings.setAccessible(root.getBoolean(ACCESSIBLE));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public MapSettingsData loadSync(AssetManager manager, String fileName, FileHandle file, MapSettingsParameter parameter) {
        return settings;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MapSettingsParameter parameter) {
        return null;
    }

    public static class MapSettingsParameter extends AssetLoaderParameters<MapSettingsData> {}
}
