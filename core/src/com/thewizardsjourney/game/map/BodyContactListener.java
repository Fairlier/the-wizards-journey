package com.thewizardsjourney.game.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.thewizardsjourney.game.constant.General;
import com.thewizardsjourney.game.ecs.component.BodyComponent;
import com.thewizardsjourney.game.ecs.component.CollisionComponent;
import com.thewizardsjourney.game.ecs.component.EntityTypeComponent;

public class BodyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        short categoryA = fixtureA.getFilterData().categoryBits;
        Entity entityA = (Entity) fixtureA.getBody().getUserData();
        Fixture fixtureB = contact.getFixtureB();
        short categoryB = fixtureB.getFilterData().categoryBits;
        Entity entityB = (Entity) fixtureB.getBody().getUserData();

        if (fixtureA.isSensor() || fixtureB.isSensor()) {
            System.out.println("sensor");
        }

        if (entityA != null && entityB != null) {
            addCollisionData(categoryA, entityA, entityB);
            addCollisionData(categoryB, entityB, entityA);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        short categoryA = fixtureA.getFilterData().categoryBits;
        Entity entityA = (Entity) fixtureA.getBody().getUserData();
        Fixture fixtureB = contact.getFixtureB();
        short categoryB = fixtureB.getFilterData().categoryBits;
        Entity entityB = (Entity) fixtureB.getBody().getUserData();

        if (entityA != null && entityB != null) {
            removeCollisionData(categoryA, entityA, entityB);
            removeCollisionData(categoryB, entityB, entityA);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}

    private void addCollisionData(short sourceCategory, Entity sourceEntity, Entity collidedEntity) {
        EntityTypeComponent entityTypeComponent = sourceEntity.getComponent(EntityTypeComponent.class);
        CollisionComponent collisionComponent = sourceEntity.getComponent(CollisionComponent.class);
        BodyComponent bodyComponent = sourceEntity.getComponent(BodyComponent.class);

        if (entityTypeComponent != null && collisionComponent != null && bodyComponent != null) {
            collisionComponent.category = sourceCategory;
            collisionComponent.firstCollidedEntity = collidedEntity;
            collisionComponent.lastCollidedEntity = null;
        }
    }

    private void removeCollisionData(short sourceCategory, Entity sourceEntity, Entity collidedEntity) {
        EntityTypeComponent entityTypeComponent = sourceEntity.getComponent(EntityTypeComponent.class);
        CollisionComponent collisionComponent = sourceEntity.getComponent(CollisionComponent.class);
        BodyComponent bodyComponent = sourceEntity.getComponent(BodyComponent.class);

        if (entityTypeComponent != null && collisionComponent != null && bodyComponent != null) {
            collisionComponent.category = sourceCategory;
            collisionComponent.firstCollidedEntity = null;
            collisionComponent.lastCollidedEntity = collidedEntity;
        }
    }
}
