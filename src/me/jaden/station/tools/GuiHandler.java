package me.jaden.station.tools;

import info.rockscode.util.Vector2f;
import me.jaden.station.Gui;
import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaden on 8/3/2015.
 */
public class GuiHandler {

    List<Gui> guis;

    LuaTable luaGuiHan;

    public GuiHandler() {
        guis = new ArrayList<Gui>();

        createLuaTable();
    }

    private void createLuaTable() {
        this.luaGuiHan = LuaTable.tableOf();

        this.luaGuiHan.set("createGui", new creategui(this));
    }

    public LuaTable getLuaTable() {
        return this.luaGuiHan;
    }

    public void createGui(Vector2f pos) {
        this.guis.add(new Gui(pos, Constants.width/4, Constants.height/4));
    }

    public void update(double delta) {
        List<Gui> rmv = new ArrayList<Gui>();
        for (Gui i : this.guis) {
            if (i.isClosed()) {
                rmv.add(i);
                i.cleanUp();
            }
        }
        for (Gui i : rmv) {
            this.guis.remove(i);
        }
        rmv.clear();
        for (Gui i : this.guis) {
            i.update(delta);
        }
    }

    public void render() {
        for (Gui i : this.guis) {
            i.render();
        }
    }

    public void reInit() {
        for (Gui i : this.guis) {
            i.reconstruct();
        }
    }

    public void cleanUp() {
        for (Gui i : this.guis) {
            i.cleanUp();
        }
    }

    public void keyInput(KeyEvent e) {
        for (Gui i : this.guis) {
            i.keyInput(e);
        }
    }

    public void mouseInput(MouseEvent e) {
        for (Gui i : this.guis) {
            i.mouseInput(e);
        }
    }

    protected class creategui extends TwoArgFunction {

        private GuiHandler g;

        public creategui(GuiHandler g) {
            this.g = g;
        }

        @Override
        public LuaValue call(LuaValue x, LuaValue y) {
            this.g.createGui(new Vector2f(x.tofloat(), y.tofloat()));
            return LuaValue.valueOf(0); // Send the gui created
        }

    }

}
