package com.thewizardsjourney.game.asset.material;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.ObjectMap;

public class MaterialsData {
    private final ObjectMap<String, FixtureDef> materials;

    public MaterialsData() {
        materials = new ObjectMap<>();
    }

    public ObjectMap<String, FixtureDef> getMaterials() {
        return materials;
    }

    public void putMaterial(Material material) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = material.getDensity();
        fixtureDef.friction = material.getFriction();
        fixtureDef.restitution = material.getRestitution();
        materials.put(material.getName(), fixtureDef);
    }
}
