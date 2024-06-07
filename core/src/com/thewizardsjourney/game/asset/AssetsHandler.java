package com.thewizardsjourney.game.asset;

import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.Map;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.Player;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.SETTINGS_DEFAULT_LANGUAGE;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.SETTINGS_DEFAULT_MUSIC_VOLUME;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.SETTINGS_DEFAULT_SOUNDS_VOLUME;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.SETTINGS_MUSIC_VOLUME;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.SETTINGS_LANGUAGE;
import static com.thewizardsjourney.game.constant.AssetConstants.AssetPath.SETTINGS_SOUNDS_VOLUME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.animation.AnimationsData;
import com.thewizardsjourney.game.asset.animation.AnimationsLoader;
import com.thewizardsjourney.game.asset.map.MapSettingsData;
import com.thewizardsjourney.game.asset.map.MapSettingsLoader;
import com.thewizardsjourney.game.asset.material.MaterialsData;
import com.thewizardsjourney.game.asset.material.MaterialsLoader;
import com.thewizardsjourney.game.asset.player.PlayerSettingsData;
import com.thewizardsjourney.game.asset.player.PlayerSettingsLoader;

public class AssetsHandler implements Disposable, AssetErrorListener {
    private static final String TAG = "AssetHandler";
    private final AssetManager manager;
    private final ObjectMap<String, ObjectMap<String, AssetData>> groups;

