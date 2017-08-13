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
    }

    public void update() {
        if (glfwGetKey(windowID, GLFW_KEY_A) == GL_TRUE) {
            camera.rotate(-1f);
        }
        if (glfwGetKey(windowID, GLFW_KEY_D) == GL_TRUE) {
            camera.rotate(1f);
        }
        if (glfwGetKey(windowID, GLFW_KEY_S) == GL_TRUE) {
            camera.changePitch(-1f);
        }
        if (glfwGetKey(windowID, GLFW_KEY_W) == GL_TRUE) {
            camera.changePitch(1f);
        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_ADD) == GL_TRUE) {
            if (System.currentTimeMillis() > zoomTimer+20) {
                camera.zoom(2f);
                zoomTimer = System.currentTimeMillis();
            }
        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_SUBTRACT) == GL_TRUE) {
            if (System.currentTimeMillis() > zoomTimer+20) {
                camera.zoom(-2f);
                zoomTimer = System.currentTimeMillis();
            }
        }

//        if (glfwGetKey(windowID, GLFW_KEY_KP_2) == GL_TRUE) {
//            if (camera.getPosition().z >= -1000) camera.addPitch(-1f);
//        }
//
//        if (glfwGetKey(windowID, GLFW_KEY_KP_8) == GL_TRUE) {
//            if (camera.getPosition().z >= -1000) camera.addPitch(1f);
//        }
//
//        if (glfwGetKey(windowID, GLFW_KEY_KP_4) == GL_TRUE) {
//            if (camera.getPosition().z >= -1000) camera.addYaw(-1f);
//        }
//
//        if (glfwGetKey(windowID, GLFW_KEY_KP_6) == GL_TRUE) {
//            if (camera.getPosition().z >= -1000) camera.addYaw(1f);
//        }

        if (glfwGetKey(windowID, GLFW_KEY_SPACE) == GL_TRUE) {
            camera.printPosition();
            player.printPosition();
            camera.setPitch(90);
            camera.setYaw(0);
        }


        if (glfwGetKey(windowID, GLFW_KEY_UP) == GL_TRUE) {
           if (System.currentTimeMillis() > moveTimer+200) {
//                player.move(new Vector3f(0,0,1));
               light.addPosition(new Vector3f(1000,0,0));
               moveTimer = System.currentTimeMillis();
           }
        }

        if (glfwGetKey(windowID, GLFW_KEY_DOWN) == GL_TRUE) {
           if (System.currentTimeMillis() > moveTimer+200) {
//                player.move(new Vector3f(0,0,-1));
               light.addPosition(new Vector3f(-1000,0,0));
               moveTimer = System.currentTimeMillis();
           }
        }

        if (glfwGetKey(windowID, GLFW_KEY_LEFT) == GL_TRUE) {
            if (System.currentTimeMillis() > moveTimer+200) {
//                player.move(new Vector3f(-1f,0,0));
                light.addPosition(new Vector3f(0,0,-1000));
                moveTimer = System.currentTimeMillis();
            }
        }

        if (glfwGetKey(windowID, GLFW_KEY_RIGHT) == GL_TRUE) {
            if (System.currentTimeMillis() > moveTimer+200) {
                light.addPosition(new Vector3f(0,0,1000));
                moveTimer = System.currentTimeMillis();
            }
        }


        if (glfwGetMouseButton(windowID, GLFW_MOUSE_BUTTON_2) == GL_TRUE) {
//            camera.printPosition();
            if (System.currentTimeMillis() > clickTimer+200) {
                mousePicker.update();
                Vector3f mapPoint = mousePicker.getCurrentTerrainPoint();
                if (mapPoint != null) {
                    player.setPosition(mapPoint);
                }
                clickTimer = System.currentTimeMillis();
            }
            camera.calculateCameraPosition();
        }

        if (glfwGetMouseButton(windowID, GLFW_MOUSE_BUTTON_1) == GL_TRUE) {
//            camera.printPosition();
            if (System.currentTimeMillis() > clickTimer+200) {
                mousePicker.update();
                Vector3f mapPoint = mousePicker.getCurrentTerrainPoint();
                if (mapPoint != null) {
                    player.moveTo(mapPoint);
                }
                clickTimer = System.currentTimeMillis();
            }
            camera.calculateCameraPosition();
        }
    }
}
