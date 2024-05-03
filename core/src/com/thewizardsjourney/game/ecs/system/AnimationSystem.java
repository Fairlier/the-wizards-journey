package com.thewizardsjourney.game.ecs.system;

import static com.thewizardsjourney.game.constant.General.Screens.UNITS;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thewizardsjourney.game.ecs.component.AnimationComponent;
import com.thewizardsjourney.game.ecs.component.RenderComponent;

public class AnimationSystem extends IteratingSystem {
    private final ComponentMapper<AnimationComponent> animationComponentCM =
            ComponentMapper.getFor(AnimationComponent.class);
    private final ComponentMapper<RenderComponent> renderComponentCM =
            ComponentMapper.getFor(RenderComponent.class);


    public AnimationSystem(Family family) {
        super(Family.all(
                AnimationComponent.class,
                RenderComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animationComponent = animationComponentCM.get(entity);
        RenderComponent renderComponent = renderComponentCM.get(entity);

        if (animationComponent.isStateChanged) {
            animationComponent.isStateChanged = false;
            animationComponent.animationTime = 0.0f;
            animationComponent.animation = animationComponent.animations.get(animationComponent.state);
        } else {
            animationComponent.animationTime += deltaTime * animationComponent.animationSpeed;
        }

        if (animationComponent.animation != null) {
            TextureRegion textureRegion = (TextureRegion) animationComponent.animation.getKeyFrame(animationComponent.animationTime);
            Sprite sprite = new Sprite(textureRegion);
            sprite.setSize(textureRegion.getRegionWidth() * UNITS, textureRegion.getRegionHeight() * UNITS);
            sprite.setOriginCenter();
            renderComponent.sprite = sprite;
        }
    }
}
