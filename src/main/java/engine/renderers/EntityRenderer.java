package engine.renderers;

import engine.entites.Entity;
import engine.textures.ModelTexture;
import engine.models.TexturedModel;
import engine.shaders.StaticShader;
import engine.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjglx.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        this.shader.start();
        this.shader.loadProjectionMatrix(projectionMatrix);
        this.shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for(TexturedModel model : entities.keySet()) {
            this.prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity : batch) {
                this.prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getMeshModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            this.unbidTextureModel();
        }
    }

    private void prepareTexturedModel(TexturedModel model) {
        GL30.glBindVertexArray(model.getMeshModel().getVoaID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture modelTexture = model.getTexture();
        if(modelTexture.hasTransparency()) {
            MasterRenderer.disableCulling(); // TODO: heritage with MasterRenderer?
        }
        this.shader.loadFakeLighting(modelTexture.useFakeLighting());
        this.shader.loadShineVariables(modelTexture.getShineDamper(), modelTexture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL30.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void unbidTextureModel(){
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix =
                Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        this.shader.loadTransformationMatrix(transformationMatrix);
    }


}
