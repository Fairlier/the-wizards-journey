package com.thewizardsjourney.game.ecs.system;

import static com.thewizardsjourney.game.constant.GlobalConstants.Screens.UNIT_SCALE;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thewizardsjourney.game.constant.ECSConstants;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.FacingComponent;
import com.thewizardsjourney.game.ecs.component.RenderingComponent;

public class AnimationSystem extends IteratingSystem {
    private final ComponentMapper<AnimationComponent> animationComponentCM =
            ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<RenderingComponent> renderComponentCM =
            ComponentMapper.getFor(RenderingComponent.class);
    private final ComponentMapper<FacingComponent> facingComponentCM =
            ComponentMapper.getFor(FacingComponent.class);


    public AnimationSystem() {
        super(Family.all(
                AnimationComponent.class,
                RenderingComponent.class,
                FacingComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = animationComponentCM.get(entity);
        RenderingComponent renderingComponent = renderComponentCM.get(entity);
        FacingComponent facingComponent = facingComponentCM.get(entity);

        if (animationComponent.isStateChanged) {
            animationComponent.isStateChanged = false;
            animationComponent.animationTime = 0.0f;
            animationComponent.animation = animationComponent.animations.get(animationComponent.state);
        } else {
            animationComponent.animationTime += deltaTime * animationComponent.animationsAttributes.get(animationComponent.state).getAnimationSpeed();
        }

        if (animationComponent.animation != null) {
            TextureRegion textureRegion = (TextureRegion) animationComponent.animation.getKeyFrame(animationComponent.animationTime);
            Sprite sprite = new Sprite(textureRegion);
            sprite.setSize(textureRegion.getRegionWidth() * UNIT_SCALE * 0.5f, textureRegion.getRegionHeight() * UNIT_SCALE * 0.5f);
            sprite.setOriginCenter();
            if (facingComponent.direction == ECSConstants.FacingDirection.LEFT) {
                sprite.flip(true, false);
            } else {
                sprite.flip(false, false);
            }
            renderingComponent.sprite = sprite;
        }
    }
}
