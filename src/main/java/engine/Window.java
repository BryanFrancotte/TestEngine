package engine;

import engine.Input;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    public int frames;
    public long time;

    public Input input;

    // public static engine.Window window = null;

    public Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "TestEngine";
        GLFWErrorCallback.createPrint(System.err).set();
    }

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        GLFWErrorCallback.createPrint(System.err).set();
    }

    public void create() {
        //Init GLFW
        if(!glfwInit())
            throw new IllegalStateException("Unable to init GLFW");

        this.input = new Input();
        // Configure the GLFW
        glfwDefaultWindowHints(); // optional !
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window is resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // the window is maximized

        // Create the window
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(this.glfwWindow == NULL)
            throw new RuntimeException("Failed to create GLFW window");

        // Make openGL context current
        glfwMakeContextCurrent(this.glfwWindow);

        // Setting the inputs callback
        glfwSetKeyCallback(this.glfwWindow, this.input.getKeyboardCallback());
        glfwSetCursorPosCallback(this.glfwWindow, this.input.getMouseMoveCallback());
        glfwSetMouseButtonCallback(this.glfwWindow, this.input.getMouseButtonsCallback());

        // Enabling v-Sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(this.glfwWindow);

        this.time = System.currentTimeMillis();
    }

    public void update() {
        glfwPollEvents();
        this.frames++;
        if(System.currentTimeMillis() > this.time + 1000) {
            glfwSetWindowTitle(this.glfwWindow, this.title + " | FPS: " + this.frames);
            this.time = System.currentTimeMillis();
            this.frames = 0;
        }
    }

    public void swapBuffers() {
        glfwSwapBuffers(this.glfwWindow);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.glfwWindow);
    }

    public void destroy() {
        this.input.destroy();
        glfwWindowShouldClose(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);
        glfwTerminate();
    }
}
