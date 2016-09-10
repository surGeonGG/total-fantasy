
import org.lwjgl.nuklear.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Main {


    public static final float min = -1.0f, max = 1.0f;

    public static final int WIDTH = 1600, HEIGHT = 1024;

    private static Camera camera;

    private static World world;

    private static Input input;

    private static Shader shader;

    private static Window window;

    private static int fps = 0;


    public Main() {



        if (!glfwInit()) {
            System.err.println("GLFW failed to initialize");
            System.exit(1);
        }

        window = new Window(WIDTH, HEIGHT, "Game");

        camera = new Camera(WIDTH, HEIGHT);



        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        shader = new Shader("shader");

        glClearColor(0, 0, 0, 1);

        world = new World(this);

        input = new Input(this);

        long delta = 0;
        long delta2 = 0;
        long sysTime = 0;

        int x = 0, y = 0;

        while (!glfwWindowShouldClose(window.getWindowID())) { //Game Loop

            delta += (System.currentTimeMillis() - sysTime);
            sysTime = System.currentTimeMillis();
            if (delta >= 1/60) {
                update();
                render();
                window.swapBuffers();

                delta2 += delta;
                delta = 0;
                fps++;
            }
            if (delta2 >= 1000) {
                delta2 = 0;
                System.out.println(fps);
                fps = 0;
            }
        }

        window.destroy();
        glfwTerminate();

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
        shader.setUniform("green", 1);
        world.render();

    }

    public static Camera getCamera() {
        return camera;
    }

    public static Window getWindow() {
        return window;
    }

    public static Shader getShader() {
        return shader;
    }

    public static World getWorld() {return world; }

    public static int getWIDTH() { return WIDTH; }

    public static int getHEIGHT() { return HEIGHT; }

}
