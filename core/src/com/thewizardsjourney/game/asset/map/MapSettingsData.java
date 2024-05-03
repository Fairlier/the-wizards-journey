package com.thewizardsjourney.game.asset.map;

import com.badlogic.gdx.utils.Array;

public class MapSettingsData {
    private String name;
    private Array<String> interactedObjects;
    private boolean accessible;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Array<String> getInteractedObjects() {
        return interactedObjects;
    }

    public void setInteractedObjects(Array<String> interactedObjects) {
        this.interactedObjects = interactedObjects;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }
}
