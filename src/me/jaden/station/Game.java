package me.jaden.station;

import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.GuiHandler;
import me.jaden.station.tools.WorldLoader;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Created by Jaden on 7/14/2015.
 */
public class Game {

    private World world;
    private Camera camera;
    private GuiHandler guiHandler;

    private boolean gui;

    private LuaObject lua;
    private LuaTable luaTable;

    public Game() {
        this.createLuaTable();

        this.attachLua(Constants.luaPath + "init");

        if (this.lua != null) {
            this.lua.runLuaFunc("init");
        }

        world = WorldLoader.loadWorld(Constants.savePath+"worlds"+File.separator+"wiremap");
        camera = new Camera();
        guiHandler = new GuiHandler();

        this.gui = false;
    }

    public void update() {
        beginCheck();
        world.update();
    }

    public void update(double delta) {
        beginCheck();
        world.update(delta);
        guiHandler.update(delta);
    }

    private boolean play = false;
    private void beginCheck() {
        if (!play) {
            onBeginPlay();
            play = true;
        }
    }

    public void onBeginPlay() {
        world.onBeginPlay();
    }

    public void render() {
        world.render();
        guiHandler.render();
    }

    public void cleanUp() {
        world.cleanUp();
        guiHandler.cleanUp();
    }

    public void reInit() {
        world.reInit();
        guiHandler.reInit();
    }

    public void keyInput(KeyEvent e) {
        guiHandler.keyInput(e);
        if (!this.gui) {
            world.keyInput(e);
        }
    }

    public void mouseInput(MouseEvent e) {
        guiHandler.mouseInput(e);
        if (!this.gui) {
            world.mouseInput(e);
        }
        this.gui = false;
    }

    public World getWorld() {
        return this.world; // Temp
    }

    public Camera getCamera() {
        return this.camera;
    }

    public GuiHandler getGuiHandler() {
        return this.guiHandler;
    }

    public void guiClick() {
        this.gui = true;
    }

    public LuaTable getLuaTable() {
        return this.luaTable;
    }

    public void attachLua(String path) {
        this.lua = new LuaObject(path, this.getLuaTable());
    }

    private void createLuaTable() {
        this.luaTable = LuaTable.tableOf();

        this.luaTable.set("linkIdToTile", new linkidtotile());
    }

    protected class linkidtotile extends TwoArgFunction {

        public linkidtotile() {}

        @Override
        public LuaValue call(LuaValue id, LuaValue params) {
            if (params.istable() && !params.isnil()) {
                String s = params.get("lua").tojstring();
                s = s.replaceAll("/", Matcher.quoteReplacement(File.separator));
                s = Constants.luaPath + s;
                String si = params.get("image").tojstring();
                si = si.replaceAll("/", Matcher.quoteReplacement(File.separator));
                si = Constants.assetsPath + si;
                WorldLoader.linkIdToTile(id.toint(), s, si, params.get("sheetId").toint());
            }
            else {
                String s = params.tojstring();
                s = s.replaceAll("/", Matcher.quoteReplacement(File.separator));
                s = Constants.luaPath + s;
                WorldLoader.linkIdToTile(id.toint(), s);
            }
            return LuaValue.valueOf(0);
        }

    }

}
