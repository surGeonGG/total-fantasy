import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private static long windowID;

    private static Input input;

    public Window(int width, int height, String title) {



        windowID = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(windowID, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2 );

        if (windowID == MemoryUtil.NULL)
            throw new IllegalStateException("Window failed");

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
