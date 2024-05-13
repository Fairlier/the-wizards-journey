package com.thewizardsjourney.game.asset.player;

import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.COST;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.ENERGY;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.HEALTH;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.JUMP_SPEED;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.MOVEMENT_SPEED;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.NAME;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.PURCHASED;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.PlayerConfig.RANGE;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class PlayerSettingsLoader extends AsynchronousAssetLoader<PlayerSettingsData, PlayerSettingsLoader.PlayerSettingsParameter> {
    private PlayerSettingsData playerSettingsData;

    public PlayerSettingsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, PlayerSettingsParameter parameter) {
        playerSettingsData = new PlayerSettingsData();
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);
            playerSettingsData.setName(root.getString(NAME));
            playerSettingsData.setHealth(root.getInt(HEALTH));
            playerSettingsData.setEnergy(root.getInt(ENERGY));
            playerSettingsData.setRange(root.getInt(RANGE));
            playerSettingsData.setMovementSpeed(root.getFloat(MOVEMENT_SPEED));
            playerSettingsData.setJumpSpeed(root.getFloat(JUMP_SPEED));
            playerSettingsData.setCost(root.getInt(COST));
            playerSettingsData.setPurchased(root.getBoolean(PURCHASED));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerSettingsData loadSync(AssetManager manager, String fileName, FileHandle file, PlayerSettingsParameter parameter) {
        return playerSettingsData;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PlayerSettingsParameter parameter) {
        return null;
    }

    public static class PlayerSettingsParameter extends AssetLoaderParameters<PlayerSettingsData> {}
}
