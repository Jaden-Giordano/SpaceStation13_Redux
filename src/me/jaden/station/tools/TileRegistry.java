package me.jaden.station.tools;

import me.jaden.station.Station;
import me.jaden.station.objects.Tile;
import me.jaden.station.objects.tiles.DefaultTile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaden on 7/15/2015.
 */
public class TileRegistry {

    public static final int TILE_SIZE = 32;

    public static final int DEFAULT = 0;

    // Registry Variables
    public static Map<Integer, Class> tilesRegistry;

    // Registry Functions
    public static void initRegistry() {
        tilesRegistry = new HashMap<Integer, Class>();

        addTileToRegistry(DEFAULT, DefaultTile.class);
    }

    public static Tile createTile(int id) {
        Station.instance.getLogger().log("No tile defined for Id: "+id);
        return new DefaultTile();
    }

    public static void addTileToRegistry(int id, Class t) {
        tilesRegistry.put(id, t);
    }

}
