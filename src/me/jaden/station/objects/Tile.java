package me.jaden.station.objects;

import me.jaden.station.GameObject;
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

}
