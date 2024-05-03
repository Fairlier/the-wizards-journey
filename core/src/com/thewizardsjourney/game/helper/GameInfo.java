package com.thewizardsjourney.game.helper;

import com.badlogic.gdx.utils.Array;

public class GameInfo {
    private Array<String> mapsName;
    private Array<String> charactersName;
    private String selectedMapName = "map_0_tiled_map";
    private String selectedCharacterName;

    public GameInfo() {}

    public Array<String> getMapsName() {
        return mapsName;
    }

    public void setMapsName(Array<String> mapsName) {
        this.mapsName = mapsName;
    }

    public Array<String> getCharactersName() {
        return charactersName;
    }

    public void setCharactersName(Array<String> charactersName) {
        this.charactersName = charactersName;
    }

    public String getSelectedMapName() {
        return selectedMapName;
    }

    public void setSelectedMapName(String selectedMapName) {
        this.selectedMapName = selectedMapName;
    }

    public String getSelectedCharacterName() {
        return selectedCharacterName;
    }

    public void setSelectedCharacterName(String selectedCharacterName) {
        this.selectedCharacterName = selectedCharacterName;
    }
}
