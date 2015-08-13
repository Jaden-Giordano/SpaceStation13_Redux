package me.jaden.station;

import info.rockscode.util.Vector2f;

/**
 * Created by Jaden on 7/20/2015.
 */
public class Camera {

    private Vector2f pos;

    public Camera() {
        this.pos = new Vector2f(0, 0);
    }

    public Vector2f getPosition() {
        return this.pos;
    }

}
