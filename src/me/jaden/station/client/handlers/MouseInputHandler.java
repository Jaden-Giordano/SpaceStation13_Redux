package me.jaden.station.client.handlers;

import me.jaden.station.client.Station;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

/**
 * Created by Jaden on 7/21/2015.
 */
public class MouseInputHandler {

    public GLFWMouseButtonCallback mouseButtonCallback;
    public GLFWCursorPosCallback cursorPosCallback;

    public MouseInputHandler() { // Sets up glfw's mouseButtonCallBack & cursorPosCallback
        glfwSetMouseButtonCallback(Station.instance.window, mouseButtonCallback = new GLFWMouseButtonCallback() {

            public void invoke(long window, int button, int action, int mods) { // Invoked on mouseButtonEvent (Click, Drag, Release ect.)
                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1), yPos = BufferUtils.createDoubleBuffer(1);
                    glfwGetCursorPos(window, xPos, yPos);
                    System.out.println("m1 pressed @ X:" + xPos.get() + " Y: " + yPos.get());
                }
                if (action == GLFW_PRESS) {
                    DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1), yPos = BufferUtils.createDoubleBuffer(1);
                    glfwGetCursorPos(window, xPos, yPos);
                    Station.instance.mousePressed(button, xPos.get(), yPos.get());
                }
            }
        });

        glfwSetCursorPosCallback(Station.instance.window, cursorPosCallback = new GLFWCursorPosCallback() {

            public void invoke(long window, double xPos, double yPos) { // Invoked on cursorMove
                // System.out.println(xPos + ", " + yPos);
            }
        });
    }

    public void setWindow(long window) {
        glfwSetMouseButtonCallback(window, mouseButtonCallback);
        glfwSetCursorPosCallback(window, cursorPosCallback);
    }

    public void release() {
        mouseButtonCallback.release();
        cursorPosCallback.release();
    }

}
