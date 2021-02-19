package Main;

import engine.textures.ModelTexture;
import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;
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
    private TexturedModel treeModel;
    private TexturedModel lowPolyTreeModel;
    private TexturedModel grassModel;
    private TexturedModel flowerModel;
    private TexturedModel fernModel;
    private List<Entity> entities;
//    private Entity entity;

    private Terrain terrain;
    private Terrain terrain2;

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

        //**************TERRAIN TEXTURE INIT**************

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("/textures/grassy2.png"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("/textures/mud.png"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("/textures/grassFlowers.png"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("/textures/path.png"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("/textures/blendMap.png"));

        //************************************************

        this.terrain = new Terrain(0, 0, this.loader, texturePack, blendMap);
        this.terrain2 = new Terrain(1, 0, this.loader, texturePack, blendMap);
        this.model = OBJLoader.loadObjModel("dragon", this.loader);
        this.staticModel = new TexturedModel(this.model, new ModelTexture(this.loader.loadTexture("/textures/blue.png")));
        this.treeModel = new TexturedModel(OBJLoader.loadObjModel("tree", this.loader), new ModelTexture(this.loader.loadTexture("/textures/tree.png")));
        this.lowPolyTreeModel = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", this.loader), new ModelTexture((this.loader.loadTexture("/textures/lowPolyTree.png"))));
        this.grassModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", this.loader), new ModelTexture(this.loader.loadTexture("/textures/grassTexture.png")));
        this.flowerModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", this.loader), new ModelTexture(this.loader.loadTexture(("/textures/flower.png"))));
        this.fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", this.loader), new ModelTexture(this.loader.loadTexture("/textures/fern.png")));

        this.grassModel.getTexture().setHasTransparency(true);
        this.grassModel.getTexture().setUseFakeLighting(true);
        this.flowerModel.getTexture().setHasTransparency(true);
        this.flowerModel.getTexture().setUseFakeLighting(true);
        this.fernModel.getTexture().setHasTransparency(true);

        this.entities = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 400; i++) {
            if(i % 7 == 0) {
                entities.add(new Entity(this.grassModel, new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400), 0, 0, 0,1.8f));
                entities.add(new Entity(this.flowerModel, new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400), 0, 0, 0,2.3f));
            }
            if(i % 3 ==0) {
                entities.add(new Entity(this.fernModel, new Vector3f(random.nextFloat() * 400 - 200, 0, random.nextFloat() * -400), 0, random.nextFloat() * 360, 0, 0.9f));
                entities.add(new Entity(this.lowPolyTreeModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, random.nextFloat() * 360,0, random.nextFloat() * 0.1f + 0.6f));
                entities.add(new Entity(this.treeModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, random.nextFloat() * 1 + 4));
            }
        }

//        this.entity = new Entity(this.staticModel, new Vector3f(0,-0,-25), 0,0,0,1);
        this.staticModel.getTexture().setShineDamper(10);
        this.staticModel.getTexture().setReflectivity(1);
        this.entities.add(new Entity(this.staticModel, new Vector3f(0,25,-150), 0,0,0,10));

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
//        this.entity.increaseRotation(0,1,0);
        this.camera.move();

        this.renderer.processTerrain(this.terrain);
        this.renderer.processTerrain(this.terrain2);
//        this.renderer.processEntity(this.entity);
        for(Entity entity : this.entities) {
            this.renderer.processEntity(entity);
        }

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
