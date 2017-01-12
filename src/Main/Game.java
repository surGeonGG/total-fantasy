package Main;

import Entities.Light;
import Entities.Player;
import Gui.Gui;
import org.joml.Vector3f;

import static Main.Main.WINDOW_HEIGHT;
import static Main.Main.WINDOW_WIDTH;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    Map map;
    private Camera camera;
    private Window window;
    private MousePicker mousePicker;
    private Input input;
    private Renderer renderer;
    private Player player;
    private Loader loader;
    private Gui gui;
    private Light light;

    public Game(Window window) {
        this.window = window;
        camera = new Camera(WINDOW_WIDTH, WINDOW_HEIGHT);
//        gui = new Gui(WINDOW_WIDTH, WINDOW_HEIGHT, this);
//        gui.run(window.getWindowID());
//        generalShader = new Shader("general");
//        generalShader.bind();
//        generalShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        Shader shader = new Shader("player");
        shader.bind();
        shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        light = new Light(new Vector3f(50,10,10), new Vector3f(1,1,1));
        loader = new Loader();
        renderer = new Renderer(camera);
        player = new Player(new Vector3f(0,1,0), camera, loader, renderer, shader);
        camera.followPlayer(player);
        map = new Map(camera, shader, loader, renderer, new Vector3f(0,0,0));
        mousePicker = new MousePicker(camera, window.getWindowID(), map);
        input = new Input(camera, window.getWindowID(), player, map, mousePicker);
        gameLoop();
        cleanUp();
    }

    private void gameLoop() {
        long delta = 0;
        long sysTime = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window.getWindowID())) {
            delta += (System.currentTimeMillis() - sysTime);
            if (delta > 1 / 60) {
                update();
                render();
                window.swapBuffers();
            }
        }
    }

    public void update() {
        window.update();
        input.update();
        map.update();
    }

    public void render(){
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        map.render();
        player.render();
//        gui.render();
    }

    private void cleanUp() {
//        gui.nk_glfw3_shutdown();
        glfwFreeCallbacks(window.getWindowID());
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        window.destroy();
    }
}
