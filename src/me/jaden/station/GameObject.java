package me.jaden.station;

import info.rockscode.util.Texture;
import info.rockscode.util.Vector2f;
import info.rockscode.util.Vector3f;
import me.jaden.station.components.AnimationComponent;
import me.jaden.station.components.RenderComponent;
import me.jaden.station.events.InteractEvent;
import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.TextureLoader;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Created by Jaden on 7/20/2015.
 */
public abstract class GameObject {

    protected Vector3f pos;
    protected boolean solid;

    protected RenderComponent renderComponent;

    protected LuaObject lua;
    protected LuaTable luaTable;

    public GameObject() {
        pos = new Vector3f(0, 0, 0);

        createLuaTable();

        this.solid = false;
    }

    public void onBeginPlay() {
        if (this.lua != null) {
            this.lua.runLuaFunc("beginPlay");
        }
    }

    public void update() {
        this.lua.runLuaFunc("update");
    }

    public void update(double delta) {
        if (this.renderComponent != null) {
            this.renderComponent.update(delta);
        }
        if (this.lua != null) {
            this.lua.runLuaFunc("update", LuaValue.valueOf(delta));
        }
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
        if (this.lua != null) {
            this.lua.runLuaFunc("onInteract", e.getLuaTable());
        }
    }

    private void createLuaTable() {
        luaTable = LuaValue.tableOf();

        // parent.getPos()

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
        luaTable.set("callFunction", new callfunction(this));
    }

    public Vector3f getPosition() {
        return this.pos;
    }

    public void setPosition(Vector3f v) {
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
        this.lua = new LuaObject(path, this.getLuaTable());
    }

    public LuaTable getLuaTable() {
        return this.luaTable;
    }

    public void keyInput(KeyEvent e) {
        this.lua.runLuaFunc("keyInput", e.getLuaTable());
    }

    public void mouseInput(MouseEvent e) {
        this.lua.runLuaFunc("mouseInput", e.getLuaTable());
    }

    public void setSolid(boolean b) {
        this.solid = b;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public void move(Vector2f delta) {
        Vector3f temp = this.getPosition().add(new Vector3f(delta.x, delta.y, 0));
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
            t.set("z", LuaValue.valueOf(o.getPosition().z));

            return t;
        }

    }

    protected class setpos extends ThreeArgFunction {

        private GameObject o;

        public setpos(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
            o.setPosition(new Vector3f(x.tofloat(), y.tofloat(), z.tofloat()));

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
            String s = sheet.tojstring();
            s = s.replaceAll("/", Matcher.quoteReplacement(File.separator));
            s = Constants.assetsPath + s;
            this.o.attachRenderComponent(TextureLoader.getTileFromSheet(id.toint(), s));
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

    protected class callfunction extends TwoArgFunction {

        private GameObject o;

        public callfunction(GameObject o) {
            this.o = o;
        }

        @Override
        public LuaValue call(LuaValue func, LuaValue params) {
            return o.lua.callFunction(func.tojstring(), params);
        }

    }

}
