package com.thewizardsjourney.game.asset.player;

import static com.thewizardsjourney.game.constant.Asset.AssetPath.Player.ANIMATIONS;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.PlayerConfig.COST;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.PlayerConfig.ENERGY;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.PlayerConfig.HEALTH;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.PlayerConfig.NAME;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.PlayerConfig.PURCHASED;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.PlayerConfig.RANGE;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.thewizardsjourney.game.asset.animation.AnimationsData;

import java.io.File;

public class PlayerSettingsLoader extends AsynchronousAssetLoader<PlayerSettingsData, PlayerSettingsLoader.PlayerSettingsParameter> {
    private PlayerSettingsData settings;

    public PlayerSettingsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, PlayerSettingsParameter parameter) {
        FileHandle animationsFile = file.parent().child(file.parent().name() + ANIMATIONS);
        AnimationsData animations = manager.get(animationsFile.path(), AnimationsData.class);
        if (animations != null) {
            settings = new PlayerSettingsData();
            try {
                JsonReader reader = new JsonReader();
                JsonValue root = reader.parse(file);
                settings.setName(root.getString(NAME));
                settings.setHealth(root.getInt(HEALTH));
                settings.setEnergy(root.getInt(ENERGY));
                settings.setRange(root.getFloat(RANGE));
                settings.setCost(root.getInt(COST));
                settings.setPurchased(root.getBoolean(PURCHASED));

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public PlayerSettingsData loadSync(AssetManager manager, String fileName, FileHandle file, PlayerSettingsParameter parameter) {
        return settings;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PlayerSettingsParameter parameter) {
        return null;
    }

    public static class PlayerSettingsParameter extends AssetLoaderParameters<PlayerSettingsData> {}
}
