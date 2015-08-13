package me.jaden.station.objects;

import info.rockscode.util.Vector2f;
import me.jaden.station.GameObject;
import me.jaden.station.SpriteSheet;
import me.jaden.station.Station;
import me.jaden.station.events.InteractEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.TextureLoader;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.io.File;

/**
 * Created by Jaden on 7/22/2015.
 */
public class Player extends GameObject {

    private String name;

    private SpriteSheet body;

    public static final int UP = 1, DOWN = 0, RIGHT = 2, LEFT = 3;
    private int facing;

    public Player() {
        super();

        this.name = "icyhate";

        this.setPosition(new Vector2f(300, 300));

        this.body = new SpriteSheet(Constants.assetsPath+"player"+File.separator+"human.png");

        this.attachRenderComponent(this.body.getAidanProofTexture(facing + 3));

        this.luaTable.set("getName", new getname(this));
        this.luaTable.set("setName", new setname(this));
        this.luaTable.set("setFacing", new setfacing(this));

        this.attachLua(Constants.luaPath + "player" + File.separator + "controller");
    }

    public void mouseInput(MouseEvent e) {
        super.mouseInput(e);

        Tile t = Station.instance.getGame().getWorld().getTileAtPos(e.getX(), e.getY());
        if (t != null) {
            InteractEvent ie = new InteractEvent(this, t, InteractEvent.CLICK);
            this.onInteract(ie);
            t.onInteract(ie);
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacing(int f) {
        this.facing = f;
        this.renderComponent.setTexture(body.getAidanProofTexture(facing+3));
    }

    public class setname extends OneArgFunction {

        private Player t;

        public setname(Player t) {
            this.t = t;
        }

        @Override
        public LuaValue call(LuaValue s) {
            t.setName(s.tojstring());
            return s;
        }
    }

    public class getname extends ZeroArgFunction {

        private Player t;

        public getname(Player t) {
            this.t = t;
        }

        @Override
        public LuaValue call() {
            return LuaValue.valueOf(t.getName());
        }
    }

    protected class setfacing extends OneArgFunction {

        private Player p;

        public setfacing(Player p) {
            this.p = p;
        }

        @Override
        public LuaValue call(LuaValue f) {
            String face = f.tojstring();
            if (face.equalsIgnoreCase("up")) {
                this.p.setFacing(Player.UP);
            }
            if (face.equalsIgnoreCase("down")) {
                this.p.setFacing(Player.DOWN);
            }
            if (face.equalsIgnoreCase("right")) {
                this.p.setFacing(Player.RIGHT);
            }
            if (face.equalsIgnoreCase("left")) {
                this.p.setFacing(Player.LEFT);
            }

            return f;
        }

    }

}
