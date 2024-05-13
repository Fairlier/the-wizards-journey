package com.thewizardsjourney.game.asset.map;

import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.MapConfig.ACCESSIBLE;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.MapConfig.INTERACTED_OBJECTS;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.MapConfig.NAME;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class MapSettingsLoader extends AsynchronousAssetLoader<MapSettingsData, MapSettingsLoader.MapSettingsParameter> {
    private MapSettingsData mapSettingsData;

    public MapSettingsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MapSettingsParameter parameter) {
        mapSettingsData = new MapSettingsData();
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);
            mapSettingsData.setName(root.getString(NAME));
            Array<String> interactedObjects = new Array<>();
            JsonValue interactedObjectsValue = root.get(INTERACTED_OBJECTS);
            for (JsonValue interactedObjectValue : interactedObjectsValue) {
                interactedObjects.add(interactedObjectValue.asString());
            }
            mapSettingsData.setInteractedObjects(interactedObjects);
            mapSettingsData.setAccessible(root.getBoolean(ACCESSIBLE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MapSettingsData loadSync(AssetManager manager, String fileName, FileHandle file, MapSettingsParameter parameter) {
        return mapSettingsData;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MapSettingsParameter parameter) {
        return null;
    }

    public static class MapSettingsParameter extends AssetLoaderParameters<MapSettingsData> {}
}
