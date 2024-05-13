package com.thewizardsjourney.game.asset.material;

import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.MaterialConfig.DENSITY;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.MaterialConfig.FRICTION;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.MaterialConfig.NAME;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.MaterialConfig.RESTITUTION;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class MaterialsLoader extends AsynchronousAssetLoader<MaterialsData, MaterialsLoader.MaterialsParameter> {
    private MaterialsData materialsData;

    public MaterialsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MaterialsParameter parameter) {
        materialsData = new MaterialsData();
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);
            for (JsonValue materialValue : root) {
                MaterialAttributes attributes = new MaterialAttributes();
                attributes.setName(materialValue.getString(NAME));
                attributes.setDensity(materialValue.getFloat(DENSITY));
                attributes.setFriction(materialValue.getFloat(FRICTION));
                attributes.setRestitution(materialValue.getFloat(RESTITUTION));
                materialsData.putMaterial(attributes);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MaterialsData loadSync(AssetManager manager, String fileName, FileHandle file, MaterialsParameter parameter) {
        return materialsData;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MaterialsParameter parameter) {
        return null;
    }

    public static class MaterialsParameter extends AssetLoaderParameters<MaterialsData> {}
}
