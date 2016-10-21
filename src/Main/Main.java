package Main;

import Database.SQLite;
import Gui.Gui;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {


    public static final float min = -1.0f, max = 1.0f;

    public static final int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = WINDOW_WIDTH - WINDOW_WIDTH/4;

    private static Camera camera;

    private static EventHandler eventHandler;

    private static SQLite sqLite;

    private static World world;

    private static Input input;

    private static Shader shader;

    private static Renderer renderer;

    private static Window window;

    private static Player player;

    private static Loader loader;

    private static Gui gui;

    private static int fps = 0;


    public Main() {

        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize");
            System.exit(1);
        }

        window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, "Game");

        camera = new Camera(WINDOW_WIDTH, WINDOW_HEIGHT);

        gui = new Gui(WINDOW_WIDTH, WINDOW_HEIGHT, this);

        gui.run(window.getWindowID());

        shader = new Shader("world");

        loader = new Loader();

        renderer = new Renderer();

        world = new World(this);

        player = new Player(new Vector3f(300,300,0), this, world.getMapCoords());

        input = new Input(this);

        sqLite = new SQLite();

        sqLite.getConnection();
        sqLite.getEvent("old_man");

        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        
        glClearColor(0.5f, 0.5f, 0.4f, 1);

        gameLoop();

        cleanUp();

    }

    private void cleanUp() {
        gui.nk_glfw3_shutdown();
        glfwFreeCallbacks(window.getWindowID());
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        window.destroy();
    }

    private void gameLoop() {

        long delta = 0;
        long delta2 = 0;
        long sysTime = System.currentTimeMillis();;


        while (!glfwWindowShouldClose(window.getWindowID())) { //Game Loop

            //glEnable(GL_DEPTH_TEST);


            delta += (System.currentTimeMillis() - sysTime);
            sysTime = System.currentTimeMillis();

            if (delta > 1/60) {

                update();
                render();

                window.swapBuffers();

                delta2 += delta;
                delta = 0;
                fps++;

            }
            if (delta2 > 1000) {

                delta2 = 0;
                System.out.println(fps);
                checkCursor();

                fps = 0;
                System.out.println(GL_VERSION);
    
            }
        }
    }

    private void checkCursor() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window.getWindowID(), posX, posY);

        System.out.println(posX.get(0) + ", " + posY.get(0));

    }


    public static void main(String[] args) {

        new Main();

    }

    public static void update() {

        window.update();
        input.update();
        world.update();

    }

    public static void render(){

        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        shader.bind();
        world.render();
        player.render();
        gui.render();

    }


    public static EventHandler getEventHandler() { return eventHandler; }

    public static Camera getCamera() {
        return camera;
    }

    public static Window getWindow() {
        return window;
    }

    public static Shader getShader() {
        return shader;
    }

    public static Gui getGui() { return gui; }

    public static World getWorld() {return world; }

    public static int getWIDTH() { return WINDOW_WIDTH; }

    public static int getHEIGHT() { return WINDOW_HEIGHT; }

    public static Player getPlayer() { return player; }

    public static Renderer getRenderer() {
        return renderer;
    }

    public static Loader getLoader() {
        return loader;
    }
}
