package me.jaden.station.client.handlers;

import me.jaden.station.client.Station;
import me.jaden.station.client.tools.Constants;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Jaden on 7/21/2015.
 */
public class WindowEventHandler {

    public GLFWWindowSizeCallback windowSizeCallback;

    public WindowEventHandler() { // Sets up glfw's keyCallback
        glfwSetWindowSizeCallback(Station.instance.window, windowSizeCallback = new GLFWWindowSizeCallback() {

            public void invoke(long window, int width, int height) {
                Constants.width = width;
                Constants.height = height;
                Station.instance.resized();
            }
        });
    }

    public void setWindow(long window) {
        glfwSetWindowSizeCallback(window, windowSizeCallback);
    }

    public void release() {
        windowSizeCallback.release();
    }

}
