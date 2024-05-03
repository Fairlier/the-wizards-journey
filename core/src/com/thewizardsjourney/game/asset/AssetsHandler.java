package com.thewizardsjourney.game.asset;

import static com.thewizardsjourney.game.constant.Asset.AssetPath.Map;
import static com.thewizardsjourney.game.constant.Asset.AssetPath.Player;

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
import com.thewizardsjourney.game.constant.Asset.AssetGroups;

public class AssetsHandler implements Disposable, AssetErrorListener { // TODO
    private static final String TAG = "AssetHandler";
    private AssetManager manager;
    private ObjectMap<String, ObjectMap<String, AssetData>> groups;

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

    public void parseMapsFromDirectory(String directoryPath) {
        FileHandle mapsDirectory = Gdx.files.internal(directoryPath);
        if (mapsDirectory.exists() && mapsDirectory.isDirectory()) {
            FileHandle[] mapDirectories = mapsDirectory.list();
            ObjectMap<String, AssetData> maps = new ObjectMap<>();
            ObjectMap<String, AssetData> settings = new ObjectMap<>();
            for (FileHandle mapDirectory : mapDirectories) {
                if (mapDirectory.isDirectory()) {
                    FileHandle mapFile = mapDirectory.child(mapDirectory.name() + Map.TILED_MAP);
                    if (mapFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(mapFile.nameWithoutExtension());
                        assetData.setType(TiledMap.class);
                        assetData.setPath(mapFile.path());
                        maps.put(assetData.getName(), assetData);
                    }
                    FileHandle settingsFile = mapDirectory.child(mapDirectory.name() + Map.SETTINGS);
                    if (settingsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(settingsFile.nameWithoutExtension());
                        assetData.setType(MapSettingsData.class);
                        assetData.setPath(settingsFile.path());
                        settings.put(assetData.getName(), assetData);
                    }
                }
            }
            groups.put(AssetGroups.Maps.GROUP_NAME, maps);
            groups.put(AssetGroups.MapsSettings.GROUP_NAME, settings);
        }
    }

    public void parsePlayersFromDirectory(String directoryPath) {
        FileHandle mapsDirectory = Gdx.files.internal(directoryPath);
        if (mapsDirectory.exists() && mapsDirectory.isDirectory()) {
            FileHandle[] mapDirectories = mapsDirectory.list();
            ObjectMap<String, AssetData> atlases = new ObjectMap<>();
            ObjectMap<String, AssetData> animationsCollection = new ObjectMap<>();
            ObjectMap<String, AssetData> settings = new ObjectMap<>();
            for (FileHandle mapDirectory : mapDirectories) {
                if (mapDirectory.isDirectory()) {
                    FileHandle atlasFile = mapDirectory.child(mapDirectory.name() + Player.TEXTURE_ATLAS);
                    if (atlasFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(atlasFile.nameWithoutExtension());
                        assetData.setType(TextureAtlas.class);
                        assetData.setPath(atlasFile.path());
                        atlases.put(assetData.getName(), assetData);
                    }
                    FileHandle animationsFile = mapDirectory.child(mapDirectory.name() + Player.ANIMATIONS);
                    if (animationsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(animationsFile.nameWithoutExtension());
                        assetData.setType(AnimationsData.class);
                        assetData.setPath(animationsFile.path());
                        animationsCollection.put(assetData.getName(), assetData);
                    }
                    FileHandle settingsFile = mapDirectory.child(mapDirectory.name() + Player.SETTINGS);
                    if (settingsFile.exists()) {
                        AssetData assetData = new AssetData();
                        assetData.setName(settingsFile.nameWithoutExtension());
                        assetData.setType(PlayerSettingsData.class);
                        assetData.setPath(settingsFile.path());
                        settings.put(assetData.getName(), assetData);
                    }
                }
            }
            groups.put(AssetGroups.PlayerTextureAtlases.GROUP_NAME, atlases);
            groups.put(AssetGroups.PlayerAnimations.GROUP_NAME, animationsCollection);
            groups.put(AssetGroups.PlayerSettings.GROUP_NAME, settings);
        }
    }

    public Array<String> getSortedMapNames(String groupName) {
        Array<String> mapNames = new Array<>();
        ObjectMap<String, AssetData> group = groups.get(groupName);
        if (group != null) {
            for (AssetData assetData : group.values()) {
                mapNames.add(assetData.getName());
            }
            mapNames.sort();
        }
        return mapNames;
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
}
