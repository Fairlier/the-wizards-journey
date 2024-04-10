package com.thewizardsjourney.game.asset.material;

import static com.thewizardsjourney.game.constant.Asset.CustomAsset.Material.DENSITY;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.Material.FRICTION;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.Material.NAME;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.Material.RESTITUTION;

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
            for (JsonValue materialsValue : root) {
                Material material = new Material();
                material.setName(materialsValue.getString(NAME));
                material.setDensity(materialsValue.getFloat(DENSITY));
                material.setFriction(materialsValue.getFloat(FRICTION));
                material.setRestitution(materialsValue.getFloat(RESTITUTION));
                materials.putMaterial(material);
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

    public static class MaterialsParameter extends AssetLoaderParameters<MaterialsData> {
    }
}
