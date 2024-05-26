package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.thewizardsjourney.game.asset.AssetsHandler;
import com.thewizardsjourney.game.asset.map.MapSettingsData;
import com.thewizardsjourney.game.asset.material.MaterialsData;
import com.thewizardsjourney.game.constant.AssetConstants;

public class MapInfo {
    private TiledMap map;
    private TextureAtlas dynamicObjectsAtlas;
    private TextureAtlas puzzlesAtlas;
    private MapSettingsData mapSettingsData;
    private MaterialsData materialsData;
    private final AssetsHandler assetsHandler;

    public MapInfo(AssetsHandler assetsHandler) {
        this.assetsHandler = assetsHandler;
    }

    public boolean setMapInfo(String mapGroupName) {
        map = null;
        mapSettingsData = null;
        materialsData = null;

        String mapFileName = AssetConstants.AssetPath.Map.TILED_MAP;
        mapFileName = mapFileName.substring(0, mapFileName.lastIndexOf("."));

        String settingsFileName = AssetConstants.AssetPath.Map.SETTINGS;
        settingsFileName = settingsFileName.substring(0, settingsFileName.lastIndexOf("."));

        String materialsFileName = AssetConstants.AssetPath.Map.MATERIALS;
        materialsFileName = materialsFileName.substring(0, materialsFileName.lastIndexOf("."));

        String dynamicObjectsAtlasFileName = AssetConstants.AssetPath.Map.DYNAMIC_OBJECTS_TEXTURE_ATLAS;
        dynamicObjectsAtlasFileName = dynamicObjectsAtlasFileName.substring(0, dynamicObjectsAtlasFileName.lastIndexOf("."));

        String puzzlesAtlasFileName = AssetConstants.AssetPath.Map.PUZZLES_TEXTURE_ATLAS;
        puzzlesAtlasFileName = puzzlesAtlasFileName.substring(0, puzzlesAtlasFileName.lastIndexOf("."));

        if (assetsHandler.isLoaded(mapGroupName, mapFileName) && 
                assetsHandler.isLoaded(mapGroupName, settingsFileName) && 
                assetsHandler.isLoaded(mapGroupName, materialsFileName)) {
            map = assetsHandler.get(mapGroupName, mapFileName);
            mapSettingsData = assetsHandler.get(mapGroupName, settingsFileName);
            materialsData = assetsHandler.get(mapGroupName, materialsFileName);

            if (assetsHandler.isLoaded(mapGroupName, dynamicObjectsAtlasFileName) &&
                    assetsHandler.isLoaded(mapGroupName, puzzlesAtlasFileName)) {
                dynamicObjectsAtlas = assetsHandler.get(mapGroupName, dynamicObjectsAtlasFileName);
                puzzlesAtlas = assetsHandler.get(mapGroupName, puzzlesAtlasFileName);
            }
        }
        return areAllVariablesInitialized();
    }

    public boolean areAllVariablesInitialized() {
        return map != null && mapSettingsData != null && materialsData != null;
    }

    public TiledMap getMap() {
        return map;
    }

    public MapSettingsData getMapSettingsData() {
        return mapSettingsData;
    }

    public MaterialsData getMaterialsData() {
        return materialsData;
    }

    public TextureAtlas getDynamicObjectsAtlas() {
        return dynamicObjectsAtlas;
    }

    public TextureAtlas getPuzzlesAtlas() {
        return puzzlesAtlas;
    }
}
