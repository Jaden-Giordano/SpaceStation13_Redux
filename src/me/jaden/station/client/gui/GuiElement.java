package me.jaden.station.client.gui;

import info.rockscode.util.Vector2f;

/**
 * Created by Jaden on 8/4/2015.
 */
public abstract class GuiElement {

    Runnable click;
    Runnable update;

    protected Vector2f pos;
    protected float width, height;

    public GuiElement() {
        click = new Runnable() {
            @Override
            public void run() {

            }
        };
        update = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    public Vector2f getPos() {
        return this.pos;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setClickFunc(Runnable c) {
        this.click = c;
    }

    public void setUpdateFunc(Runnable u) {
        this.update = u;
    }

    public void update(double delta) {
        update.run();
    }
    public void render() {

    }
    public void reconstruct() {

    }
    public void cleanUp() {

    }
    public void onClick() {
        this.click.run();
    }

}
