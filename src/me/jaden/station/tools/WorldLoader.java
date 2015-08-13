package me.jaden.station.tools;

import com.google.gson.Gson;
import me.jaden.station.Station;
import me.jaden.station.objects.Tile;
import me.jaden.station.World;
import me.jaden.station.objects.tiles.CustomTile;
import org.apache.commons.io.IOUtils;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaden on 7/14/2015.
 */
public class WorldLoader {

    protected class WorldDataContainer {
        public String layer;
        public int[] size;
        public int[] tiles;

        public WorldDataContainer(String layer, int[] size, int[] tiles) {
            this.layer = layer;
            this.size = size;
            this.tiles = tiles;
        }
    }

    private static Map<Integer, TileRefContainer> custom = new HashMap<Integer, TileRefContainer>();

    public static void linkIdToTile(int id, String path) {
        custom.put(id, new TileRefContainer(path, "", -1));
    }

    public static void linkIdToTile(int id, String path, String i_path, int sheetId) {
        Station.instance.getLogger().log("Params Link : "+i_path+" "+sheetId);
        if (sheetId == -1) {
            custom.put(id, new TileRefContainer(path, i_path, -1));
        }
        else {
            custom.put(id, new TileRefContainer(path, i_path, sheetId));
        }
    }

    public static World loadWorld(String path) {
        World world = new World();
        try {
            InputStream inp = new FileInputStream(path+".station");
            String in = IOUtils.toString(inp, "UTF-8");

            String[] s = in.split(System.lineSeparator());

            Gson gson = new Gson();
            WorldDataContainer worldData = gson.fromJson(s[0], WorldDataContainer.class);

            if (worldData == null) {
                Station.instance.getLogger().log("Unable to load world");
                return new World();
            }

            world.setBaseLayer(convertTileData(worldData.tiles, worldData.size));

            return world;
        } catch (Exception e) {
            Station.instance.getLogger().log("Error loading file: " + path + ".world");
            e.printStackTrace();
            return world;
        }
    }

    private static Tile[][] convertTileData(int[] tiles, int[] size) {
        if (tiles.length > 0) {
            Tile[][] convertTile = new Tile[size[0]][size[1]];

            for (int j = 0; j < size[1]; j++) {
                for (int i = 0; i < size[0]; i++) {
                    if (custom.containsKey(tiles[(size[0]*j)+i])) {
                        TileRefContainer t = custom.get(tiles[(size[0]*j)+i]);
                        convertTile[i][j] = new CustomTile(tiles[(size[0]*j)+i], custom.get(tiles[(size[0]*j)+i]).lua_path, t.image_path, t.sheetId);
                    }
                    else {
                        convertTile[i][j] = TileRegistry.createTile(tiles[(size[0] * j) + i]);
                    }
                }
            }

            for (int i = 0; i < convertTile.length; i++) {
                for (int j = 0; j < convertTile[i].length; j++) {
                    convertTile[i][j].getPosition().x = i*TileRegistry.TILE_SIZE;
                    convertTile[i][j].getPosition().y = j*TileRegistry.TILE_SIZE;
                }
            }

            return convertTile;
        }
        return null;
    }

}