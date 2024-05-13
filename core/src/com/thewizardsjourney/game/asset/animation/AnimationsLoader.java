package com.thewizardsjourney.game.asset.animation;

import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AnimationConfig.ANIMATION_SPEED;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AnimationConfig.FRAMES;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AnimationConfig.FRAME_DURATION;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AnimationConfig.PLAY_MODE;
import static com.thewizardsjourney.game.constant.AssetConstants.CustomAsset.AnimationConfig.STATE;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class AnimationsLoader extends AsynchronousAssetLoader<AnimationsData, AnimationsLoader.AnimationsParameter> {
    private AnimationsData animationsData;

    public AnimationsLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, AnimationsParameter parameter) {
        animationsData = new AnimationsData();
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(file);
            for (JsonValue animationValue : root) {
                AnimationAttributes attributes = new AnimationAttributes();
                attributes.setState(animationValue.getString(STATE));
                attributes.setFrameDuration(animationValue.getFloat(FRAME_DURATION));
                attributes.setAnimationSpeed(animationValue.getFloat(ANIMATION_SPEED));
                attributes.setPlayMode(animationValue.getString(PLAY_MODE));
                Array<String> frames = new Array<>();
                JsonValue framesValue = animationValue.get(FRAMES);
                for (JsonValue frameValue : framesValue) {
                    frames.add(frameValue.asString());
                }
                attributes.setFrames(frames);
                animationsData.putAnimation(attributes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AnimationsData loadSync(AssetManager manager, String fileName, FileHandle file, AnimationsLoader.AnimationsParameter parameter) {
        return animationsData;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AnimationsLoader.AnimationsParameter parameter) {
        return null;
    }

    public static class AnimationsParameter extends AssetLoaderParameters<AnimationsData> {}
}
