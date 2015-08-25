package me.jaden.station;

import info.rockscode.util.Vector2f;
import info.rockscode.util.Vector3f;
import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.objects.Player;
import me.jaden.station.objects.tiles.Space;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.TileRegistry;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaden on 7/14/2015.
 */
public class World {

    private List<GameObject> objects;
    private String name;

    private Space[][] space;

    private LuaObject lua;
    private LuaTable luaWorld;

    public World() {
        this.objects = new ArrayList<GameObject>();

        name = "";

        createLuaTable();
        attachLua(Constants.luaPath + "world" + File.separator + "world");

        createSpace();

        if (this.lua != null) {
            this.lua.runLuaFunc("init");
        }
    }

    public World(List<GameObject> objects) {
        this.objects = objects;

        name = "";
        createLuaTable();
        attachLua(Constants.luaPath + "world" + File.separator+"world");

        createSpace();

        if (this.lua != null) {
            this.lua.runLuaFunc("init");
        }
    }

    public void createSpace() {
        space = new Space[(int) Math.floor(Constants.width / TileRegistry.TILE_SIZE)+1][(int) Math.floor(Constants.height/TileRegistry.TILE_SIZE)+1];
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                space[i][j] = new Space();
                space[i][j].setPosition(new Vector2f(i * TileRegistry.TILE_SIZE, j * TileRegistry.TILE_SIZE));
            }
        }
    }

    public void update() {
        for (GameObject i : this.objects) {
            i.update();
        }
        if (this.lua != null) {
            this.lua.runLuaFunc("update");
        }
    }

    public void update(double delta) {
        for (GameObject i : this.objects) {
            i.update(delta);
        }
        if (this.lua != null) {
            this.lua.runLuaFunc("update", LuaValue.valueOf(delta));
        }
    }

    public void onBeginPlay() {
        for (GameObject i : this.objects) {
            i.onBeginPlay();
        }
        if (this.lua != null) {
            this.lua.runLuaFunc("beginPlay");
        }
    }

    public void render() {
        for (int i = 0; i < this.space.length; i++) {
            for (int j = 0; j < this.space[i].length; j++) {
                this.space[i][j].render();
            }
        }
        for (GameObject i : this.objects) {
            i.render();
        }
    }

    public void cleanUp() {
        for (int i = 0; i < this.space.length; i++) {
            for (int j = 0; j < this.space[i].length; j++) {
                this.space[i][j].cleanUp();
            }
        }
        for (GameObject i : this.objects) {
            i.cleanUp();
        }
    }

    public void reInit() {
        createSpace();
        for (int i = 0; i < this.space.length; i++) {
            for (int j = 0; j < this.space[i].length; j++) {
                this.space[i][j].reconstruct();
            }
        }
        for (GameObject i : this.objects) {
            i.reInit();
        }
    }

    public void keyInput(KeyEvent e) {
        for (GameObject i : this.objects) {
            i.keyInput(e);
        }
        if (this.lua != null) {
            this.lua.runLuaFunc("keyInput", e.getLuaTable());
        }
    }

    public void mouseInput(MouseEvent e) {
        for (GameObject i : this.objects) {
            i.mouseInput(e);
        }
        if (this.lua != null) {
            this.lua.runLuaFunc("mouseInput", e.getLuaTable());
        }
    }

    public List<GameObject> getTileAtPos(float x, float y, float z) {
        List<GameObject> objs = new ArrayList<GameObject>();
        for (GameObject o : this.objects) {
            if (o.getPosition().equals(new Vector3f(x, y, z))) {
                objs.add(o);
            }
        }

        return objs;
    }

    public boolean overlaps(Vector3f pos) {
        if (this.objects.size() != 0) {
            List<GameObject> os = getTileAtPos(pos.x, pos.y, pos.z);
            if (os.size() > 0) {
                GameObject o = os.get(0); // TODO CHANGE THIS SHIT TO ALLOW CHECKING ALL TILES INSTEAD OF FIRST FOUND
                if (o != null) {
                    if (o.isSolid()) {
                        return true;
                    }
                }
            }
            os = getTileAtPos(pos.x + TileRegistry.TILE_SIZE, pos.y, pos.z);
            if (os.size() > 0) {
                GameObject o = os.get(0); // TODO CHANGE THIS SHIT TO ALLOW CHECKING ALL TILES INSTEAD OF FIRST FOUND
                if (o != null) {
                    if (o.isSolid()) {
                        return true;
                    }
                }
            }
            os = getTileAtPos(pos.x, pos.y + TileRegistry.TILE_SIZE, pos.z);
            if (os.size() > 0) {
                GameObject o = os.get(0); // TODO CHANGE THIS SHIT TO ALLOW CHECKING ALL TILES INSTEAD OF FIRST FOUND
                if (o != null) {
                    if (o.isSolid()) {
                        return true;
                    }
                }
            }
            os = getTileAtPos(pos.x + TileRegistry.TILE_SIZE, pos.y + TileRegistry.TILE_SIZE, pos.z);
            if (os.size() > 0) {
                GameObject o = os.get(0); // TODO CHANGE THIS SHIT TO ALLOW CHECKING ALL TILES INSTEAD OF FIRST FOUND
                if (o != null) {
                    if (o.isSolid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addObject(GameObject o) {
        this.objects.add(o);
    }

    public List<GameObject> getObjects() {
        return this.objects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void attachLua(String path) {
        this.lua = new LuaObject(path, this.getLuaTable());
    }

    private void createLuaTable() {
        luaWorld = LuaValue.tableOf();

        this.luaWorld.set("setName", new setname(this));
        this.luaWorld.set("getName", new getname(this));
        this.luaWorld.set("getTile", new gettile(this));
        this.luaWorld.set("addPlayer", new addplayer(this));
    }

    public LuaTable getLuaTable() {
        return luaWorld;
    }

    public class setname extends OneArgFunction {

        private World w;

        public setname(World w) {
            this.w = w;
        }

        @Override
        public LuaValue call(LuaValue s) {
            w.setName(s.tojstring());
            return s;
        }
    }

    public class getname extends ZeroArgFunction {

        private World w;

        public getname(World w) {
            this.w = w;
        }

        @Override
        public LuaValue call() {
            return LuaValue.valueOf(w.getName());
        }
    }

    protected class gettile extends ThreeArgFunction {

        private World w;

        public gettile(World w) {
            this.w = w;
        }

        @Override
        public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
            List<GameObject> ts = this.w.getTileAtPos(x.tofloat(), y.tofloat(), z.tofloat());
            if (ts.size() > 0) {
                return ts.get(0).getLuaTable();  // TODO CHANGE THIS SHIT TO ALLOW CHECKING ALL TILES INSTEAD OF FIRST FOUND
            }
            else {
                return LuaValue.NIL;
            }
        }
    }

    protected class addplayer extends ZeroArgFunction {

        private World w;

        public addplayer(World w) {
            this.w = w;
        }

        @Override
        public LuaValue call() {
            for (GameObject i : this.w.getObjects()) {
                if (i instanceof Player) {
                    Station.instance.getLogger().log("Game Already contains a player...");
                    return LuaValue.valueOf(0);
                }
            }
            Player p = new Player();
            this.w.addObject(p);
            return p.getLuaTable();
        }
    }

}
