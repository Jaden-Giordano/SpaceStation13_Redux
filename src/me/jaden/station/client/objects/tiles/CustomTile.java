package me.jaden.station.client.objects.tiles;

import me.jaden.station.client.Station;
import me.jaden.station.client.objects.Tile;
import me.jaden.station.client.tools.TextureLoader;

/**
 * Created by Jaden on 8/11/2015.
 */
public class CustomTile extends Tile {

    public CustomTile(int id, String pathToLua, String pathToImage, int sheetId) {
        super(id);

        this.attachLua(pathToLua);

        if (!pathToImage.equalsIgnoreCase("")) {
            Station.instance.getLogger().log("using params");
            if (sheetId == -1) {
                Station.instance.getLogger().log("single image");
                this.attachRenderComponent(TextureLoader.loadTexture(pathToImage));
            }
            else {
                Station.instance.getLogger().log("using sheet");
                this.attachRenderComponent(TextureLoader.getTileFromSheet(sheetId, pathToImage));
            }
        }
    }

}
