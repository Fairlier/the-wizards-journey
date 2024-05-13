package com.thewizardsjourney.game.asset.animation;

import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.constant.ECSConstants;

public class AnimationsData {
    private final ObjectMap<ECSConstants.AnimationState, AnimationAttributes> animationsAttributes;

    public AnimationsData() {
        animationsAttributes = new ObjectMap<>();
    }

    public void putAnimation(AnimationAttributes attributes) {
        ECSConstants.AnimationState state;
        try {
            state = ECSConstants.AnimationState.valueOf(attributes.getState().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid animation state: " + attributes.getState());
            return;
        }
        this.animationsAttributes.put(state, attributes);
    }

    public ObjectMap<ECSConstants.AnimationState, AnimationAttributes> getAnimationsAttributes() {
        return animationsAttributes;
    }
}
