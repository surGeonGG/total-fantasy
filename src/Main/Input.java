package Main;

import Entities.Player;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public class Input {

    private static Main main;

    private static Camera camera;

    private static World world;

    private static Player player;

    private static long windowID;

    private static long rebuildTimer = 0;

    private static long moveTimer = 0;

    public Input(Main main) {

        this.main = main;

        this.world = main.getWorld();

        this.camera = main.getCamera();

        this.windowID = main.getWindow().getWindowID();

        this.player = main.getPlayer();

    }

    public static void update() {



        if (glfwGetKey(windowID, GLFW_KEY_A) == GL_TRUE) {
            camera.addPosition(new Vector3f(-0.01f*camera.getPosition().z, 0f, 0f));
        }
        if (glfwGetKey(windowID, GLFW_KEY_D) == GL_TRUE) {
            camera.addPosition(new Vector3f(0.01f*camera.getPosition().z, 0f, 0f));

        }
        if (glfwGetKey(windowID, GLFW_KEY_S) == GL_TRUE) {
            camera.addPosition(new Vector3f(0f, -0.01f*camera.getPosition().z, 0f));

        }
        if (glfwGetKey(windowID, GLFW_KEY_W) == GL_TRUE) {
            camera.addPosition(new Vector3f(0f, 0.01f*camera.getPosition().z, 0f));
        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_ADD) == GL_TRUE) {
            if (camera.getPosition().z < -0.2f) camera.addPosition(new Vector3f(0f, 0f, 3f));
            System.out.println(camera.getPosition().z);
        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_SUBTRACT) == GL_TRUE) {
            if (camera.getPosition().z >= -1000) camera.addPosition(new Vector3f(0f, 0f, -3f));
            System.out.println(camera.getPosition().z);

        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_2) == GL_TRUE) {
            if (camera.getPosition().z >= -1000) camera.addPitch(-1f);
        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_8) == GL_TRUE) {
            if (camera.getPosition().z >= -1000) camera.addPitch(1f);
        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_4) == GL_TRUE) {
            if (camera.getPosition().z >= -1000) camera.addYaw(-1f);
        }

        if (glfwGetKey(windowID, GLFW_KEY_KP_6) == GL_TRUE) {
            if (camera.getPosition().z >= -1000) camera.addYaw(1f);
        }

        if (glfwGetKey(windowID, GLFW_KEY_SPACE) == GL_TRUE) {
            System.out.println(camera.getPosition());
            System.out.println();
            System.out.println(camera.getPosition().z);

        }

        if (glfwGetKey(windowID, GLFW_KEY_F5) == GL_TRUE) {
            if (System.currentTimeMillis() > rebuildTimer+1000) world.rebuild();
            rebuildTimer = System.currentTimeMillis();
        }

        if (glfwGetKey(windowID, GLFW_KEY_UP) == GL_TRUE) {
           if (System.currentTimeMillis() > moveTimer+200) {
                player.move(new Vector3f(0,1,0));
                moveTimer = System.currentTimeMillis();
           }
        }

        if (glfwGetKey(windowID, GLFW_KEY_DOWN) == GL_TRUE) {
           if (System.currentTimeMillis() > moveTimer+200) {
                player.move(new Vector3f(0,-1,0));
                moveTimer = System.currentTimeMillis();
           }
        }

        if (glfwGetKey(windowID, GLFW_KEY_LEFT) == GL_TRUE) {
            if (System.currentTimeMillis() > moveTimer+200) {
                player.move(new Vector3f(-1,0,0));
                moveTimer = System.currentTimeMillis();
            }
        }

        if (glfwGetKey(windowID, GLFW_KEY_RIGHT) == GL_TRUE) {
            if (System.currentTimeMillis() > moveTimer+200) {
                player.move(new Vector3f(1,0,0));
                moveTimer = System.currentTimeMillis();
            }
        }

        if (glfwGetMouseButton(windowID, GLFW_MOUSE_BUTTON_1) == GL_TRUE) {
            DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
            IntBuffer windowX = BufferUtils.createIntBuffer(1);
            IntBuffer windowY = BufferUtils.createIntBuffer(1);

            glfwGetWindowSize(windowID, windowX, windowY);
            glfwGetCursorPos(windowID, posX, posY);

            Matrix4f projection = camera.getProjection();



        }
    }
}
