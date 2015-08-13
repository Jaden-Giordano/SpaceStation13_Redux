package me.jaden.station.events;

import me.jaden.station.GameObject;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Created by Jaden on 7/24/2015.
 */
public class InteractEvent {

    public static final int CLICK = 0;

    private GameObject inter, inted;
    private int action;

    private LuaTable luaInteractEvent;

    public InteractEvent(GameObject inter, GameObject inted, int action) {
        this.inter = inter;
        this.inted = inted;
        this.action = action;

        createLuaTable();
    }

    public GameObject getInteracter() {
        return this.inter;
    }

    public GameObject getInteracted() {
        return this.inted;
    }

    public int getAction() {
        return this.action;
    }

    private void createLuaTable() {
        luaInteractEvent = LuaValue.tableOf();

        this.luaInteractEvent.set("getInteracter", new getinter(this));
        this.luaInteractEvent.set("getInteracted", new getinted(this));
        this.luaInteractEvent.set("getAction", new getaction(this));
    }

    public LuaTable getLuaTable() {
        return this.luaInteractEvent;
    }

    protected class getinter extends ZeroArgFunction {

        private InteractEvent e;

        public getinter(InteractEvent e) {
            this.e = e;
        }

        @Override
        public LuaValue call() {
            return this.e.getInteracter().getLuaTable();
        }

    }

    protected class getinted extends ZeroArgFunction {

        private InteractEvent e;

        public getinted(InteractEvent e) {
            this.e = e;
        }

        @Override
        public LuaValue call() {
            return this.e.getInteracted().getLuaTable();
        }

    }

    protected class getaction extends ZeroArgFunction {

        private InteractEvent e;

        public getaction(InteractEvent e) {
            this.e = e;
        }

        @Override
        public LuaValue call() {
            if (e.getAction() == InteractEvent.CLICK) {
                return LuaValue.valueOf("click");
            }
            return LuaValue.valueOf(this.e.getAction());
        }

    }

}
