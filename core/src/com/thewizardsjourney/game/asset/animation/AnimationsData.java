package com.thewizardsjourney.game.asset.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.constant.ECS;

public class AnimationsData {
    private final TextureAtlas atlas;
    private final ObjectMap<ECS.AnimationState, Animation> animations;

    public AnimationsData(TextureAtlas atlas) {
        this.atlas = atlas;
        animations = new ObjectMap<>();
    }

    public void putAnimation(AnimationAttributes settings) {
        if (atlas == null) {
            System.out.println("TextureAtlas is not set");
            return;
        }
        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for (String frame : settings.getFrames()) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(frame);
            if (region != null) {
                regions.add(region);
            } else {
                System.out.println("Region not found: " + frame);
                return;
            }
        }
        ECS.AnimationState state;
        try {
            state = ECS.AnimationState.valueOf(settings.getState().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid animation state: " + settings.getState());
            return;
        }
        Animation animation = new Animation(settings.getFrameDuration(), regions, settings.getPlayMode());
        animations.put(state, animation);
    }

    public ObjectMap<ECS.AnimationState, Animation> getAnimations() {
        return animations;
    }
}
