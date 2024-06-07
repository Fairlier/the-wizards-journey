package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

public class GameInfo {
    private Array<String> mapGroupNames;
    private Array<String> playerGroupNames;
    private String selectedMapGroupName = "";
    private String selectedPlayerGroupName = "players_player_0";
    private final String menuMapGroupName = "maps_menu_map";
    private I18NBundle i18NBundle;
    private String selectedLanguage = "";
    private Music music;

    public GameInfo() {}

    public Array<String> getMapGroupNames() {
        return mapGroupNames;
    }

    public void setMapGroupNames(Array<String> mapGroupNames) {
        this.mapGroupNames = mapGroupNames;
    }

    public Array<String> getPlayerGroupNames() {
        return playerGroupNames;
    }

    public void setPlayerGroupNames(Array<String> playerGroupNames) {
        this.playerGroupNames = playerGroupNames;
    }

    public String getSelectedMapGroupName() {
        return selectedMapGroupName;
    }

    public void setSelectedMapGroupName(String selectedMapGroupName) {
        this.selectedMapGroupName = selectedMapGroupName;
    }

    public String getSelectedPlayerGroupName() {
        return selectedPlayerGroupName;
    }

    public void setSelectedPlayerGroupName(String selectedPlayerGroupName) {
        this.selectedPlayerGroupName = selectedPlayerGroupName;
    }

    public String getMenuMapGroupName() {
        return menuMapGroupName;
    }

    public Array<String> getMapGroupNamesForLevelSelection() {
        Array<String> sortedMapGroupNames = new Array<>(mapGroupNames);
        sortedMapGroupNames.removeValue(menuMapGroupName, false);
        sortedMapGroupNames.sort();
        return sortedMapGroupNames;
    }

    public I18NBundle getI18NBundle() {
        return i18NBundle;
    }

    public void setI18NBundle(I18NBundle i18NBundle) {
        this.i18NBundle = i18NBundle;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
