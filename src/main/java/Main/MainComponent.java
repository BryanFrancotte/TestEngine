package Main;

import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Input;
import engine.io.Window;
import engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

public class MainComponent implements Runnable{

    private Thread gameEngine;
    private Window window;
    private Renderer renderer;
    private Shader shader;

    private Mesh mesh = new Mesh(new Vertex[] {
            new Vertex(new Vector3f(-0.5f, 0.5f, 0.0f)),
            new Vertex(new Vector3f(0.5f, 0.5f, 0.0f)),
            new Vertex(new Vector3f(0.5f, -0.5f, 0.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f))
    }, new int[] {
            0, 1, 2,
            0, 3, 2
    });

    public void start() {
        this.gameEngine = new Thread(this, "gameEngine");
        this.gameEngine.start();
    }

    public void stop() {
    }

    public void init() {
        this.window = new Window();
        this.shader = new Shader("/shaders/basicVertex.vs", "/shaders/basicFragment.fs");
        this.renderer = new Renderer(this.shader);
        this.window.setBackgroundColor(1.0f, 0.0f, 0.0f);
        this.window.create();
        this.mesh.create();
        this.shader.create();
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
        this.renderer.renderMesh(this.mesh);
        this.window.swapBuffers();
    }

    public static void main(String[] args) {
        new MainComponent().start();
    }
}
