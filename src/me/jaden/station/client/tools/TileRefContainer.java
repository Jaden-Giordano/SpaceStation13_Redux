package me.jaden.station.client.tools;

/**
 * Created by Jaden on 8/12/2015.
 */
public class TileRefContainer {

    public String lua_path, image_path;
    public int sheetId;

    public TileRefContainer(String l_path, String i_path, int sheetId) {
        this.lua_path = l_path;
        this.image_path = i_path;
        this.sheetId = sheetId;
    }

}
