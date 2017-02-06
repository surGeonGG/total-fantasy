package Main;

import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Gui.GuiElement;
import Terrains.Ocean;
import Terrains.Terrain;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    public static final float HEIGHT_MULTIPLIER = 20;
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
    private Light light;

    public Game(Window window) {
        this.window = window;
        camera = new Camera(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        light = new Light(new Vector3f(512,5000,512), new Vector3f(1,1,1));
        loader = new Loader();
        renderer = new Renderer(camera);
        player = new Player(new Vector3f(500,1,500), camera, loader, renderer);
        camera.followPlayer(player);
        Terrain.generateTerrain();
        Random random = new Random();
//        RawModel rawModel = loader.loadOBJ("pine");
//        ByteBuffer texByteBuffer = loader.loadImageFileToByteBuffer("pine.png");
//        Texture texture = loader.create2DTextureFromByteBuffer(texByteBuffer, 512, 512);
//        TexturedModel texturedModel = new TexturedModel(rawModel, texture);
        Terrain[][] terrains = new Terrain[NUMBER_OF_TILES_X][NUMBER_OF_TILES_Y];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                terrains[j][i] = new Terrain(renderer, j, i);
            }
        }
        Ocean ocean = new Ocean();
        List<Entity> entities = new ArrayList<>();
        List<GuiElement> guiElements = new ArrayList<>();
        guiElements.add(new GuiElement(new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f)));
//        for (int i = 0; i < 1000; i++) {
//            int x = random.nextInt(1000);
//            int z = random.nextInt(1000);
//            entities.add(new Entity(texturedModel, new Vector3f(x, Terrain.getHeight(x, z), z), 0f, 0f, 0f, 0.1f));
//        }
        mousePicker = new MousePicker(camera, window.getWindowID(), terrains);
        input = new Input(camera, window.getWindowID(), player, terrains, mousePicker, light);
        gameLoop(entities, guiElements, terrains, ocean, light);
        cleanUp();
    }

    private void gameLoop(List<Entity> entities, List<GuiElement> guiElements, Terrain[][] terrains, Ocean ocean, Light light) {
        long delta = 0;
        long sysTime = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window.getWindowID())) {
            delta += (System.currentTimeMillis() - sysTime);
            sysTime = System.currentTimeMillis();
            if (delta > 1000 / 120) {
                //System.out.println("delta: " + delta);
                delta = 0;
                update(terrains, light);
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
                for (GuiElement guiElement : guiElements) {
                    renderer.renderGui(guiElement);
                }
                window.swapBuffers();
            }
        }
    }

    public void update(Terrain[][] terrains, Light light) {
        window.update();
        input.update();
        camera.calculateCameraPosition();
        terrains[0][0].changeSeason(0.0001f);
        light.advanceTime(0.001f);

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
