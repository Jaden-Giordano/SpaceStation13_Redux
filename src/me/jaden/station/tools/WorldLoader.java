package me.jaden.station.tools;

import com.google.gson.Gson;
import info.rockscode.util.Vector3f;
import me.jaden.station.GameObject;
import me.jaden.station.Station;
import me.jaden.station.World;
import me.jaden.station.objects.tiles.CustomTile;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jaden on 7/14/2015.
 */
public class WorldLoader {

    public class WorldDataContainer {

        public int[] size; // Disregarded... For aidan
        public TileContainer[] tiles;

        public WorldDataContainer(int[] size, TileContainer[] tiles) {
            this.tiles = tiles;
        }
    }

    public class TileContainer {

        public int[] pos;
        public int id;

        public TileContainer(int[] pos, int id) {
            this.pos = pos;
            this.id= id;
        }

    }

    private static Map<Integer, TileRefContainer> custom = new HashMap<Integer, TileRefContainer>();

    public static void linkIdToTile(int id, String path) {
        custom.put(id, new TileRefContainer(path, "", -1));
    }

    public static void linkIdToTile(int id, String path, String i_path, int sheetId) {
        Station.instance.getLogger().log("Params Link : "+i_path+" "+sheetId);
        custom.put(id, new TileRefContainer(path, i_path, sheetId));
    }

    public static World loadWorld(String path) {
        World world = new World();
        try {
            InputStream inp = new FileInputStream(path+".station");
            String in = IOUtils.toString(inp, "UTF-8");

            Gson gson = new Gson();
            WorldDataContainer worldData = gson.fromJson(in, WorldDataContainer.class);
            if (worldData == null) {
                throw new Exception("Unable to load World : "+path+".station "+"(World file may be empty or corupt...");
            }

            List<GameObject> tileList = new ArrayList<GameObject>();

            for (int i = 0; i < worldData.tiles.length; i++) {
                if (custom.containsKey(worldData.tiles[i].id)) {
                    TileRefContainer t = custom.get(worldData.tiles[i].id);
                    tileList.add(new CustomTile(worldData.tiles[i].id, t.lua_path, t.image_path, t.sheetId));
                    tileList.get(tileList.size()-1).setPosition(new Vector3f((float) (worldData.tiles[i].pos[0]-1) * 32, (float) (worldData.tiles[i].pos[1]-1) * 32, (float) worldData.tiles[i].pos[2]));
                }
                else {
                    tileList.add(TileRegistry.createTile(worldData.tiles[i].id));
                }
            }

            world = new World(tileList);

            return world;
        } catch (Exception e) {
            Station.instance.getLogger().log("Error loading file: " + path + ".station");
            e.printStackTrace();
            return world;
        }
    }

}