package com.thewizardsjourney.game.asset.material;

import static com.thewizardsjourney.game.constant.Asset.CustomAsset.MaterialConfig.DENSITY;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.MaterialConfig.FRICTION;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.MaterialConfig.NAME;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.MaterialConfig.RESTITUTION;

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
    private MaterialsData materials;

    public MaterialsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, MaterialsParameter parameter) {
        materials = new MaterialsData();
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);
            for (JsonValue materialValue : root) {
                MaterialAttributes settings = new MaterialAttributes();
                settings.setName(materialValue.getString(NAME));
                settings.setDensity(materialValue.getFloat(DENSITY));
                settings.setFriction(materialValue.getFloat(FRICTION));
                settings.setRestitution(materialValue.getFloat(RESTITUTION));
                materials.putMaterial(settings);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MaterialsData loadSync(AssetManager manager, String fileName, FileHandle file, MaterialsParameter parameter) {
        return materials;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MaterialsParameter parameter) {
        return null;
    }

    public static class MaterialsParameter extends AssetLoaderParameters<MaterialsData> {}
}
