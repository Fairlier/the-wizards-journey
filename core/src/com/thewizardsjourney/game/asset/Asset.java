package com.thewizardsjourney.game.asset;

import static com.thewizardsjourney.game.constant.General.Asset.ASSET_TYPE;
import static com.thewizardsjourney.game.constant.General.Asset.PARAMETERS;
import static com.thewizardsjourney.game.constant.General.Asset.PATH;
import static com.thewizardsjourney.game.constant.General.Asset.TYPE;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class Asset implements Json.Serializable {
    private Class<?> type;
    private String path;
    private AssetLoaderParameters parameters;

    @Override
    public void write(Json json) {
        json.writeValue(ASSET_TYPE, type.getName());
        json.writeValue(PATH, path);
        json.writeValue(PARAMETERS, parameters);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        try {
            type = ClassReflection.forName(jsonData.get(TYPE).asString());
        } catch (Exception e) {
            type = null;
        }
        path = jsonData.get(PATH).asString();
        JsonValue parametersValue = jsonData.get(PARAMETERS);
        parameters = parametersValue != null
                ? json.fromJson(AssetLoaderParameters.class, parametersValue.toString())
                : null;
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

    public AssetLoaderParameters getParameters() {
        return parameters;
    }

    public void setParameters(AssetLoaderParameters parameters) {
        this.parameters = parameters;
    }
}
