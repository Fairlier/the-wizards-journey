package com.thewizardsjourney.game.asset;

import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AssetConfig.NAME;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AssetConfig.ASSET_TYPE;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AssetConfig.PATH;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AssetConfig.TYPE;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class AssetData implements Json.Serializable {
    private String name;
    private Class<?> type;
    private String path;

    @Override
    public void write(Json json) {
        json.writeValue(NAME, name);
        json.writeValue(ASSET_TYPE, type.getName());
        json.writeValue(PATH, path);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        name = jsonData.get(NAME).asString();
        try {
            type = ClassReflection.forName(jsonData.get(TYPE).asString());
        } catch (Exception e) {
            type = null;
        }
        path = jsonData.get(PATH).asString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
