package engine.graphics;

import engine.entites.Camera;
import engine.entites.Entity;
import engine.entites.Light;
import engine.models.TexturedModel;
import engine.shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: PLZ change to a better name...
public class MasterRenderer {
    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(Light light, Camera camera) {
        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(this.entities);
        shader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = this.entities.get(entityModel);
        if(batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void destroy() {
        shader.destroy();
    }
}
