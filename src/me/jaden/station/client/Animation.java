package me.jaden.station.client;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Created by Jaden on 8/1/2015.
 */
public class Animation {

    private SpriteSheet s;
    private double time;

    private int frame;

    private boolean stop;

    private int start, end;

    private boolean reversed;

    private double count;

    public static final int LOOP = 0, ONCE = 1, REVERSE = 2;

    private int mode;

    private LuaTable luaAnim;

    public Animation(SpriteSheet s, double time) {
        this.s = s;
        this.time = time;

        this.frame = 0;

        this.stop = true;

        this.start = 0;
        this.end = 0;

        this.reversed = false;

        this.count = 0;

        this.mode = LOOP;

        createLuaTable();
    }

    private void createLuaTable() {
        this.luaAnim = LuaTable.tableOf();

        this.luaAnim.set("play", new playf(this));
        this.luaAnim.set("stop", new stopf(this));
        this.luaAnim.set("reset", new resetf(this));
        this.luaAnim.set("setZone", new setzone(this));
        this.luaAnim.set("setTime", new settime(this));
        this.luaAnim.set("setFrame", new setframe(this));
        this.luaAnim.set("setMode", new setmode(this));
    }

    public LuaTable getLuaTable() {
        return this.luaAnim;
    }

    public void setTime(double t) {
        this.time = t;
    }

    public double getTime() {
        return this.time;
    }

    public void setFrame(int f) {
        this.frame = f;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setZone(int start, int end) {
        this.start = start;
        if (end < start) {
            this.reversed = true;
        }
        else {
            this.reversed = false;
        }
        this.end = end;
        this.frame = start;
    }

    public void play() {
        this.frame = this.start;
        this.stop = false;
    }

    public void reset() {
        this.count = 0;
        this.frame = 0;
        this.stop = true;
    }

    public void stop() {
        this.stop = true;
    }

    public void update(double delta) {
        if (!this.stop) {
            this.count += delta;
            if (this.count >= this.time) {
                if (this.reversed) {
                    this.frame--;
                } else {
                    this.frame++;
                }
                if (this.getMode() == ONCE) {
                    if (this.frame == this.end) {
                        this.stop();
                    }
                } else if (this.getMode() == LOOP) {
                    if (this.frame == this.end) {
                        this.frame = this.start;
                    }
                }
                this.count = 0;
            }
        }
    }

    public void bind() {
        s.getAidanProofTexture(this.frame).bind();
    }

    protected class playf extends ZeroArgFunction {

        private Animation a;

        public playf(Animation a) {
            this.a = a;
        }

        @Override
        public LuaValue call() {
            a.play();
            return LuaValue.valueOf(0);
        }

    }

    protected class stopf extends ZeroArgFunction {

        private Animation a;

        public stopf(Animation a) {
            this.a = a;
        }

        @Override
        public LuaValue call() {
            a.stop();
            return LuaValue.valueOf(0);
        }

    }

    protected class resetf extends ZeroArgFunction {

        private Animation a;

        public resetf(Animation a) {
            this.a = a;
        }

        @Override
        public LuaValue call() {
            a.reset();
            return LuaValue.valueOf(0);
        }

    }

    protected class setzone extends TwoArgFunction {

        private Animation a;

        public setzone(Animation a) {
            this.a = a;
        }

        @Override
        public LuaValue call(LuaValue s, LuaValue e) {
            a.setZone(s.toint(), e.toint());
            return LuaValue.valueOf(0);
        }

    }

    protected class settime extends OneArgFunction {

        private Animation a;

        public settime(Animation a) {
            this.a = a;
        }

        @Override
        public LuaValue call(LuaValue l) {
            a.setTime(l.todouble());
            return l;
        }

    }

    protected class setframe extends OneArgFunction {

        private Animation a;

        public setframe(Animation a) {
            this.a = a;
        }

        @Override
        public LuaValue call(LuaValue f) {
           this.a.setFrame(f.toint());
            return f;
        }

    }

    protected class setmode extends OneArgFunction {

        private Animation a;

        public setmode(Animation a) {
            this.a = a;
        }

        @Override
        public LuaValue call(LuaValue f) {
            if (f.tojstring().equalsIgnoreCase("loop")) {
                this.a.setMode(Animation.LOOP);
            }
            else if (f.tojstring().equalsIgnoreCase("ONCE")) {
                this.a.setMode(Animation.ONCE);
            }
            return f;
        }

    }

}
