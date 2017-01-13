package Main;

import Entities.Light;
import Entities.Player;
import Gui.Gui;
import Terrains.Terrain;
import org.joml.Vector3f;

import static Main.Main.WINDOW_HEIGHT;
import static Main.Main.WINDOW_WIDTH;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    Terrain terrain;
    private Camera camera;
    private Window window;
    private MousePicker mousePicker;
    private Input input;
    private Renderer renderer;
    private Player player;
    private Loader loader;
    private Gui gui;
    private Light light;
    private Shader shader;

    public Game(Window window) {
        this.window = window;
        camera = new Camera(WINDOW_WIDTH, WINDOW_HEIGHT);
//        gui = new Gui(WINDOW_WIDTH, WINDOW_HEIGHT, this);
//        gui.run(window.getWindowID());
//        generalShader = new Shader("general");
//        generalShader.bind();
//        generalShader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        light = new Light(new Vector3f(500,100,100), new Vector3f(1,0.8f,1));
        shader = new Shader("player");
        shader.bind();
        shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        loader = new Loader();
        renderer = new Renderer(camera);
        player = new Player(new Vector3f(500,1,500), camera, loader, renderer, shader);
        camera.followPlayer(player);
        SimplexNoiseGenerator simplexNoiseGenerator = new SimplexNoiseGenerator();
        float[][] noiseMap = simplexNoiseGenerator.buildNoise(1000, 1000);
        terrain = new Terrain(camera, shader, loader, renderer, 5, 5, noiseMap);
        mousePicker = new MousePicker(camera, window.getWindowID(), terrain);
        input = new Input(camera, window.getWindowID(), player, terrain, mousePicker, light);
        gameLoop();
        cleanUp();
    }

    private void updateShaders() {
        shader.setUniform("lightPosition", light.getPosition());
        shader.setUniform("lightColor", light.getColor());

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
        terrain.update();
        updateShaders();
    }

    public void render(){
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        terrain.render();
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
