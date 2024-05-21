package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.graphics.Color;
import com.thewizardsjourney.game.constant.ECSConstants;

public class EntityTypeInfo {
    private ECSConstants.EntityType entityType;
    private String objectCategoryName;
    private Color color = null;

    public EntityTypeInfo(ECSConstants.EntityType entityType, String objectCategoryName) {
        this.entityType = entityType;
        this.objectCategoryName = objectCategoryName;
    }

    public ECSConstants.EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(ECSConstants.EntityType entityType) {
        this.entityType = entityType;
    }

    public String getObjectCategoryName() {
        return objectCategoryName;
    }

    public void setObjectCategoryName(String objectCategoryName) {
        this.objectCategoryName = objectCategoryName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
