package Main;

import Entities.Light;
import Entities.Player;
import Terrains.TerrainTile;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public class Input {

    private Camera camera;
    private Player player;
    private Light light;
    private TerrainTile[][] terrainTiles;
    private MousePicker mousePicker;
    private long windowID;
    private long rebuildTimer = 0;
    private long moveTimer = 0;
    private long zoomTimer = 0;
    private long clickTimer = 0;

    public Input(Camera camera, long windowID, Player player, TerrainTile[][] terrainTiles, MousePicker mousePicker, Light light) {
        this.camera = camera;
        this.windowID = windowID;
        this.player = player;
        this.terrainTiles = terrainTiles;
        this.light = light;
        this.mousePicker = mousePicker;

        glfwSetMouseButtonCallback(windowID, (long window, int button, int action, int mods) -> {
            switch (button) {
                case GLFW_MOUSE_BUTTON_RIGHT:
                    if (System.currentTimeMillis() > clickTimer+200) {
                        mousePicker.update();
                        Vector3f mapPoint = mousePicker.getCurrentTerrainPoint();
                        if (mapPoint != null) {
                            player.setPosition(mapPoint);
                        }
                        clickTimer = System.currentTimeMillis();
                    }
                    break;
                case GLFW_MOUSE_BUTTON_LEFT:
                    if (System.currentTimeMillis() > clickTimer+200) {
                        mousePicker.update();
                        Vector3f mapPoint = mousePicker.getCurrentTerrainPoint();
                        if (mapPoint != null) {
                            player.moveTo(mapPoint);
                        }
                        clickTimer = System.currentTimeMillis();
                    }
                    break;
            }

        });

        glfwSetKeyCallback(windowID, (long window, int key, int scancode, int action, int mods) -> {
            switch (key) {
                case GLFW_KEY_A:
                    if (action == GLFW_PRESS)
                        camera.setYawRate(-1);
                    if (action == GLFW_RELEASE)
                        camera.setYawRate(0);
                    break;
                case GLFW_KEY_D:
                    if (action == GLFW_PRESS)
                        camera.setYawRate(1);
                    if (action == GLFW_RELEASE)
                        camera.setYawRate(0);
                    break;
                case GLFW_KEY_W:
                    if (action == GLFW_PRESS)
                        camera.setPitchRate(1);
                    if (action == GLFW_RELEASE)
                        camera.setPitchRate(0);
                    break;
                case GLFW_KEY_S:
                    if (action == GLFW_PRESS)
                        camera.setPitchRate(-1);
                    if (action == GLFW_RELEASE)
                        camera.setPitchRate(0);
                    break;
                case GLFW_KEY_KP_ADD:
                    if (action == GLFW_PRESS)
                        camera.setZoomRate(1);
                    if (action == GLFW_RELEASE)
                        camera.setZoomRate(0);
                    break;
                case GLFW_KEY_KP_SUBTRACT:
                    if (action == GLFW_PRESS)
                        camera.setZoomRate(-1);
                    if (action == GLFW_RELEASE)
                        camera.setZoomRate(0);
                    break;
                case GLFW_KEY_SPACE:
                    camera.setPitch(90);
                    camera.setYaw(0);
                    break;
            }
        });
    }
}
