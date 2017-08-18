package Main;

import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Gui.GuiElement;
import Terrains.Ocean;
import Terrains.TerrainSquare;
import Terrains.TerrainTile;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    public static final int X_SQUARES_PER_TILE = 150;
    public static final int Z_SQUARES_PER_TILE = 100;
    public static final int SQUARES_PER_TILE = X_SQUARES_PER_TILE * Z_SQUARES_PER_TILE;
    public static final int X_VERTICES_PER_SQUARE = 5;
    public static final int Z_VERTICES_PER_SQUARE = 5;
    public static final int VERTICES_PER_SQUARE = X_VERTICES_PER_SQUARE * Z_VERTICES_PER_SQUARE;
    public static final int TILE_WIDTH = X_SQUARES_PER_TILE * X_VERTICES_PER_SQUARE;
    public static final int TILE_HEIGHT = Z_SQUARES_PER_TILE * Z_VERTICES_PER_SQUARE;
    public static final int VERTICES_PER_TILE = TILE_HEIGHT * TILE_WIDTH;
    public static final int NUMBER_OF_TILES_X = 1;
    public static final int NUMBER_OF_TILES_Y = 1;
    public static final int WIDTH = NUMBER_OF_TILES_X * TILE_HEIGHT;
    public static final int HEIGHT = NUMBER_OF_TILES_Y * TILE_WIDTH;

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
        light = new Light(new Vector3f(512,500,512), new Vector3f(1,1,1));
        loader = new Loader();
        renderer = new Renderer(camera);
        player = new Player(new Vector3f(WIDTH/2,0,HEIGHT/2), camera, loader, renderer);
        camera.followPlayer(player);
        Random random = new Random();
        TerrainTile[][] terrainTiles = new TerrainTile[NUMBER_OF_TILES_X][NUMBER_OF_TILES_Y];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                terrainTiles[j][i] = new TerrainTile(j, i);
            }
        }
        RawModel outlines = terrainTiles[0][0].getSquareOutlines();
        Ocean ocean = new Ocean();
        List<Entity> entities = new ArrayList<>();
        List<GuiElement> guiElements = new ArrayList<>();
        guiElements.add(new GuiElement(new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f)));
//        for (int i = 0; i < 1000; i++) {
//            int x = random.nextInt(1000);
//            int z = random.nextInt(1000);
//            entities.add(new Entity(texturedModel, new Vector3f(x, TerrainTile.getHeightRelCoords(x, z), z), 0f, 0f, 0f, 0.1f));
//        }
        mousePicker = new MousePicker(camera, window.getWindowID(), terrainTiles);
        input = new Input(camera, window.getWindowID(), player, terrainTiles, mousePicker, light);
        gameLoop(entities, guiElements, terrainTiles, ocean, light, outlines);
        cleanUp();
    }

    private void gameLoop(List<Entity> entities, List<GuiElement> guiElements, TerrainTile[][] terrainTiles,
                          Ocean ocean, Light light, RawModel outlines) {
        long delta = 0;
        long sysTime = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window.getWindowID())) {
            delta += (System.currentTimeMillis() - sysTime);
            sysTime = System.currentTimeMillis();
            if (delta > 1000 / 120) {
                //System.out.println("delta: " + delta);
                delta = 0;
                update(terrainTiles, light);
                render();
                for (Entity entity : entities) {
                    if (entity.getPosition().x < player.getPosition().x + 100
                            && entity.getPosition().x > player.getPosition().x - 100
                            && entity.getPosition().z < player.getPosition().z + 100
                            && entity.getPosition().z > player.getPosition().z - 100)
                        renderer.renderEntity(entity, light);
                }
                renderer.renderOcean(ocean, light);
                for (TerrainTile[] terrainTiles2 : terrainTiles) {
                    for (TerrainTile terrainTile : terrainTiles2) {
                        renderer.renderTerrain(terrainTile, light);
                    }
                }
                renderer.renderOutline(outlines);
                for (GuiElement guiElement : guiElements) {
                    renderer.renderGui(guiElement);
                }
                window.swapBuffers();
            }
        }
    }

    public void update(TerrainTile[][] terrainTiles, Light light) {
        window.update();
        camera.calculateCameraPosition();

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
