package me.jaden.station;

import info.rockscode.util.Vector2f;
import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.objects.Player;
import me.jaden.station.objects.Tile;
import me.jaden.station.objects.tiles.Space;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.TileRegistry;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaden on 7/14/2015.
 */
public class World {

    private Tile[][] base;
    private String name;

    private Space[][] space;

    private List<GameObject> objects;

    private LuaValue lua;
    private LuaTable luaWorld;

    public World() {
        this.objects = new ArrayList<GameObject>();

        this.base = new Tile[0][0];

        name = "";
        attachLua(Constants.luaPath+"world"+File.separator+"world");

        createSpace();

        createLuaTable();
        this.runLuaFunc("init");
    }

    public World(Tile[][] base) {
        this.objects = new ArrayList<GameObject>();

        this.base = base;

        name = "";
        attachLua(Constants.luaPath+File.separator+"world"+File.separator+"world");

        space = new Space[100][100];
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                space[i][j] = new Space();
                space[i][j].setPosition(new Vector2f(i * TileRegistry.TILE_SIZE, j * TileRegistry.TILE_SIZE));
            }
        }

        createLuaTable();
        this.runLuaFunc("init");
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
        this.runLuaFunc("update");
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].update();
            }
        }
        for (GameObject i : this.objects) {
            i.update();
        }
        this.runLuaFunc("update");
    }

    public void update(double delta) {
        this.runLuaFunc("update", LuaValue.valueOf(delta));
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].update(delta);
            }
        }
        for (GameObject i : this.objects) {
            i.update(delta);
        }
        this.runLuaFunc("update", LuaValue.valueOf(delta));
    }

    public void onBeginPlay() {
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].onBeginPlay();
            }
        }
        for (GameObject i : this.objects) {
            i.onBeginPlay();
        }
        this.runLuaFunc("beginPlay");
    }

    public void render() {
        for (int i = 0; i < this.space.length; i++) {
            for (int j = 0; j < this.space[i].length; j++) {
                this.space[i][j].render();
            }
        }
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].render();
            }
        }
        for (GameObject i : this.objects) {
            i.render();
        }
        this.runLuaFunc("render");
    }

    public void cleanUp() {
        for (int i = 0; i < this.space.length; i++) {
            for (int j = 0; j < this.space[i].length; j++) {
                this.space[i][j].cleanUp();
            }
        }
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].cleanUp();
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
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].reInit();
            }
        }
        for (GameObject i : this.objects) {
            i.reInit();
        }
    }

    public void keyInput(KeyEvent e) {
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].keyInput(e);
            }
        }
        for (GameObject i : this.objects) {
            i.keyInput(e);
        }
        this.runLuaFunc("keyInput", e.getLuaTable());
    }

    public void mouseInput(MouseEvent e) {
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                this.base[i][j].mouseInput(e);
            }
        }
        for (GameObject i : this.objects) {
            i.mouseInput(e);
        }
        this.runLuaFunc("mouseInput", e.getLuaTable());
    }

    public Tile getTileAtPos(double x, double y) {
        for (int i = 0; i < this.base.length; i++) {
            for (int j = 0; j < this.base[i].length; j++) {
                Vector2f cpos = Station.instance.getGame().getCamera().getPosition();
                Vector2f tpos = this.base[i][j].getPosition();
                if ((x+cpos.x > tpos.x) &&
                        (x+cpos.x < tpos.x+TileRegistry.TILE_SIZE) &&
                        (y+cpos.y > tpos.y) &&
                        (y+cpos.y < tpos.y+TileRegistry.TILE_SIZE)) { // TODO DO NOT USE CONSTANTS WITH THIS JADEN
                    return this.base[i][j];
                }
            }
        }
        return null;
    }

    public boolean overlaps(Vector2f pos) {
        GameObject o = getTileAtPos(pos.x, pos.y);
        if (o != null) {
            if (o.isSolid()) {
                return true;
            }
        }
        o = getTileAtPos(pos.x+TileRegistry.TILE_SIZE, pos.y);
        if (o != null) {
            if (o.isSolid()) {
                return true;
            }
        }
        o = getTileAtPos(pos.x, pos.y+ TileRegistry.TILE_SIZE);
        if (o != null) {
            if (o.isSolid()) {
                return true;
            }
        }
        o = getTileAtPos(pos.x+TileRegistry.TILE_SIZE, pos.y+TileRegistry.TILE_SIZE);
        if (o != null) {
            if (o.isSolid()) {
                return true;
            }
        }
        return false;
    }

    public void setBaseLayer(Tile[][] b) {
        this.base = b;
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

    public Tile getRelativeTile(Direction dir, Tile rel) {
        Tile t;
        try {
            if (dir == Direction.UP) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32))][((int) Math.floor(rel.getPosition().y) / 32) - 1];
            } else if (dir == Direction.DOWN) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32))][((int) Math.floor(rel.getPosition().y) / 32) + 1];
            } else if (dir == Direction.RIGHT) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32)) + 1][((int) Math.floor(rel.getPosition().y) / 32)];
            } else if (dir == Direction.LEFT) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32)) - 1][((int) Math.floor(rel.getPosition().y) / 32)];
            } else if (dir == Direction.UP_RIGHT) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32)) + 1][((int) Math.floor(rel.getPosition().y) / 32) - 1];
            } else if (dir == Direction.UP_LEFT) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32)) - 1][((int) Math.floor(rel.getPosition().y) / 32) - 1];
            } else if (dir == Direction.DOWN_RIGHT) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32)) + 1][((int) Math.floor(rel.getPosition().y) / 32) + 1];
            } else if (dir == Direction.DOWN_LEFT) {
                t = this.base[((int) Math.floor(rel.getPosition().x / 32 - 1))][((int) Math.floor(rel.getPosition().y) / 32) + 1];
            } else {
                Station.instance.getLogger().log("This shit is only 2D asshole...");
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            t = null;
        }
        return t;
    }

    public void attachLua(String path) {
        try {
            this.lua = JsePlatform.standardGlobals();
            this.lua.get("dofile").call(LuaValue.valueOf(path + ".lua"));
        } catch (Exception e) {
            Station.instance.getLogger().log("Unable to find "+path+".lua");
        }
    }

    public void runLuaFunc(String func) {
        if (this.lua != null) {
            try {
                LuaValue luaGetLine = this.lua.get(func);
                if (!luaGetLine.isnil()) {
                    luaGetLine.call(this.getLuaTable());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Station.instance.getLogger().log("Unable to run "+func+"()");
            }
        }
    }

    public void runLuaFunc(String func, LuaValue arg) {
        if (this.lua != null) {
            try {
                LuaValue luaGetLine = this.lua.get(func);
                if (!luaGetLine.isnil()) {
                    luaGetLine.call(this.getLuaTable(), arg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Station.instance.getLogger().log("Unable to run "+func+"()");
            }
        }
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

    protected class gettile extends TwoArgFunction {

        private World w;

        public gettile(World w) {
            this.w = w;
        }

        @Override
        public LuaValue call(LuaValue x, LuaValue y) {
            return this.w.base[x.toint()][y.toint()].getLuaTable();
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
