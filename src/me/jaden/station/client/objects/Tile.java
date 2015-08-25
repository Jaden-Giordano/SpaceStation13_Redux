package me.jaden.station.client.objects;

import me.jaden.station.client.Direction;
import me.jaden.station.client.GameObject;
import me.jaden.station.client.Station;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Created by Jaden on 7/15/2015.
 */
public abstract class Tile extends GameObject {

    // Tile Variables
    protected String name;
    protected int id;

    public Tile() {
        super();
        this.name = "";
        this.id = 0;
        createLuaTable();
    }

    public Tile(int id) {
        super();
        this.name = "";
        this.id = id;
        createLuaTable();
    }

    public void onBeginPlay() {
        super.onBeginPlay();
    }

    public void update() {
        super.update();
    }

    public void render() {
        super.render();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public Tile setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    private void createLuaTable() {
        this.luaTable.set("setName", new setname(this));
        this.luaTable.set("getName", new getname(this));
        this.luaTable.set("setId", new setid(this));
        this.luaTable.set("getId", new getid(this));
    }

    //Lua Tile lib
    protected class setname extends OneArgFunction {

        private Tile t;

        public setname(Tile t) {
            this.t = t;
        }

        @Override
        public LuaValue call(LuaValue s) {
            t.setName(s.tojstring());
            return s;
        }
    }

    protected class getname extends ZeroArgFunction {

        private Tile t;

        public getname(Tile t) {
            this.t = t;
        }

        @Override
        public LuaValue call() {
            return LuaValue.valueOf(t.getName());
        }
    }

    protected class setid extends OneArgFunction {

        private Tile t;

        public setid(Tile t) {
            this.t = t;
        }

        @Override
        public LuaValue call(LuaValue i) {
            this.t.setId(i.toint());
            return i;
        }

    }

    protected class getid extends ZeroArgFunction {

        private Tile t;

        public getid(Tile t) {
            this.t = t;
        }

        @Override
        public LuaValue call() {
            return LuaValue.valueOf(this.t.getId());
        }

    }

    protected class getdirection extends OneArgFunction {

        private Tile t;

        public getdirection(Tile t) {
            this.t = t;
        }

        @Override
        public LuaValue call(LuaValue dir) {
            Tile n;
            if ((n = Station.instance.getGame().getWorld().getRelativeTile(convertToDir(dir.tojstring()), t)) != null) {
                return n.getLuaTable();
            }
            return LuaValue.NIL;
        }

        private Direction convertToDir(String dir) {
            if (dir.equalsIgnoreCase("n")) {
                return Direction.UP;
            } else if (dir.equalsIgnoreCase("s")) {
                return Direction.DOWN;
            } else if (dir.equalsIgnoreCase("e")) {
                return Direction.RIGHT;
            } else if (dir.equalsIgnoreCase("w")) {
                return Direction.LEFT;
            } else if (dir.equalsIgnoreCase("ne")) {
                return Direction.UP_RIGHT;
            } else if (dir.equalsIgnoreCase("nw")) {
                return Direction.UP_LEFT;
            } else if (dir.equalsIgnoreCase("se")) {
                return Direction.DOWN_RIGHT;
            } else if (dir.equalsIgnoreCase("sw")) {
                return Direction.DOWN_LEFT;
            }
            return Direction.UP;
        }

    }

}
