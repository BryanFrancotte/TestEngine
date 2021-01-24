package Main;

import engine.entites.Camera;
import engine.entites.Entity;
import engine.graphics.Loader;
import engine.graphics.OBJLoader;
import engine.graphics.Texture;
import engine.models.MeshModel;
import engine.graphics.Renderer;
import engine.io.Input;
import engine.io.Window;

import engine.models.TexturedModel;
import engine.shaders.StaticShader;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.Sys;
import org.lwjglx.util.vector.Vector3f;

public class MainComponent implements Runnable{

    private Thread gameEngine;
    private Window window;
    private Loader loader;
    private Renderer renderer;
    private StaticShader shader;

    private MeshModel model;
    private Texture texture;
    private TexturedModel staticModel;
    private Entity entity;
    private Camera camera;

    public void start() {
        this.gameEngine = new Thread(this, "gameEngine");
        this.gameEngine.start();
    }

    private void stop() {
    }

    private void init() {
        this.window = new Window();
        this.window.setBackgroundColor(0.0f, 0.0f, 0.0f);
        this.window.create();
        this.loader = new Loader();
        this.shader = new StaticShader();
        this.renderer = new Renderer(this.window, this.shader);

        this.model = OBJLoader.loadObjModel("stall", this.loader);
        System.out.println("Object Loaded");
        this.texture = new Texture(this.loader.loadTexture("/textures/stallTexture.png"));
        this.staticModel = new TexturedModel(this.model, this.texture);
        this.entity = new Entity(staticModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
        this.camera = new Camera();
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
        this.close();
    }

    private void update() {
        this.window.update();
        if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            System.out.println("X: " + Input.getMouseX() + ", Y: " + Input.getMouseY());
            System.out.println("scrollX: " + Input.getScrollX() + ", scrollY: " + Input.getScrollY());
        }
    }

    private void render() {
        this.entity.increaseRotation(0,1,0);
        camera.move();
        this.shader.start();
        this.shader.loadViewMatrix(camera);
        this.renderer.render(this.entity, this.shader);
        this.shader.stop();
        this.window.swapBuffers();
    }

    private void close() {
        this.shader.destroy();
        this.loader.destroy();
        this.window.destroy();
    }

    public static void main(String[] args) {
        new MainComponent().start();
    }
}
