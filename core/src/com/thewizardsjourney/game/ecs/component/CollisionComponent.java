package com.thewizardsjourney.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.thewizardsjourney.game.constant.General;

public class CollisionComponent implements Component {
    public short category;
    public Entity firstCollidedEntity;
    public Entity lastCollidedEntity;
}
