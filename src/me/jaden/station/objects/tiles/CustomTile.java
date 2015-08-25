package me.jaden.station.objects.tiles;

import me.jaden.station.objects.Tile;
import me.jaden.station.tools.TextureLoader;

/**
 * Created by Jaden on 8/11/2015.
 */
public class CustomTile extends Tile {

    public CustomTile(int id, String pathToLua, String pathToImage, int sheetId) {
        super(id);

        this.attachLua(pathToLua);

        if (!pathToImage.equalsIgnoreCase("")) {
            if (sheetId == -1) {
                this.attachRenderComponent(TextureLoader.loadTexture(pathToImage));
            }
            else {
                this.attachRenderComponent(TextureLoader.getTileFromSheet(sheetId, pathToImage));
            }
        }
        else {
            this.attachRenderComponent(TextureLoader.getTileFromBaseSheet(id));
        }
    }

}
