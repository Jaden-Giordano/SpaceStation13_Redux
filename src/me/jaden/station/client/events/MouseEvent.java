package me.jaden.station.client.events;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Created by Jaden on 7/24/2015.
 */
public class MouseEvent {

    private int button;
    private double x, y;

    private LuaTable luaMouseEvent;

    public LuaTable getLuaTable() {
        return this.luaMouseEvent;
    }

    public int getButton() {
        return this.button;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public MouseEvent(int button, double x, double y) {
        this.button = button;
        this.x = x;
        this.y = y;

        createLuaTable();
    }

    private void createLuaTable() {
        luaMouseEvent = LuaValue.tableOf();

        luaMouseEvent.set("getButton", new getbutton(this));
        luaMouseEvent.set("getX", new getx(this));
        luaMouseEvent.set("getY", new gety(this));
    }

    protected class getbutton extends ZeroArgFunction {

        private MouseEvent e;

        public getbutton(MouseEvent e) {
            this.e = e;
        }

        public LuaValue call() {
            return LuaValue.valueOf(e.getButton());
        }

    }

    protected class getx extends ZeroArgFunction {

        private MouseEvent e;

        public getx(MouseEvent e) {
            this.e = e;
        }

        public LuaValue call() {
            return LuaValue.valueOf(e.getX());
        }

    }

    protected class gety extends ZeroArgFunction {

        private MouseEvent e;

        public gety(MouseEvent e) {
            this.e = e;
        }

        public LuaValue call() {
            return LuaValue.valueOf(e.getY());
        }

    }

}
