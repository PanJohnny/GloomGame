package com.panjohnny.game.mem;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String assetName) {
        super("Asset not found: " + assetName);
    }
}
