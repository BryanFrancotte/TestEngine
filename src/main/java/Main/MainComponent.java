package Main;

import engine.Input;
import engine.Window;
import org.lwjgl.glfw.GLFW;

public class MainComponent implements Runnable{

    private Thread gameEngine;
    private Window window;

    public void start() {
        this.gameEngine = new Thread(this, "gameEngine");
        this.gameEngine.start();
    }

    public void stop() {
    }

    public void init() {
        this.window = new Window();
        this.window.setBackgroundColor(1.0f, 0.0f, 0.0f);
        this.window.create();
    }

    public void run() {
        this.init();
        while(!this.window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            this.update();
            this.render();
            if(Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
                window.setFullScreen(!window.isFullScreen());
            }
        }
        this.window.destroy();
    }

    public void update() {
        this.window.update();
        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            System.out.println("X: " + Input.getMouseX() + ", Y: " + Input.getMouseY());
            System.out.println("scrollX: " + Input.getScrollX() + ", scrollY: " + Input.getScrollY());
        }
    }

    public void render() {
        this.window.swapBuffers();
    }

    public static void main(String[] args) {
        new MainComponent().start();
    }
}
