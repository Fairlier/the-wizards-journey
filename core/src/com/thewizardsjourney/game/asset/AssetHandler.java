package com.thewizardsjourney.game.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.material.MaterialsData;
import com.thewizardsjourney.game.asset.material.MaterialsLoader;

public class AssetHandler implements Disposable, AssetErrorListener { // TODO
    private static final String TAG = "AssetHandler";
    private AssetManager manager;
    private ObjectMap<String, ObjectMap<String, AssetData>> groups;

    public AssetHandler(String assetFile) {
        manager = new AssetManager();
        groups = new ObjectMap<>();
        manager.setErrorListener(this);
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.setLoader(MaterialsData.class, new MaterialsLoader(new InternalFileHandleResolver()));
        loadGroups(assetFile);
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

    private void loadGroups(String assetFile) {
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
