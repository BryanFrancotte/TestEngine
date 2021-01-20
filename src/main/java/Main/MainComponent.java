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
        this.window.create();
    }

    public void run() {
        this.init();
        while(!this.window.shouldClose()) {
            this.update();
            this.render();
            if(Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) return;
        }
        this.window.destroy();
    }

    public void update() {
        this.window.update();
        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            System.out.println("X: " + Input.getMouseX() + ", Y: " + Input.getMouseY());
        }
    }

    public void render() {
        this.window.swapBuffers();
    }

    public static void main(String[] args) {
        new MainComponent().start();
    }
}
