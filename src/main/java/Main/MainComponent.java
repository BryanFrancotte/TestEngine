package Main;

import engine.entites.Camera;
import engine.entites.Entity;
import engine.entites.Light;
import engine.graphics.*;
import engine.models.MeshModel;
import engine.io.Input;
import engine.io.Window;

import engine.models.TexturedModel;
import engine.renderers.MasterRenderer;
import engine.terrain.Terrain;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

public class MainComponent implements Runnable{

    private Thread gameEngine;
    private Window window;
    private Loader loader;
    private MasterRenderer renderer;

    private Terrain terrain;
    private Terrain terrain2;

    private MeshModel model;
    private TexturedModel staticModel;
    private Entity entity;


    private Light light;
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
        this.renderer = new MasterRenderer();

        this.terrain = new Terrain(0, 0, this.loader, new Texture(this.loader.loadTexture("/textures/grass.png")));
        this.terrain2 = new Terrain(1, 0, this.loader, new Texture(this.loader.loadTexture("/textures/grass.png")));

        this.model = OBJLoader.loadObjModel("tree", this.loader);
        this.staticModel = new TexturedModel(this.model, new Texture(this.loader.loadTexture("/textures/tree.png")));
        this.entity = new Entity(this.staticModel, new Vector3f(0,-0,-25), 0,0,0,1);
        this.staticModel.getTexture().setShineDamper(10);
        this.staticModel.getTexture().setReflectivity(1);

        this.light = new Light(new engine.maths.Vector3f(3000,2000, 2000), new engine.maths.Vector3f(1, 1, 1));
        this.camera = new Camera();
    }

    public void run() {
        this.init();
        while(!this.window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            this.update();
            this.render();
            if(Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
                this.window.setFullScreen(!window.isFullScreen());
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
        this.camera.move();

        this.renderer.processTerrain(this.terrain);
        this.renderer.processTerrain(this.terrain2);
        this.renderer.processEntity(this.entity);

        this.renderer.render(this.light, this.camera);
        this.window.swapBuffers();
    }

    private void close() {
        this.renderer.destroy();
        this.loader.destroy();
        this.window.destroy();
    }

    public static void main(String[] args) {
        new MainComponent().start();
    }
}
