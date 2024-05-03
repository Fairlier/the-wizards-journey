package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thewizardsjourney.game.controller.InputHandler;
import com.thewizardsjourney.game.ecs.component.AbilityComponent;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.PlayerComponent;

public class AbilitySystem extends IteratingSystem {
    private Vector2 touchPoint;
    private final World world;
    private final InputHandler controller;
    private final Viewport viewport;
    private final ShapeRenderer renderer;
    ComponentMapper<BodyComponent> bodyComponentCM
            = ComponentMapper.getFor(BodyComponent.class);
    ComponentMapper<AbilityComponent> abilityComponentCM
            = ComponentMapper.getFor(AbilityComponent.class);

    public AbilitySystem(World world, InputHandler controller, Viewport viewport) {
        super(Family.all(
                BodyComponent.class,
                AbilityComponent.class,
                PlayerComponent.class
        ).get());
        this.world = world;
        this.controller = controller;
        this.viewport = viewport;
        touchPoint = new Vector2();
        renderer = new ShapeRenderer();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) { // TODO
        AbilityComponent abilityComponent = abilityComponentCM.get(entity);
        if (abilityComponent.state) {
            renderer.setProjectionMatrix(viewport.getCamera().combined);
            BodyComponent bodyComponent = bodyComponentCM.get(entity);
            drawInnerCircleAroundCharacter(bodyComponent.body.getTransform().getPosition());
            drawOuterCircleAroundCharacter(bodyComponent.body.getTransform().getPosition());
            if (abilityComponent.isStateChanged) {
                touchPoint.set(bodyComponent.body.getTransform().getPosition());
            } else {
                Vector3 worldCoordinates = new Vector3(controller.getFingerLocation(), 0);
                viewport.getCamera().unproject(worldCoordinates);
                Vector2 temp = new Vector2(worldCoordinates.x, worldCoordinates.y);
                if (temp.dst(bodyComponent.body.getTransform().getPosition()) > 0.783f + 0.1f
                        && temp.dst(bodyComponent.body.getTransform().getPosition()) < 3.0f - 0.1f) {
                    touchPoint.set(temp);
                }
            }
            if (touchPoint.dst(bodyComponent.body.getTransform().getPosition()) > 0.783f + 0.1f
                && touchPoint.dst(bodyComponent.body.getTransform().getPosition()) < 3.0f - 0.1f) {
                drawTouchPoint();
                drawLineToTouchPoint(bodyComponent.body.getTransform().getPosition());
            }

        }
    }

    private final RayCastCallback callback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            System.out.println(point);
            return 0;
        }
    };

    private void drawInnerCircleAroundCharacter(Vector2 position) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.circle(position.x, position.y, 0.783f, 100);
        renderer.end();
    }

    private void drawOuterCircleAroundCharacter(Vector2 position) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.circle(position.x, position.y, 3.0f, 100);
        renderer.end();
    }

    private void drawTouchPoint() {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.circle(touchPoint.x, touchPoint.y, 0.1f, 100);
        renderer.end();
    }

    private void drawLineToTouchPoint(Vector2 position) {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.line(position.x, position.y, touchPoint.x, touchPoint.y);
        renderer.end();
    }
}
