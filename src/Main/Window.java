package Main;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.Platform;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBDebugOutput.*;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL43.*;

public class Window {

    private static long windowID;
    private static final boolean fullscreen = true;
    private final int FULLSCREEN_WIDTH = 1920;
    private final int FULLSCREEN_HEIGHT = 1080;
    private final int RESOLUTION_WIDTH = 1000;
    private final int RESOLUTION_HEIGHT = 1000;

    private static Input input;

    public Window(int WINDOW_WIDTH, int WINDOW_HEIGHT, String title) {
        GLFWErrorCallback.createPrint().set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize glfw");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_SAMPLES, 8);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if (Platform.get() == Platform.MACOSX)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        if (fullscreen) {
            windowID = glfwCreateWindow(FULLSCREEN_WIDTH, FULLSCREEN_WIDTH, title, glfwGetPrimaryMonitor(), MemoryUtil.NULL);
        }
        else {
            windowID = glfwCreateWindow(RESOLUTION_WIDTH, RESOLUTION_HEIGHT, title, MemoryUtil.NULL, MemoryUtil.NULL);
        }
        glfwMakeContextCurrent(windowID);
        GLCapabilities caps = GL.createCapabilities();
        if (fullscreen) {
            GL11.glViewport(0,0, FULLSCREEN_WIDTH, FULLSCREEN_HEIGHT);
        }
        else {
            GL11.glViewport(0, 0, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
        }
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowID, (vidmode.width() - WINDOW_WIDTH) / 2, (vidmode.height() - WINDOW_HEIGHT) / 2 );
        if (windowID == MemoryUtil.NULL)
            throw new IllegalStateException("Main.Window failed");
        glfwShowWindow(windowID);
        glfwSwapInterval(1);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowID, width, height);
        System.out.println(width.get() + " " + height.get());
    }

    public static long getWindowID() {
        return windowID;
    }

    public static void swapBuffers() {
        glfwSwapBuffers(windowID);
    }

    public static void destroy() {
        glfwDestroyWindow(windowID);
    }
}
