package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.asset.animation.AnimationAttributes;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.constant.ECSConstants.AnimationState;

public class AnimationComponent implements Component {
    public AnimationState state = AnimationState.IDLE;
    public boolean isStateChanged;
    public float animationTime = 0.0f;
    public Animation animation;
    public ObjectMap<AnimationState, Animation> animations = new ObjectMap<>();
    public ObjectMap<ECSConstants.AnimationState, AnimationAttributes> animationsAttributes = new ObjectMap<>();
}
