package me.jaden.station.handlers;

import me.jaden.station.Station;
import me.jaden.station.tools.Constants;
import org.apache.commons.io.FileUtils;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Jaden on 7/21/2015.
 */
public class ErrorHandler {

    public static GLFWErrorCallback errorCallback;

    SimpleDateFormat dfe = new SimpleDateFormat("ddMMyyHHmmss");
    SimpleDateFormat dfi = new SimpleDateFormat("dd/MM/yy(z) - HH:mm:ss.SS");

    public ErrorHandler() {
        glfwSetErrorCallback(errorCallback = new GLFWErrorCallback() {

            public void invoke(int error, long description) { // Invoked on glfw error
                try {
                    logError(error + " " + description + " | " + new String(ByteBuffer.allocate(8).putLong(description).array(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logError(e);
                    System.out.println("Error Log Failed - See Next 'Error Logged'");
                }
            }
        });
    }

    public void logError(Error error) {
        String msg = null;
        try {
            msg = error.getMessage().toString();
        } catch (NullPointerException e) {
            msg = "NoMSG";
        } finally {
            logError(msg + " " + error.getStackTrace().toString());
        }
    }

    public void logError(Exception exception) {
        String msg = null;
        try {
            msg = exception.getMessage().toString();
        } catch (NullPointerException e) {
            msg = "NoMSG";
        } finally {
            logError(msg + " " + exception.getStackTrace().toString());
        }
    }

    public void logError(String errorText) {
        try {
            File file = new File(Constants.logPath + "errorLog(" + dfe.format(new Date()) + ").txt");
            FileUtils.writeLines(file, Arrays.asList("[" + dfi.format(Constants.startDate) + "] " + errorText), true);
            System.out.println("Error Logged @ " + Constants.logPath + "errorLog(" + dfe.format(Constants.startDate) + ").txt");
        } catch (IOException e) {
            Station.errorHandler.logError(e);
            System.out.println("Error Log Failed - See Next 'Error Logged'");
        }
    }

}
