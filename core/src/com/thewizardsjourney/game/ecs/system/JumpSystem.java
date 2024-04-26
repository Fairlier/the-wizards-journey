package com.thewizardsjourney.game.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.thewizardsjourney.game.constant.General;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.JumpComponent;

public class JumpSystem extends IteratingSystem {
    private boolean isColliding;
    private final World world;
    private final ComponentMapper<BodyComponent> bm =
            ComponentMapper.getFor(BodyComponent.class);
    private final ComponentMapper<JumpComponent> jm =
            ComponentMapper.getFor(JumpComponent.class);

    public JumpSystem(World world) {
        super(Family.all(
                BodyComponent.class,
                JumpComponent.class
        ).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bm.get(entity);
        JumpComponent jumpComponent  = jm.get(entity);
        if (jumpComponent.state) {
            isColliding = false;
            collisionCheck(bodyComponent.body);
            if (isColliding) {
                bodyComponent.body.setLinearVelocity(
                        bodyComponent.body.getLinearVelocity().x,
                        jumpComponent.velocity.y * bodyComponent.body.getMass()
                );
            }
        }
    }

    private void collisionCheck(Body body) { // TODO константы?
        Vector2 bottomLeftPoint = new Vector2();
        Vector2 bottomRightPoint = new Vector2();
        ((PolygonShape) body.getFixtureList().get(0).getShape()).getVertex(0, bottomLeftPoint);
        ((PolygonShape) body.getFixtureList().get(0).getShape()).getVertex(1, bottomRightPoint);
        bottomLeftPoint.set(
                body.getTransform().getPosition().x + (bottomRightPoint.x * 0.7f),
                body.getTransform().getPosition().y + bottomLeftPoint.y);
        bottomRightPoint.set(
                body.getTransform().getPosition().x + (bottomRightPoint.x * 0.7f),
                body.getTransform().getPosition().y + (bottomRightPoint.y * 1.01f));
        world.QueryAABB(callback, bottomLeftPoint.x, bottomLeftPoint.y, bottomRightPoint.x, bottomRightPoint.y);
    }

    private final QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if (fixture.getUserData() != General.Categories.PLAYER) {
                isColliding = true;
            }
            return true;
        }
    };
}