    public AssetsHandler() {
        manager = new AssetManager();
        groups = new ObjectMap<>();
        manager.setErrorListener(this);
        manager.setLoader(MaterialsData.class, new MaterialsLoader(new InternalFileHandleResolver()));
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.setLoader(MapSettingsData.class, new MapSettingsLoader(new InternalFileHandleResolver()));
        manager.setLoader(AnimationsData.class, new AnimationsLoader(new InternalFileHandleResolver()));
        manager.setLoader(PlayerSettingsData.class, new PlayerSettingsLoader(new InternalFileHandleResolver()));
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.log(TAG, "error loading " + asset.fileName + " message: " + throwable.getMessage());
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    public void loadGroup(String groupName) {
        ObjectMap<String, AssetData> group = groups.get(groupName, null);
        if (group != null) {
            for (AssetData assetData : group.values()) {
                manager.load(assetData.getPath(), assetData.getType());
            }
        }
        else {
            Gdx.app.log(TAG, "error loading group " + groupName + ", not found");
        }
    }

    public void unloadGroup(String groupName) {
        ObjectMap<String, AssetData> group = groups.get(groupName, null);
        if (group != null) {
            for (AssetData assetData : group.values()) {
                if (manager.isLoaded(assetData.getPath(), assetData.getType())) {
                    manager.unload(assetData.getPath());
                }
            }
        }
        else {
            Gdx.app.log(TAG, "error unloading group " + groupName + ", not found");
        }
    }

    public void parseGroupsFromFile(String assetFile) {
        Gdx.app.log(TAG, "loading file " + assetFile);
        try {
            Json json = new Json();
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(Gdx.files.internal(assetFile));
            for (JsonValue groupValue : root) {
                if (groups.containsKey(groupValue.name)) {
                    Gdx.app.log(TAG, "group " + groupValue.name + " already exists, skipping");
                    continue;
                }
                Gdx.app.log(TAG, "registering group " + groupValue.name);
                ObjectMap<String, AssetData> assets = new ObjectMap<>();
                for (JsonValue assetValue : groupValue) {
                    AssetData assetData = json.fromJson(AssetData.class, assetValue.toString());
                    assets.put(assetData.getName(), assetData);
                }
                groups.put(groupValue.name, assets);
            }
        }
        catch (Exception e) {
            Gdx.app.log(TAG, "error loading file " + assetFile + " " + e.getMessage());
        }
    }

    public Array<String> parseMapsFromDirectory(String directoryPath) {
        Array<String> groupNames = new Array<>();
        FileHandle mapsDirectory = Gdx.files.internal(directoryPath);
        if (mapsDirectory.exists() && mapsDirectory.isDirectory()) {
            FileHandle[] mapDirectories = mapsDirectory.list();
            for (FileHandle mapDirectory : mapDirectories) {
                if (mapDirectory.isDirectory()) {
                    ObjectMap<String, AssetData> assets = new ObjectMap<>();
                    FileHandle mapFile = mapDirectory.child(Map.TILED_MAP);
                    if (mapFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(mapFile.nameWithoutExtension());
                        assetData.setType(TiledMap.class);
                        assetData.setPath(mapFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    FileHandle settingsFile = mapDirectory.child(Map.SETTINGS);
                    if (settingsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(settingsFile.nameWithoutExtension());
                        assetData.setType(MapSettingsData.class);
                        assetData.setPath(settingsFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    FileHandle materialsFile = mapDirectory.child(Map.MATERIALS);
                    if (materialsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(materialsFile.nameWithoutExtension());
                        assetData.setType(MaterialsData.class);
                        assetData.setPath(materialsFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    FileHandle objectsFile = mapDirectory.child(Map.DYNAMIC_OBJECTS_TEXTURE_ATLAS);
                    if (materialsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(objectsFile.nameWithoutExtension());
                        assetData.setType(TextureAtlas.class);
                        assetData.setPath(objectsFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    FileHandle puzzlesFile = mapDirectory.child(Map.PUZZLES_TEXTURE_ATLAS);
                    if (materialsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(puzzlesFile.nameWithoutExtension());
                        assetData.setType(TextureAtlas.class);
                        assetData.setPath(puzzlesFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    if (mapFile.exists() && settingsFile.exists() && materialsFile.exists()) {
                        String groupName = String.join("_", mapDirectory.parent().name(), mapDirectory.name());
                        groupNames.add(groupName);
                        groups.put(groupName, assets);
                    }
                }
            }
        }
        groupNames.sort();
        return groupNames;
    }

    public Array<String> parsePlayersFromDirectory(String directoryPath) {
        Array<String> groupNames = new Array<>();
        FileHandle mapsDirectory = Gdx.files.internal(directoryPath);
        if (mapsDirectory.exists() && mapsDirectory.isDirectory()) {
            FileHandle[] mapDirectories = mapsDirectory.list();
            for (FileHandle mapDirectory : mapDirectories) {
                if (mapDirectory.isDirectory()) {
                    ObjectMap<String, AssetData> assets = new ObjectMap<>();
                    FileHandle atlasFile = mapDirectory.child(Player.TEXTURE_ATLAS);
                    if (atlasFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(atlasFile.nameWithoutExtension());
                        assetData.setType(TextureAtlas.class);
                        assetData.setPath(atlasFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    FileHandle animationsFile = mapDirectory.child(Player.ANIMATIONS);
                    if (animationsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(animationsFile.nameWithoutExtension());
                        assetData.setType(AnimationsData.class);
                        assetData.setPath(animationsFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    FileHandle settingsFile = mapDirectory.child(Player.SETTINGS);
                    if (settingsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(settingsFile.nameWithoutExtension());
                        assetData.setType(PlayerSettingsData.class);
                        assetData.setPath(settingsFile.path());
                        assets.put(assetData.getName(), assetData);
                    }
                    if (atlasFile.exists() && animationsFile.exists() && settingsFile.exists()) {
                        String groupName = String.join("_", mapDirectory.parent().name(), mapDirectory.name());
                        groupNames.add(groupName);
                        groups.put(groupName, assets);
                    }
                }
            }
        }
        groupNames.sort();
        return groupNames;
    }




    public synchronized <T> T get(String groupName, String fileAlias) {
        ObjectMap<String, AssetData> group = groups.get(groupName);
        if (group != null) {
            AssetData assetData = group.get(fileAlias);
            if (assetData != null) {
                return manager.get(assetData.getPath());
            }
        }
        return null;
    }

    public <T> boolean isLoaded(String groupName, String fileAlias) {
        ObjectMap<String, AssetData> group = groups.get(groupName);
        if (group != null) {
            AssetData assetData = group.get(fileAlias);
            if (assetData != null) {
                return manager.isLoaded(assetData.getPath(), assetData.getType());
            }
        }
        return false;
    }

    public boolean update() {
        return manager.update();
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public ObjectMap<String, ObjectMap<String, AssetData>> getGroups() {
        return groups;
    }

    public void setMusicVolume(float volume) {
        FileHandle file = Gdx.files.local(SETTINGS_MUSIC_VOLUME);
        String data = String.valueOf(volume);
        file.writeString(data, false);
    }

    public float getMusicVolume() {
        try {
            FileHandle file = Gdx.files.local(SETTINGS_MUSIC_VOLUME);
            String volume = file.readString();
            return Float.parseFloat(volume);
        }
        catch (Exception e) {
            return SETTINGS_DEFAULT_MUSIC_VOLUME;
        }
    }

    public void setSoundVolume(float volume) {
        FileHandle file = Gdx.files.local(SETTINGS_SOUNDS_VOLUME);
        String data = String.valueOf(volume);
        file.writeString(data, false);
    }

    public float getSoundVolume() {
        try {
            FileHandle file = Gdx.files.local(SETTINGS_SOUNDS_VOLUME);
            String volume = file.readString();
            return Float.parseFloat(volume);
        }
        catch (Exception e) {
            return SETTINGS_DEFAULT_SOUNDS_VOLUME;
        }
    }

    public void setLanguage(String language) {
        FileHandle file = Gdx.files.local(SETTINGS_LANGUAGE);
        file.writeString(language, false);
    }

    public String getLanguage() {
        try {
            FileHandle file = Gdx.files.local(SETTINGS_LANGUAGE);
            return file.readString();
        }
        catch (Exception e) {
            return SETTINGS_DEFAULT_LANGUAGE;
        }
    }
}
