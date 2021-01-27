package engine.renderers;

import engine.graphics.Texture;
import engine.shaders.TerrainShader;
import engine.terrain.Terrain;
import engine.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;

import java.util.List;

public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        this.shader.start();
        this.shader.loadProjectionMatrix(projectionMatrix);
        this.shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for(Terrain terrain : terrains) {
            this.prepareTerrain(terrain);
            this.loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            this.unbidTextureModel();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        GL30.glBindVertexArray(terrain.getModel().getVoaID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Texture texture = terrain.getTexture();
        this.shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL30.GL_TEXTURE_2D, texture.getTextureID());
    }

    private void unbidTextureModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix =
                Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        this.shader.loadTransformationMatrix(transformationMatrix);
    }
}
