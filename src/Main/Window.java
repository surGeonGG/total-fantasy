package Main;

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
import static org.lwjgl.opengl.GL43.*;

public class Window {

    private static long windowID;

    private static Input input;

    public Window(int WINDOW_WIDTH, int WINDOW_HEIGHT, String title) {



        //start of copy/paste
        GLFWErrorCallback.createPrint().set();
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize glfw");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        if ( Platform.get() == Platform.MACOSX )
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        //glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);




        windowID = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, title, MemoryUtil.NULL, MemoryUtil.NULL);
        glfwMakeContextCurrent(windowID);
        GLCapabilities caps = GL.createCapabilities();
        //Callback debugProc = GLUtil.setupDebugMessageCallback();
        ;
        //end of copy/paste


        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(windowID, (vidmode.width() - WINDOW_WIDTH) / 2, (vidmode.height() - WINDOW_HEIGHT) / 2 );

        if (windowID == MemoryUtil.NULL)
            throw new IllegalStateException("Main.Window failed");

        glfwShowWindow(windowID);
        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);

    }

    public static long getWindowID() {
        return windowID;
    }

    public static void update() {

        glfwPollEvents();

    }

    public static void swapBuffers() {
        glfwSwapBuffers(windowID);

    }

    public static void destroy() {

        glfwDestroyWindow(windowID);
    }
}
