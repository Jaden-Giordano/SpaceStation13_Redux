package me.jaden.station.events;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Jaden on 7/22/2015.
 */
public class KeyEvent {

    public static final int RELEASED = 0, PRESSED = 1;

    private int key, action;

    private LuaTable luaKeyEvent;

    public KeyEvent(int key, int action) {
        this.key = key;
        this.action = action;

        createLuaTable();
    }

    private void createLuaTable() {
        luaKeyEvent = LuaValue.tableOf();

        luaKeyEvent.set("getKey", new getkey(this));
        luaKeyEvent.set("getAction", new getaction(this));
    }

    public LuaTable getLuaTable() {
        return this.luaKeyEvent;
    }

    public int getKey() {
        return this.key;
    }

    public int getAction() {
        return this.action;
    }

    protected class getkey extends ZeroArgFunction {

        private KeyEvent e;

        public getkey(KeyEvent e) {
            this.e = e;
        }

        public LuaValue call() {
            if (e.getKey() == GLFW_KEY_W || e.getKey() == GLFW_KEY_UP) {
                return LuaValue.valueOf("up");
            }
            else if (e.getKey() == GLFW_KEY_S || e.getKey() == GLFW_KEY_DOWN) {
                return LuaValue.valueOf("down");
            }
            else if (e.getKey() == GLFW_KEY_A || e.getKey() == GLFW_KEY_LEFT) {
                return LuaValue.valueOf("left");
            }
            else if (e.getKey() == GLFW_KEY_D || e.getKey() == GLFW_KEY_RIGHT) {
                return LuaValue.valueOf("right");
            }
            else if (e.getKey() == GLFW_KEY_ESCAPE) {
                return LuaValue.valueOf("escape");
            }
            else if (e.getKey() == GLFW_KEY_LEFT_SHIFT) {
                return LuaValue.valueOf("shift");
            }
            return LuaValue.valueOf(e.getKey());
        }

    }

    protected class getaction extends ZeroArgFunction {

        private KeyEvent e;

        public getaction(KeyEvent e) {
            this.e = e;
        }

        public LuaValue call() {
            if (e.getAction() == KeyEvent.PRESSED) {
                return LuaValue.valueOf("pressed");
            }
            return LuaValue.valueOf("released");
        }

    }


}
