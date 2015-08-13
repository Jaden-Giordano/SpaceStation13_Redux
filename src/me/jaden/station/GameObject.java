package me.jaden.station;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import info.rockscode.util.Texture;
import info.rockscode.util.Vector2f;
import me.jaden.station.components.AnimationComponent;
import me.jaden.station.components.RenderComponent;
import me.jaden.station.events.InteractEvent;
import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.TextureLoader;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Created by Jaden on 7/20/2015.
 */
public abstract class GameObject {

    protected Vector2f pos;
    protected boolean solid;

    protected RenderComponent renderComponent;

    protected LuaValue lua;
    protected LuaTable luaTable;

    public GameObject() {
        pos = new Vector2f(0, 0);

        createLuaTable();

        this.solid = false;
    }

    public void onBeginPlay() {
        this.runLuaFunc("beginPlay");
    }

    public void update() {
        this.runLuaFunc("update");
    }

    public void update(double delta) {
        if (this.renderComponent != null) {
            this.renderComponent.update(delta);
        }
        this.runLuaFunc("update", LuaValue.valueOf(delta));
    }

    public void render() {
        if (renderComponent != null) {
            renderComponent.render();
        }
    }

    public void cleanUp() {
        if (renderComponent != null) {
            renderComponent.cleanUp();
        }
    }

    public void reInit() {
        if (renderComponent != null) {
            renderComponent.reconstruct();
        }
    }

    public void onInteract(InteractEvent e) {
        this.runLuaFunc("onInteract", e.getLuaTable());
    }

    private void createLuaTable() {
        luaTable = LuaValue.tableOf();

        luaTable.set("getPos", new getpos(this));
        luaTable.set("setPos", new setpos(this));
        luaTable.set("setSolid", new setsolid(this));
        luaTable.set("isSolid", new issolid(this));
        luaTable.set("move", new movef(this));
        luaTable.set("getWorld", new getworld());
        luaTable.set("attachScript", new attachscript(this));
        luaTable.set("attachImage", new attachimage(this));
        luaTable.set("attachAnimation", new attachanimation(this));
        luaTable.set("getAnimation", new getanimation(this));
        luaTable.set("getGuiHandler", new getguihandler());
        luaTable.set("attachImageFromSheet", new attachimagefromsheet(this));
    }

    public Vector2f getPosition() {
        return this.pos;
    }

    public void setPosition(Vector2f v) {
        this.pos = v;
    }

    public GameObject attachRenderComponent(Texture t) {
        this.renderComponent = new RenderComponent(this);
        this.renderComponent.setTexture(t);
        return this;
    }

    public void attachAnimation(SpriteSheet s, double time) {
        this.renderComponent = new AnimationComponent(this);
        ((AnimationComponent) this.renderComponent).setAnimation(new Animation(s, time));
    }

    public AnimationComponent getAnimationComp() {
        if (this.renderComponent != null && this.renderComponent instanceof AnimationComponent) {
            return (AnimationComponent) this.renderComponent;
        }
        return null;
    }

    public void attachLua(String path) {
        try {
            this.lua = JsePlatform.standardGlobals();
            this.lua.get("dofile").call(LuaValue.valueOf(path + ".lua"));
        } catch (Exception e) {
            e.printStackTrace();
            Station.instance.getLogger().log("Unable to find "+path+".lua");
        }
        this.runLuaFunc("init");
    }

    public LuaTable getLuaTable() {
        return this.luaTable;
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

    public void keyInput(KeyEvent e) {
        this.runLuaFunc("keyInput", e.getLuaTable());
    }

    public void mouseInput(MouseEvent e) {
        this.runLuaFunc("mouseInput", e.getLuaTable());
    }

    public void setSolid(boolean b) {
        this.solid = b;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public void move(Vector2f delta) {
        Vector2f temp = this.getPosition().add(delta);
        if (!Station.instance.getGame().getWorld().overlaps(temp)) {
            this.setPosition(temp);
        }
    }

    protected class getpos extends ZeroArgFunction {

        private GameObject o;

        public getpos(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call() {
            LuaTable t = tableOf();

            t.set("x", LuaValue.valueOf(o.getPosition().x));
            t.set("y", LuaValue.valueOf(o.getPosition().y));

            return t;
        }

    }

    protected class setpos extends TwoArgFunction {

        private GameObject o;

        public setpos(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue x, LuaValue y) {
            o.setPosition(new Vector2f(x.tofloat(), y.tofloat()));

            return LuaValue.valueOf(0);
        }

    }

    protected class setsolid extends OneArgFunction {

        private GameObject o;

        public setsolid(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue x) {
            o.setSolid(x.toboolean());
            return LuaValue.valueOf(0);
        }

    }

    protected class issolid extends ZeroArgFunction {

        private GameObject o;

        public issolid(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call() {
            return LuaValue.valueOf(o.isSolid());
        }

    }

    protected class movef extends TwoArgFunction {

        private GameObject t;

        public movef(GameObject t) {
            this.t = t;
        }

        @Override
        public LuaValue call(LuaValue x, LuaValue y) {
            t.move(new Vector2f(x.tofloat(), y.tofloat()));
            return LuaValue.valueOf(0);
        }

    }

    protected class getworld extends ZeroArgFunction {

        public getworld() { }

        @Override
        public LuaValue call() {
            return Station.instance.getGame().getWorld().getLuaTable();
        }

    }

    protected class getguihandler extends ZeroArgFunction {

        public getguihandler() { }

        @Override
        public LuaValue call() {
            return Station.instance.getGame().getGuiHandler().getLuaTable();
        }

    }

    protected class attachscript extends OneArgFunction {

        private GameObject o;

        public attachscript(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue path) {
            String s = path.tojstring();
            s = s.replaceAll("/", Matcher.quoteReplacement(File.separator));
            s = Constants.luaPath + s;
            o.attachLua(s);
            return path;
        }

    }

    protected class attachimage extends OneArgFunction {

        private GameObject o;

        public attachimage(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue path) {
            String s = path.tojstring();
            s = s.replaceAll("/", Matcher.quoteReplacement(File.separator));
            s = Constants.assetsPath + s;
            o.attachRenderComponent(TextureLoader.loadTexture(s));
            return path;
        }

    }

    protected class attachimagefromsheet extends TwoArgFunction {

        private GameObject o;

        public attachimagefromsheet(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue id, LuaValue sheet) {
            this.o.attachRenderComponent(TextureLoader.getTileFromSheet(id.toint(), sheet.tojstring()));
            return LuaValue.valueOf(0);
        }

    }

    protected class attachanimation extends TwoArgFunction {

        private GameObject o;

        public attachanimation(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue path, LuaValue t) {
            String s = path.tojstring();
            s = s.replaceAll("/", Matcher.quoteReplacement(File.separator));
            s = Constants.assetsPath + s;
            o.attachAnimation(new SpriteSheet(s), t.todouble());
            return o.getAnimationComp().getAnimation().getLuaTable();
        }

    }

    protected class getanimation extends ZeroArgFunction {

        private GameObject o;

        public getanimation(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call() {
            if (this.o.getAnimationComp() != null && this.o.getAnimationComp().getAnimation() != null) {
                return this.o.getAnimationComp().getAnimation().getLuaTable();
            }
            return LuaValue.NIL;
        }

    }

}
