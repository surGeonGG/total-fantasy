package Main;

import Entities.Light;
import Entities.Player;
import Gui.Gui;
import Gui.GuiElement;
import Terrains.Terrain;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private Camera camera;
    private Gui gui;
    private Player player;
    private Light light;
    private Terrain[][] terrains;
    private MousePicker mousePicker;
    private GuiElement focusedElement;
    private long windowID;
    private long rebuildTimer = 0;
    private long moveTimer = 0;
    private long zoomTimer = 0;
    private long clickTimer = 0;

    public Input(Camera camera, long windowID, Player player, Terrain[][] terrains, MousePicker mousePicker, Light light,
                 Gui gui) {
        this.camera = camera;
        this.windowID = windowID;
        this.player = player;
        this.terrains = terrains;
        this.light = light;
        this.mousePicker = mousePicker;
        focusedElement = null;

        glfwSetMouseButtonCallback(windowID, (long window, int button, int action, int mods) -> {
            if (gui.guiWindowAtMouseCoord()) {
                focusedElement = gui.getGuiElementAtMouseCoord();
                focusedElement.handleMouseInput(button, action, mods);
                return;
            }
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
                        if (gui.guiWindowAtMouseCoord()) {
                            focusedElement = gui.getGuiElementAtMouseCoord();
                            focusedElement.handleMouseInput(button, action, mods);
                        }
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
            boolean handled = focusedElement.handleKeyboardInput(key, scancode, action, mods);
            if (handled) {
                return;
            }
            switch (key) {
                case GLFW_KEY_A:
                    if (action == GLFW_PRESS)
                        camera.setYawRate(-5);
                    if (action == GLFW_RELEASE)
                        camera.setYawRate(0);
                    break;
                case GLFW_KEY_D:
                    if (action == GLFW_PRESS)
                        camera.setYawRate(5);
                    if (action == GLFW_RELEASE)
                        camera.setYawRate(0);
                    break;
                case GLFW_KEY_W:
                    if (action == GLFW_PRESS)
                        camera.setPitchRate(5);
                    if (action == GLFW_RELEASE)
                        camera.setPitchRate(0);
                    break;
                case GLFW_KEY_S:
                    if (action == GLFW_PRESS)
                        camera.setPitchRate(-5);
                    if (action == GLFW_RELEASE)
                        camera.setPitchRate(0);
                    break;
                case GLFW_KEY_KP_ADD:
                    if (action == GLFW_PRESS)
                        camera.setZoomRate(15);
                    if (action == GLFW_RELEASE)
                        camera.setZoomRate(0);
                    break;
                case GLFW_KEY_KP_SUBTRACT:
                    if (action == GLFW_PRESS)
                        camera.setZoomRate(-15);
                    if (action == GLFW_RELEASE)
                        camera.setZoomRate(0);
                    break;
                case GLFW_KEY_SPACE:
                    camera.setPitch(90);
                    camera.setYaw(0);
                    break;
                case GLFW_KEY_L:
                    if (action == GLFW_PRESS) {
                        terrains[0][0].toggleLines();
                    }
                    break;
                case GLFW_KEY_F5:
                    if (action == GLFW_PRESS) {
                        terrains[0][0].reload();
                    }
                    break;
            }
        });
    }
}
