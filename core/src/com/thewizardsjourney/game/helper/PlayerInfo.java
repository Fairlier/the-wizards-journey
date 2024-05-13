package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.asset.animation.AnimationAttributes;
import com.thewizardsjourney.game.asset.animation.AnimationsData;
import com.thewizardsjourney.game.asset.player.PlayerSettingsData;
import com.thewizardsjourney.game.constant.AssetConstants;
import com.thewizardsjourney.game.constant.ECSConstants;

import java.util.Iterator;

public class PlayerInfo {
    private TextureAtlas atlas;
    private AnimationsData animationsData;
    private PlayerSettingsData playerSettingsData;
    private ObjectMap<ECSConstants.AnimationState, Animation> animations;
    private ObjectMap<ECSConstants.AnimationState, AnimationAttributes> animationsAttributes;
    private final AssetsHandler assetsHandler;

    public PlayerInfo(AssetsHandler assetsHandler) {
        this.assetsHandler = assetsHandler;
    }

    public boolean setPlayerInfo(String playerGroupName) {
        atlas = null;
        animationsData = null;
        playerSettingsData = null;

        String atlasFileName = AssetConstants.AssetPath.Player.TEXTURE_ATLAS;
        atlasFileName = atlasFileName.substring(0, atlasFileName.lastIndexOf("."));

        String animationsFileName = AssetConstants.AssetPath.Player.ANIMATIONS;
        animationsFileName = animationsFileName.substring(0, animationsFileName.lastIndexOf("."));

        String settingsFileName = AssetConstants.AssetPath.Player.SETTINGS;
        settingsFileName = settingsFileName.substring(0, settingsFileName.lastIndexOf("."));

        if (assetsHandler.isLoaded(playerGroupName, atlasFileName) &&
            assetsHandler.isLoaded(playerGroupName, animationsFileName) &&
            assetsHandler.isLoaded(playerGroupName, settingsFileName)) {
            atlas = assetsHandler.get(playerGroupName, atlasFileName);
            animationsData = assetsHandler.get(playerGroupName, animationsFileName);
            playerSettingsData = assetsHandler.get(playerGroupName, settingsFileName);
            initializeAnimations(atlas, animationsData);
            initializeAnimationsAttributes(animationsData);
        } else {
            System.out.println("NO");
        }

        return areAllVariablesInitialized();
    }

    private void initializeAnimations(TextureAtlas atlas, AnimationsData animationsData) {
        animations = new ObjectMap<>();
        Iterator<ObjectMap.Entry<ECSConstants.AnimationState, AnimationAttributes>> iterator = animationsData.getAnimationsAttributes().iterator();
        while (iterator.hasNext()) {
            ObjectMap.Entry<ECSConstants.AnimationState, AnimationAttributes> entry = iterator.next();
            ECSConstants.AnimationState animationState = entry.key;
            AnimationAttributes animationAttributes = entry.value;
            Array<TextureAtlas.AtlasRegion> regions = new Array<>();
            for (String frame : animationAttributes.getFrames()) {
                TextureAtlas.AtlasRegion region = atlas.findRegion(frame);
                if (region != null) {
                    regions.add(region);
                } else {
                    System.out.println("Region not found: " + frame);
                }
            }
            Animation.PlayMode playMode;
            try {
                playMode = Animation.PlayMode.valueOf(animationAttributes.getPlayMode().toUpperCase());
            } catch (IllegalArgumentException e) {
                playMode = Animation.PlayMode.NORMAL;
            }
            Animation animation  = new Animation<>(animationAttributes.getFrameDuration(), regions, playMode);
            animations.put(animationState, animation);
        }
    }

    private void initializeAnimationsAttributes(AnimationsData animationsData) {
        animationsAttributes = animationsData.getAnimationsAttributes();
    }

    public boolean areAllVariablesInitialized() {
        return animations != null && animationsAttributes != null && playerSettingsData != null;
    }

    public PlayerSettingsData getPlayerSettingsData() {
        return playerSettingsData;
    }

    public ObjectMap<ECSConstants.AnimationState, Animation> getAnimations() {
        return animations;
    }

    public ObjectMap<ECSConstants.AnimationState, AnimationAttributes> getAnimationsAttributes() {
        return animationsAttributes;
    }
}
