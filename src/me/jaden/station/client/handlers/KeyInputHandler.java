package me.jaden.station.client.handlers;

import me.jaden.station.client.Station;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

/**
 * Created by Jaden on 7/21/2015.
 */
public class KeyInputHandler {

    public GLFWKeyCallback keyCallback;

    public KeyInputHandler() { // Sets up glfw's keyCallback
        glfwSetKeyCallback(Station.instance.window, keyCallback = new GLFWKeyCallback() {

            public void invoke(long window, int key, int scancode, int action, int mods) { // Invoked on keyEvent (Press, Hold, Release ect.)
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) { // Exit on ESCAPE RELEASE
                    glfwSetWindowShouldClose(window, GL_TRUE);
                } else if (key == GLFW_KEY_ENTER && mods == GLFW_MOD_ALT && action == GLFW_RELEASE) { // Toggle full screen on ALT + ENTER RELEASE
                    System.out.println("Toggle Fullscreen");
                    Station.instance.toggleFullscreen();
                }

                if (action == GLFW_RELEASE) {
                    Station.instance.keyReleased(key);
                }
                if (action == GLFW_PRESS) {
                    Station.instance.keyPressed(key);
                }
            }
        });
    }

    public void setWindow(long window) {
        glfwSetKeyCallback(window, keyCallback);
    }

    public void release() {
        keyCallback.release();
    }

}
