package engine.io;

import engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    // TODO: Check if this cause issue in the future?
    private static int width;
    private static int height;
    private String title;
    private long glfwWindow;

    private Vector3f background = new Vector3f(0.5f, 0.5f, 0.5f);

    private GLFWWindowSizeCallback sizeCallback;
    private boolean isResized;
    private boolean isFullScreen;
    private int[] windowPosX = new int[1];
    private int[] windowPosY = new int[1];

    public int frames;
    public long time;

    public Input input;

    // public static engine.io.Window window = null;

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

    public static float getAspectRation() {
        return (float) width/ (float) height;
    }

    public void create() {
        //Init GLFW
        if(!GLFW.glfwInit())
            throw new IllegalStateException("Unable to init GLFW");

        this.input = new Input();
        // Configure the GLFW
        GLFW.glfwDefaultWindowHints(); // optional !
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window is resizable
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE); // the window is maximized

        // Create the window
        this.glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, isFullScreen? GLFW.glfwGetPrimaryMonitor() : NULL, NULL);
        if(this.glfwWindow == NULL)
            throw new RuntimeException("Failed to create GLFW window");

        // Make openGL context current
        GLFW.glfwMakeContextCurrent(this.glfwWindow);

        // Make the window openGL enable.
        GL.createCapabilities();

        // Depth test
        GL11.glEnable(GL11.GL_DEPTH_TEST); // This needs to be clear

        this.createCallbacks();

        // Enabling v-Sync
        GLFW.glfwSwapInterval(1);
        // Make the window visible
        GLFW.glfwShowWindow(this.glfwWindow);

        this.time = System.currentTimeMillis();
    }

    private void createCallbacks() {
        this.sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };

        // Setting the inputs callback
        GLFW.glfwSetKeyCallback(this.glfwWindow, this.input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(this.glfwWindow, this.input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(this.glfwWindow, this.input.getMouseButtonsCallback());
        GLFW.glfwSetScrollCallback(this.glfwWindow, this.input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(this.glfwWindow, this.sizeCallback);
    }

    public void update() {
        if(this.isResized) {
            GL11.glViewport(0,0, width, height);
            this.isResized = false;
        }
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();
        this.frames++;
        if(System.currentTimeMillis() > this.time + 1000) {
            GLFW.glfwSetWindowTitle(this.glfwWindow, this.title + " | FPS: " + this.frames);
            this.time = System.currentTimeMillis();
            this.frames = 0;
        }
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(this.glfwWindow);
    }

    public void setBackgroundColor(float red, float green, float blue) {
            this.background.set(red, green, blue);
        }

        public boolean shouldClose() {
            return GLFW.glfwWindowShouldClose(this.glfwWindow);
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public boolean isFullScreen() {
            return this.isFullScreen;
        }

        public void setFullScreen(boolean isFullScreen) {
            this.isFullScreen = isFullScreen;
            this.isResized = true;
            if(this.isFullScreen) {
                GLFW.glfwGetWindowPos(this.glfwWindow, this.windowPosX, this.windowPosY);
                GLFW.glfwSetWindowMonitor(this.glfwWindow, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
            } else {
                GLFW.glfwSetWindowMonitor(this.glfwWindow, NULL, this.windowPosX[0], this.windowPosY[0], width, height, 0);
            }
    }

    public void destroy() {
        this.input.destroy();
        this.sizeCallback.free();
        GLFW.glfwWindowShouldClose(this.glfwWindow);
        GLFW.glfwDestroyWindow(this.glfwWindow);
        GLFW.glfwTerminate();
    }
}
