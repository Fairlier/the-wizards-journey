package com.thewizardsjourney.game.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetHandler implements Disposable, AssetErrorListener { // TODO
    private static final String TAG = "AssetHandler";
    private AssetManager manager;
    private ObjectMap<String, Array<Asset>> groups;

    public AssetHandler(String assetFile) {
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {

    }

    @Override
    public void dispose() {

    }
}
