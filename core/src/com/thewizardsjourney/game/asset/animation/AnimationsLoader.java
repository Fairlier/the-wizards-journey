package com.thewizardsjourney.game.asset.animation;

import static com.thewizardsjourney.game.constant.Asset.AssetPath.Player.TEXTURE_ATLAS;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.AnimationConfig.FRAMES;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.AnimationConfig.FRAME_DURATION;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.AnimationConfig.PLAY_MODE;
import static com.thewizardsjourney.game.constant.Asset.CustomAsset.AnimationConfig.STATE;

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

import java.io.File;

public class AnimationsLoader extends AsynchronousAssetLoader<AnimationsData, AnimationsLoader.AnimationsParameter> {
    private AnimationsData animations;

    public AnimationsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AnimationsParameter parameter) {
        FileHandle atlasFile = file.parent().child(file.parent().name() + TEXTURE_ATLAS);
        TextureAtlas atlas = manager.get(atlasFile.path(), TextureAtlas.class);
        if (atlas != null) {
            animations = new AnimationsData(atlas);
            try {
                JsonReader reader = new JsonReader();
                JsonValue root = reader.parse(file);
                for (JsonValue animationValue : root) {
                    AnimationAttributes settings = new AnimationAttributes();
                    settings.setState(animationValue.getString(STATE));
                    settings.setFrameDuration(animationValue.getFloat(FRAME_DURATION));
                    settings.setPlayMode(animationValue.getString(PLAY_MODE));
                    Array<String> frames = new Array<>();
                    JsonValue framesValue = animationValue.get(FRAMES);
                    for (JsonValue frameValue : framesValue) {
                        frames.add(frameValue.asString());
                    }
                    settings.setFrames(frames);
                    animations.putAnimation(settings);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public AnimationsData loadSync(AssetManager manager, String fileName, FileHandle file, AnimationsLoader.AnimationsParameter parameter) {
        return animations;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationsLoader.AnimationsParameter parameter) {
        return null;
    }

    public static class AnimationsParameter extends AssetLoaderParameters<AnimationsData> {}
}
