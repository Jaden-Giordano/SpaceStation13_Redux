package me.jaden.station.client.tools;

import com.google.gson.Gson;

import java.io.File;
import java.util.Date;

/**
 *
 * @author Gazguy
 * @description Sets up constants for other classes to reference
 *
 */

public class Constants {

    private static class FileIOContainer {
        int width;
        int height;
        int windowMode;
        int currentScene;

        protected FileIOContainer(int w, int h, int winMode, int cScene) {
            width = w;
            height = h;
            windowMode = winMode;
            currentScene = cScene;
        }
    }

    public static final String OS = System.getProperty("os.name").toUpperCase();
    public static String OSType;

    public static String dataPath, logPath, savePath, assetsPath, luaPath;

    public static String title = "Station - Starting", version = "v0.0.01";
    public static int width = 800, height = 450, WINDOWED = 1, BORDERLESS = 2, FULLSCREEN = 3, fsMode = WINDOWED, currentScene = 0;

    public static final Date startDate = new Date();

    public static Gson gson;

    public static void init() {
        gson = new Gson();
        getOSType();
        getDataPath();
    }

    private static void getOSType() {
        if (OS.toUpperCase().contains("windows".toUpperCase())) {
            OSType = "windows";
        } else if (OS.toUpperCase().contains("mac".toUpperCase())) {
            OSType = "mac";
        } else if (OS.toUpperCase().contains("nix".toUpperCase()) || OS.toUpperCase().contains("nux".toUpperCase()) || OS.toUpperCase().contains("aix".toUpperCase())) {
            OSType = "unix";
        } else if (OS.toUpperCase().contains("sunos".toUpperCase())) {
            OSType = "solaris";
        } else {
            OSType = "unknown";
        }
    }

    private static void getDataPath() {
        if (OSType == "windows") {
            dataPath = System.getProperty("user.home") + "\\AppData" + File.separator + "Roaming" + File.separator + "Station" + File.separator;
        } else if (OSType == "mac" || OSType == "unix" || OSType == "solaris") {
            dataPath = System.getProperty("user.home") + File.separator + "OREGame" + File.separator;
        } else {
            dataPath = Constants.class.getProtectionDomain().getCodeSource().getLocation().toString() + File.separator;
        }

        logPath = dataPath + "logs" + File.separator;
        String workingDir = System.getProperty("user.dir");
        savePath = workingDir + File.separator +  "saves" + File.separator;
        assetsPath = workingDir + File.separator +  "assets" +File.separator;
        luaPath = workingDir + File.separator + "scripts"+File.separator;
    }
}
