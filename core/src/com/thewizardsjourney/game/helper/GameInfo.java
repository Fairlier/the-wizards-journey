package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.utils.Array;

public class GameInfo {
    private Array<String> mapGroupNames;
    private Array<String> playerGroupNames;
    private String selectedMapGroupName = "maps_map_4";
    private String selectedPlayerGroupName = "players_player_0";

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
}
