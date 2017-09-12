package Main;

import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Gui.Gui;
import Terrains.Ocean;
import Terrains.Terrain;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    public static final int X_SQUARES_PER_TILE = 500;
    public static final int Z_SQUARES_PER_TILE = 350;
    public static final int SQUARE_WIDTH = 5;
    public static final int WIDTH = X_SQUARES_PER_TILE * SQUARE_WIDTH;
    public static final int HEIGHT = Z_SQUARES_PER_TILE * SQUARE_WIDTH;
    public static final int VERTICES_PER_SQUARE = 20;

    public static final int SQUARES_PER_TILE = X_SQUARES_PER_TILE * Z_SQUARES_PER_TILE;

    public static final int NUMBER_OF_TILES_X = 1;
    public static final int NUMBER_OF_TILES_Y = 1;

    private Camera camera;
    private Window window;
    private MousePicker mousePicker;
    private Input input;
    private Renderer renderer;
    private Player player;
    private Loader loader;
    private Light light;

    public Game(Window window) {
        this.window = window;
        camera = new Camera(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        light = new Light(new Vector3f(20000,50000,512), new Vector3f(1,1,1));
        loader = new Loader();
        renderer = new Renderer(camera);
        player = new Player(new Vector3f(WIDTH/2,0,HEIGHT/2), camera, loader, renderer);
        camera.followPlayer(player);
        Random random = new Random();
        Terrain[][] terrains = new Terrain[NUMBER_OF_TILES_X][NUMBER_OF_TILES_Y];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                terrains[j][i] = new Terrain(j, i);
            }
        }
        Ocean ocean = new Ocean();
        List<Entity> entities = new ArrayList<>();
        Gui gui = new Gui();

        long windowID = window.getWindowID();
        mousePicker = new MousePicker(camera, windowID, terrains);
        input = new Input(camera, windowID, player, terrains, mousePicker, light);
        gameLoop(entities, gui, terrains, ocean, light);
        cleanUp();
    }

    private void gameLoop(List<Entity> entities, Gui gui, Terrain[][] terrains,
                          Ocean ocean, Light light) {
        long delta = 0;
        long sysTime = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window.getWindowID())) {
            delta += (System.currentTimeMillis() - sysTime);
            sysTime = System.currentTimeMillis();
            if (delta > 1000 / 120) {
                gui.getGuiElements().get(0).changeText(0, "framerate: " + delta + " ms");
//                System.out.println("delta: " + String.valueOf(delta));
                delta = 0;
                glfwPollEvents();
                camera.calculateCameraPosition();
                player.update();
                glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
                renderer.renderEntity(player, light);
                for (Entity entity : entities) {
                    if (entity.getPosition().x < player.getPosition().x + 100
                            && entity.getPosition().x > player.getPosition().x - 100
                            && entity.getPosition().z < player.getPosition().z + 100
                            && entity.getPosition().z > player.getPosition().z - 100) {
                        renderer.renderEntity(entity, light);
                    }
                }
                renderer.renderOcean(ocean, light);
                for (Terrain[] terrainTiles2 : terrains) {
                    for (Terrain terrain : terrainTiles2) {
                        renderer.renderTerrain(terrain, light);
                        if (terrain.linesShown()) {
                            renderer.renderOutline(terrain.getSquareOutlines());
                        }
                    }
                }

                renderer.renderGui(gui);
                window.swapBuffers();
            }
        }
    }

    private void cleanUp() {
//        gui.nk_glfw3_shutdown();
        glfwFreeCallbacks(window.getWindowID());
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        window.destroy();
    }
}
