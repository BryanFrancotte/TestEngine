package engine.renderers;

import engine.entites.Camera;
import engine.entites.Entity;
import engine.entites.Light;
import engine.io.Window;
import engine.models.TexturedModel;
import engine.shaders.StaticShader;
import engine.shaders.TerrainShader;
import engine.terrain.Terrain;
import org.lwjgl.opengl.GL11;
import org.lwjglx.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: PLZ change to a better name...
public class MasterRenderer {
    private static final float FOV = 70.0f;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f;


    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    private TerrainShader terrainShader = new TerrainShader();
    private TerrainRenderer terrainRenderer;

    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer() {
        enableCulling();
        this.createProjectionMatrix();
        this.entityRenderer = new EntityRenderer(this.shader, this.projectionMatrix);
        this.terrainRenderer = new TerrainRenderer(this.terrainShader, this.projectionMatrix);
    }

    public static void enableCulling() { // TODO: is the static necessary?
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() { // TODO: is the static necessary?
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(Light light, Camera camera) {
        this.shader.start();
        this.shader.loadSkyColour(0.5f, 0.5f, 0.5f);
        this.shader.loadLight(light);
        this.shader.loadViewMatrix(camera);
        this.entityRenderer.render(this.entities);
        this.shader.stop();
        this.terrainShader.start();
        this.terrainShader.loadSkyColour(0.5f, 0.5f, 0.5f);
        this.terrainShader.loadLight(light);
        this.terrainShader.loadViewMatrix(camera);
        this.terrainRenderer.render(terrains);
        this.terrainShader.stop();

        this.entities.clear();
        this.terrains.clear();
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = this.entities.get(entityModel);
        if(batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            this.entities.put(entityModel, newBatch);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void destroy() {
        this.shader.destroy();
        this.terrainShader.destroy();
    }

    // TODO: try to understand this shit.
    private void createProjectionMatrix () {
        float aspectRatio = Window.getAspectRation(); // TODO: Move to window class ?
        float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        this.projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
