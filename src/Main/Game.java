package Main;

import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Gui.Gui;
import Terrains.Ocean;
import Terrains.Terrain;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Main.Main.WINDOW_HEIGHT;
import static Main.Main.WINDOW_WIDTH;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    public static final float HEIGHT_MULTIPLIER = 10;
    public static final int WIDTH = 1024, HEIGHT = 1024;
    public static final int NUMBER_OF_TILES_X = 4;
    public static final int NUMBER_OF_TILES_Y = 4;

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
        light = new Light(new Vector3f(500,500,500), new Vector3f(1,0.8f,1));
        loader = new Loader();
        renderer = new Renderer(camera);
        player = new Player(new Vector3f(500,1,500), camera, loader, renderer);
        camera.followPlayer(player);
        Terrain.generateTerrain();
        Random random = new Random();
        RawModel rawModel = loader.loadOBJ("pine");
        ByteBuffer texByteBuffer = loader.loadImageFileToByteBuffer("pine.png");
        Texture texture = loader.createTextureFromByteBuffer(texByteBuffer, 512, 512);
        TexturedModel texturedModel = new TexturedModel(rawModel, texture);
        Terrain[][] terrains = new Terrain[NUMBER_OF_TILES_X][NUMBER_OF_TILES_Y];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                terrains[j][i] = new Terrain(renderer, j, i);
            }
        }
        Ocean ocean = new Ocean(renderer);
        List<Entity> entities = new ArrayList<>();
//        for (int i = 0; i < 1000; i++) {
//            int x = random.nextInt(1000);
//            int z = random.nextInt(1000);
//            entities.add(new Entity(texturedModel, new Vector3f(x, Terrain.getHeight(x, z), z), 0f, 0f, 0f, 0.1f));
//        }
        mousePicker = new MousePicker(camera, window.getWindowID(), terrains);
        input = new Input(camera, window.getWindowID(), player, terrains, mousePicker, light);
        gameLoop(entities, terrains, ocean);
        cleanUp();
    }

    private void gameLoop(List<Entity> entities, Terrain[][] terrains, Ocean ocean) {
        long delta = 0;
        long sysTime = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window.getWindowID())) {
            delta += (System.currentTimeMillis() - sysTime);
            if (delta > 1 / 60) {
                update();
                render();
                for (Entity entity : entities) {
                    if (entity.getPosition().x < player.getPosition().x + 100
                            && entity.getPosition().x > player.getPosition().x - 100
                            && entity.getPosition().z < player.getPosition().z + 100
                            && entity.getPosition().z > player.getPosition().z - 100)
                        renderer.renderEntity(entity, light);
                }
                renderer.renderOcean(ocean, light);
                for (Terrain[] terrains2 : terrains) {
                    for (Terrain terrain : terrains2) {
//                    if (terrain.getPosition().x < player.getPosition().x + 100
//                            && terrain.getPosition().x > player.getPosition().x - 100
//                            && terrain.getPosition().z < player.getPosition().z + 100
//                            && terrain.getPosition().z > player.getPosition().z - 100)
                        renderer.renderTerrain(terrain, light);
                    }
                }
                window.swapBuffers();
            }
        }
    }

    public void update() {
        window.update();
        input.update();
    }

    public void render(){
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        player.update();
        renderer.renderEntity(player, light);
//        gui.renderEntity();
    }

    private void cleanUp() {
//        gui.nk_glfw3_shutdown();
        glfwFreeCallbacks(window.getWindowID());
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        window.destroy();
    }
}
