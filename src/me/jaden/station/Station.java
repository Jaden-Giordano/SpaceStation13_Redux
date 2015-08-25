package me.jaden.station;

import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.handlers.ErrorHandler;
import me.jaden.station.handlers.KeyInputHandler;
import me.jaden.station.handlers.MouseInputHandler;
import me.jaden.station.handlers.WindowEventHandler;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.TextureLoader;
import me.jaden.station.tools.TileRegistry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


/**
 * Created by Jaden on 7/14/2015.
 */
public class Station {

    public static Station instance;

    private Logger log;

    private static Game game;

    // Window definition
    public long window;

    public static ErrorHandler errorHandler = new ErrorHandler();
    public static KeyInputHandler keyHandler;
    public static MouseInputHandler mouseHandler;
    public static WindowEventHandler windowHandler;

    // Started when run (called from main)
    public void run() {
        try {
            Constants.init();
            initGLFW();
            loop();

            game.cleanUp();
            exitGame();
        } finally {
            glfwTerminate();
            ErrorHandler.errorCallback.release();
        }
    }

    /**
     *
     * Initiates a new window
     *
     */
    private void initGLFW() {

        // Initialise glfw
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to init GLFW");
        }

        // TODO: Handle Resize

        setDisplayMode(Constants.width, Constants.height, Constants.fsMode);
        mouseHandler = new MouseInputHandler();
        keyHandler = new KeyInputHandler();
        windowHandler = new WindowEventHandler();
    }

    // Stops all glfw window stuff
    private void exitGame() {
        glfwDestroyWindow(window);

        keyHandler.release();
        mouseHandler.release();
        windowHandler.release();
    }

    // Toggle full screen
    public void toggleFullscreen() {
        if (Constants.fsMode != Constants.FULLSCREEN) {
            ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            setDisplayMode(GLFWvidmode.width(vidmode), GLFWvidmode.height(vidmode), Constants.FULLSCREEN);
        } else {
            setDisplayMode(Constants.width, Constants.height, Constants.WINDOWED);
        }
    }

    public void setDisplayMode(int width, int height) {
        setDisplayMode(width, height, Constants.fsMode);
    }

    public void setDisplayMode(int width, int height, int fsMode) {
        Constants.width = width;
        Constants.height = height;

        if (fsMode == Constants.fsMode && window != NULL) {
            glfwSetWindowSize(window, width, height);
            return;
        }

        Constants.fsMode = fsMode;

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        if (fsMode == Constants.BORDERLESS) {
            glfwWindowHint(GLFW_DECORATED, GL_FALSE);
        }

        long newWindow = GLFW.glfwCreateWindow(width, height, Constants.title + "  " + Constants.version, fsMode == Constants.FULLSCREEN ? glfwGetPrimaryMonitor() : NULL, window);

        // These need to be set after creating the window as well as before if returning
        Constants.width = width;
        Constants.height = height;
        // I don't know why; it makes 0 sense.

        if (window != NULL) {
            glfwDestroyWindow(window);
        }

        window = newWindow;

        if (window == NULL) {
            throw new RuntimeException("GLFW Window Creation Failed");
        }

        if (keyHandler != null && mouseHandler != null && windowHandler != null) {
            keyHandler.setWindow(window);
            mouseHandler.setWindow(window);
            windowHandler.setWindow(window);
        }

        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0); // TODO: Add vsync config
        GLContext.createFromCurrent();
        resized();
    }

    public void resized() {
        double ratio = (double) Constants.width / (double) Constants.height;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Constants.width, Constants.height, 0, 11, -1);
        glViewport(0, 0, Constants.width, Constants.height);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);
        if (game != null) {
            game.reInit();
        }
    }

    // Main game loop
    private void loop() {
        glClearColor(0.0F, 0.0F, 0.0F, 0.0F);

        log = new Logger("[LOG]");
        TextureLoader.init();
        TileRegistry.initRegistry();

        game = new Game();

        // ups / fps management
        long now, lastTime = System.nanoTime(), timer = System.currentTimeMillis();
        final double ns = 1000000000.0f;
        double delta = 0;
        int fps = 0;
        while (glfwWindowShouldClose(window) == GL_FALSE) {
            now = System.nanoTime();
            delta = (now - lastTime) / ns;
            lastTime = now;
            glfwPollEvents();
            instance.getGame().update(delta); // Updates game object, propagates to Components to perform physics ect.
            Timer.updateAllTimers(delta);
            // Draw frame
            render();
            fps++;
            if (System.currentTimeMillis() - timer > 1000) {
                // Display fps
                timer += 1000;
                glfwSetWindowTitle(window, Constants.title + "  " + Constants.version + " @ " + fps + "fps");
                fps = 0;
            }
        }
    }

    // Do render
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Un-sure yet if needed - may leave trails

        glEnable(GL_TEXTURE_2D);

        game.render(); // Updates game object, propagates to Components to perform draw / animations ect.

        glDisable(GL_TEXTURE_2D);

        glfwSwapBuffers(window);
    }

    public Game getGame() {
        return game;
    }

    public Logger getLogger() {
        return this.log;
    }

    public void keyPressed(int key) {
        KeyEvent e = new KeyEvent(key, KeyEvent.PRESSED);
        game.keyInput(e);
    }

    public void keyReleased(int key) {
        KeyEvent e = new KeyEvent(key, KeyEvent.RELEASED);
        game.keyInput(e);
    }

    public void mousePressed(int button, double x, double y) {
        MouseEvent e = new MouseEvent(button, x, y);
        Station.instance.getLogger().log(String.valueOf(button));
        game.mouseInput(e);
    }

    public static void main(String[] args) {
        instance = new Station();
        instance.run();
    }

}
