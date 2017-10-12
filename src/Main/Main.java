package Main;

import Database.SQLite;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;


public class Main {

    public static final int WINDOW_WIDTH = 1920, WINDOW_HEIGHT = 1080;
    public static final float ASPECT_RATIO = WINDOW_WIDTH / WINDOW_HEIGHT;
    private static SQLite sqLite;
    private static Game game;
    private static Window window;

    public Main() {
        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize");
            System.exit(1);
        }
        window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, "Game");
        sqLite = new SQLite();
        sqLite.getConnection();
        sqLite.getEvent("old_man");
        glEnable(GL_DEPTH_TEST);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_MULTISAMPLE);
        glCullFace(GL_BACK);
        glClearColor(0.53f, 0.8f, 0.92f, 1);
        game = new Game(window);
    }

    public static void main(String[] args) {

//        recursionTest(0);
//        System.out.println((int) (78/0.9f));
        new Main();
    }

    private static void recursionTest(int c) {
        System.out.println(c++);
        recursionTest(c);
    }


}
