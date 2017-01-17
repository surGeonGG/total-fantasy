package Main;

import Database.SQLite;
import Entities.Player;
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
    public static final int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 1000;
//    private static EventHandler eventHandler;
    private static SQLite sqLite;
//    private static World world;
    private static Game game;
    private static Window window;
    private static int fps = 0;

    public Main() {
        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize");
            System.exit(1);
        }
        window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, "Game");
//        world = new World(this);
//        player = new Player(new Vector3f(500,500,0), this);
        sqLite = new SQLite();
        sqLite.getConnection();
        sqLite.getEvent("old_man");
        glEnable(GL_DEPTH_TEST);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glCullFace(GL_BACK);
        glClearColor(0.5f, 0.5f, 0.4f, 1);
        game = new Game(window);
    }
    public static void main(String[] args) {
        new Main();
    }
}
