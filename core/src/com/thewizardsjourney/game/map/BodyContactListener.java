package com.thewizardsjourney.game.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;

public class BodyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Entity entityA = (Entity) fixtureA.getBody().getUserData();
        Fixture fixtureB = contact.getFixtureB();
        Entity entityB = (Entity) fixtureB.getBody().getUserData();

        if (entityA != null && entityB != null) {
            addCollisionData(entityA, entityB);
            addCollisionData(entityB, entityA);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Entity entityA = (Entity) fixtureA.getBody().getUserData();
        Fixture fixtureB = contact.getFixtureB();
        Entity entityB = (Entity) fixtureB.getBody().getUserData();

        if (entityA != null && entityB != null) {
            removeCollisionData(entityA, entityB);
            removeCollisionData(entityB, entityA);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}

    private void addCollisionData(Entity sourceEntity, Entity collidedEntity) {
        EntityTypeComponent entityTypeComponent = sourceEntity.getComponent(EntityTypeComponent.class);
        CollisionComponent collisionComponent = sourceEntity.getComponent(CollisionComponent.class);
        BodyComponent bodyComponent = sourceEntity.getComponent(BodyComponent.class);

        if (entityTypeComponent != null && collisionComponent != null && bodyComponent != null) {
            collisionComponent.firstCollidedEntity = collidedEntity;
            collisionComponent.lastCollidedEntity = null;
        }
    }

    private void removeCollisionData(Entity sourceEntity, Entity collidedEntity) {
        EntityTypeComponent entityTypeComponent = sourceEntity.getComponent(EntityTypeComponent.class);
        CollisionComponent collisionComponent = sourceEntity.getComponent(CollisionComponent.class);
        BodyComponent bodyComponent = sourceEntity.getComponent(BodyComponent.class);

        if (entityTypeComponent != null && collisionComponent != null && bodyComponent != null) {
            collisionComponent.firstCollidedEntity = null;
            collisionComponent.lastCollidedEntity = collidedEntity;
        }
    }
}
