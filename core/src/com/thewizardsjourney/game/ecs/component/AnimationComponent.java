package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ObjectMap;
import com.thewizardsjourney.game.constant.ECS.AnimationState;

public class AnimationComponent implements Component {
    public AnimationState state = AnimationState.IDLE;
    public boolean isStateChanged;
    public float animationTime = 0.0f;
    public float animationSpeed = 1.0f;
    public Animation animation;
    public ObjectMap<AnimationState, Animation> animations = new ObjectMap<>();
}
