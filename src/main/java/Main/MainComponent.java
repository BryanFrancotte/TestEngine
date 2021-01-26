package Main;

import engine.entites.Camera;
import engine.entites.Entity;
import engine.entites.Light;
import engine.graphics.*;
import engine.models.MeshModel;
import engine.io.Input;
import engine.io.Window;

import engine.models.TexturedModel;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainComponent implements Runnable{

    private Thread gameEngine;
    private Window window;
    private Loader loader;
    private MasterRenderer renderer;

    private MeshModel model;
    private TexturedModel staticModel;
    private Entity entity;
    private List<Entity> allCubes;
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
        this.window.setBackgroundColor(1.0f, 0.5f, 0.0f);
        this.window.create();
        this.loader = new Loader();
        this.renderer = new MasterRenderer();

        this.model = OBJLoader.loadObjModel("cube", this.loader);
        this.staticModel = new TexturedModel(this.model, new Texture(this.loader.loadTexture("/textures/blue.png")));
        this.allCubes = new ArrayList<>();
        this.staticModel.getTexture().setShineDamper(100000);
        this.staticModel.getTexture().setReflectivity(0);
        Random random = new Random();
        for(int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            this.allCubes.add(new Entity(staticModel, new Vector3f(x, y, z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
        }
        this.light = new Light(new engine.maths.Vector3f(3000,2000, 3000), new engine.maths.Vector3f(1, 1, 1));
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
        camera.move();
        for(Entity cube : this.allCubes) {
            this.renderer.processEntity(cube);
        }
        this.renderer.render(light, camera);
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
